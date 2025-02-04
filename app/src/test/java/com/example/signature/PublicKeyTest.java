package com.example.signature;
import com.example.signature.ECDSA.PrivateKey;
import com.example.signature.ECDSA.PublicKey;
import com.example.signature.ECDSA.utils.ByteString;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class PublicKeyTest {

    @Test
    public void testPemConversion() {
        PrivateKey privateKey = new PrivateKey();
        PublicKey publicKey1 = privateKey.publicKey();
        String pem = publicKey1.toPem();
        PublicKey publicKey2 = PublicKey.fromPem(pem);
        assertEquals(publicKey1.point.x, publicKey2.point.x);
        assertEquals(publicKey1.point.y, publicKey2.point.y);
        assertEquals(publicKey1.curve, publicKey2.curve);
    }

    @Test
    public void testDerConversion() {
        PrivateKey privateKey = new PrivateKey();
        PublicKey publicKey1 = privateKey.publicKey();
        ByteString der = publicKey1.toDer();
        PublicKey publicKey2 = PublicKey.fromDer(der);
        assertEquals(publicKey1.point.x, publicKey2.point.x);
        assertEquals(publicKey1.point.y, publicKey2.point.y);
        assertEquals(publicKey1.curve, publicKey2.curve);
    }

    @Test
    public void testStringConversion() {
        PrivateKey privateKey = new PrivateKey();
        PublicKey publicKey1 = privateKey.publicKey();
        ByteString string = publicKey1.toByteString();
        PublicKey publicKey2 = PublicKey.fromString(string);
        assertEquals(publicKey1.point.x, publicKey2.point.x);
        assertEquals(publicKey1.point.y, publicKey2.point.y);
        assertEquals(publicKey1.curve, publicKey2.curve);
    }
}
