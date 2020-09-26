package com.example.signature.ECDSA;


import com.example.signature.ECDSA.utils.BinaryAscii;
import com.example.signature.ECDSA.utils.RandomInteger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Ecdsa {

    /**
     * @param message
     * @param privateKey
     * @param hashfunc
     * @return
     */

    public static Signature sign(String message, PrivateKey privateKey, MessageDigest hashfunc) {
        //Step 1: e = HASH(m) ->  z = HASH(m)
        byte[] hashMessage = hashfunc.digest(message.getBytes());
        BigInteger numberMessage = BinaryAscii.numberFromString(hashMessage);
        Curve curve = privateKey.curve;
        //Step 3: find k random [1,n-1]
        BigInteger randNum = RandomInteger.between(BigInteger.ONE, curve.N);
        //Step 4: find (x,y) = k*G
        Point randomSignPoint = Math.multiply(curve.G, randNum, curve.N, curve.A, curve.P);
        //Step 5: r = x.mod n (r=0, turn Step 3)
        BigInteger r = randomSignPoint.x.mod(curve.N);
        //Step 6: s = k^-1 *(z + r.dA)mod n (s=0, turn Step 3)
        BigInteger s = ((numberMessage.add(r.multiply(privateKey.secret))).multiply(Math.inv(randNum, curve.N))).mod(curve.N);
        //Step 7: (r,s) to save SignatureBinary.txt
        return new Signature(r, s);
    }

    /**
     * @param message
     * @param privateKey
     * @return
     */
    public static Signature sign(String message, PrivateKey privateKey) {
        try {
            return sign(message, privateKey, MessageDigest.getInstance("SHA-256"));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find SHA-256 message digest in provided java environment");
        }
    }

    /**
     * @param message
     * @param signature
     * @param publicKey
     * @param hashfunc
     * @return
     */
    public static boolean verify(String message, Signature signature, PublicKey publicKey, MessageDigest hashfunc) {
        //Step 1: e = HASH(m) ->  z = HASH(m)
        byte[] hashMessage = hashfunc.digest(message.getBytes());
        BigInteger numberMessage = BinaryAscii.numberFromString(hashMessage);
        Curve curve = publicKey.curve;
        BigInteger r = signature.r;
        BigInteger s = signature.s;
        //Step 2: w = s^-1 mod n
        BigInteger w = Math.inv(s, curve.N);
        //Step 3: u1 = z*w mod n
        Point u1 = Math.multiply(curve.G, numberMessage.multiply(w).mod(curve.N), curve.N, curve.A, curve.P);
        //Step 4: u2 = r*w mod n
        Point u2 = Math.multiply(publicKey.point, r.multiply(w).mod(curve.N), curve.N, curve.A, curve.P);
        //Step 5: find point C = u1 * G + u2 * QA
        Point point = Math.add(u1, u2, curve.A, curve.P);
        //Step 6: if r == xC(mod n) True
        return r.compareTo(point.x) == 0;
    }

    /**
     * @param message
     * @param signature
     * @param publicKey
     * @return
     */
    public static boolean verify(String message, Signature signature, PublicKey publicKey) {
        try {
            return verify(message, signature, publicKey, MessageDigest.getInstance("SHA-256"));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find SHA-256 message digest in provided java environment");
        }
    }
}
