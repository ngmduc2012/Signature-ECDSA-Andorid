package com.example.signature;
import com.example.signature.ECDSA.Ecdsa;
import com.example.signature.ECDSA.PrivateKey;
import com.example.signature.ECDSA.PublicKey;
import com.example.signature.ECDSA.Signature;
import com.example.signature.ECDSA.utils.ByteString;

import org.junit.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.Assert.assertTrue;


public class OpenSSLTest {

    @Test
    public void testAssign() throws URISyntaxException, IOException {
        // Generated by:openssl ecparam -name secp256k1 - genkey - out privateKey.pem
        String privateKeyPem = Utils.read("C:\\Users\\Duc\\OneDrive - ptit.edu.vn\\12_Thesis\\Openssl_Key\\privateKey.pem");

        PrivateKey privateKey = PrivateKey.fromPem(privateKeyPem);

        String message = Utils.read("C:\\Users\\Duc\\OneDrive - ptit.edu.vn\\12_Thesis\\Openssl_Key\\Doc.txt");

        Signature signature = Ecdsa.sign(message, privateKey);

        PublicKey publicKey = privateKey.publicKey();

        assertTrue(Ecdsa.verify(message, signature, publicKey));
    }

    @Test
    public void testVerifySignature() throws IOException, URISyntaxException {
        // openssl ec -in privateKey.pem - pubout - out publicKey.pem
        String publicKeyPem = Utils.read("C:\\Users\\Duc\\OneDrive - ptit.edu.vn\\12_Thesis\\Openssl_Key\\publicKey.pem");
        // openssl dgst -sha256 -sign privateKey.pem -out signature.binary message.txt
        ByteString signatureBin = new ByteString(Utils.readBytes("C:\\Users\\Duc\\OneDrive - ptit.edu.vn\\12_Thesis\\Openssl_Key\\signatureBinary.txt"));

        String message = Utils.read("C:\\Users\\Duc\\OneDrive - ptit.edu.vn\\12_Thesis\\Openssl_Key\\Doc.txt");

        PublicKey publicKey = PublicKey.fromPem(publicKeyPem);

        Signature signature = Signature.fromDer(signatureBin);

        assertTrue(Ecdsa.verify(message, signature, publicKey));
    }
}
