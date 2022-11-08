package com.haccercat.client.Pepper;

import com.haccercat.client.libraries.Blake2b;
import com.haccercat.client.libraries.TweetNacl;

import java.util.Arrays;
import java.math.BigInteger;

class Nonce {
    private byte[] nonce;
    public Nonce(byte[] cpk, byte[] spk) {
        Blake2b b2b = new Blake2b(192);
        b2b.update(cpk, 0, 32);
        b2b.update(spk, 0, 32);
        nonce = new byte[24];
        b2b.doFinal(nonce, 0);
    }
    public Nonce(byte[] nonce, byte[] cpk, byte[] spk) {
        Blake2b b2b = new Blake2b(192);
        b2b.update(nonce, 0, 24);
        b2b.update(cpk, 0, 32);
        b2b.update(spk, 0, 32);
        this.nonce = new byte[24];
        b2b.doFinal(this.nonce, 0);
    }
    public Nonce() {
        nonce = new byte[24];
        TweetNacl.randombytes(nonce, 24);
    }
    public Nonce(byte[] nonce) {
        this.nonce = nonce;
    }
    public byte[] bytes() {
        return nonce;
    }
    public void increment() {
        int c = 1;
        for (int idx = 0; idx < 47; idx++) {
            if (idx == 24) c = 1;
            c += (int)nonce[idx % 24] & 0xFF;
            nonce[idx % 24] = (byte)c;
            c >>= 8;
        }
    }
}

public class PepperCrypto {
    public byte[] key;

    public Nonce server_nonce;
    public Nonce client_nonce;

    public byte[] client_secret_key;
    public byte[] client_public_key;
    public byte[] server_public_key;

    private TweetNacl.Box box;
    
    private byte[] session_key;

    public PepperCrypto() {
        server_public_key = hexStringToByteArray("84BCCFFAEAFAC86CEFD3F16E26586ADBF1ED402481FAF3E8B0AC1FE4739B2C28");
        client_nonce = new Nonce();
        client_secret_key = new byte[32];
        TweetNacl.randombytes(client_secret_key, 32);
        client_public_key = new byte[32];
        TweetNacl.crypto_scalarmult_base(client_public_key, client_secret_key);
        key = new byte[32];
        TweetNacl.crypto_box_beforenm(key, server_public_key, client_secret_key);
    }

    public byte[] encrypt(int type, byte[] payload) {
        if (type == 10100) {
            return payload;
        } else if (type == 10101) {
            Nonce nonce = new Nonce(client_public_key, server_public_key);
            byte[] encrypted = new TweetNacl.Box(server_public_key, client_secret_key).after(concat(session_key, client_nonce.bytes(), payload), nonce.bytes());
            return concat(client_public_key, encrypted);
        } else {
            client_nonce.increment();
            return box.after(payload, client_nonce.bytes());
        }
    }

    public byte[] decrypt(int type, byte[] payload) {
        if (type == 20100) {
            session_key = Arrays.copyOfRange(payload, 4, payload.length);
            return payload;
        } else if (type == 20104 || type == 20103) {
            if (type == 20103 && server_nonce == null) return payload;
            Nonce nonce = new Nonce(client_nonce.bytes(), client_public_key, server_public_key);
            byte[] decrypted = new TweetNacl.Box(server_public_key, client_secret_key).open(payload, nonce.bytes());
            server_nonce = new Nonce(Arrays.copyOfRange(decrypted, 0, 24));
            box = new TweetNacl.Box(server_public_key, client_public_key); // temp.
            box.sharedKey = Arrays.copyOfRange(decrypted, 24, 56);
            return Arrays.copyOfRange(decrypted, 56, decrypted.length);
        } else {
            server_nonce.increment();
            return box.open_after(payload, server_nonce.bytes());
        }
    }

    public static byte[] concat(byte[]...arrays) {
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            totalLength += arrays[i].length;
        }
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }
        return result;
    }
    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
