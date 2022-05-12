package com.daquv.Utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AESUtil {
    private static String key;
    private static String salt;
    private static String utsalt;

    @Value("${daquv.user.key}")
    public void setKey(final String key) { AESUtil.key = key;}

    @Value("${daquv.user.salt-key:}")
    public void setSalt(final String saltkey) {
        AESUtil.salt = saltkey;
    }

    @Value("${daquv.user.daquv-salt-key:}")
    public void setUtSalt(final String okexsaltkey) {
        AESUtil.utsalt = okexsaltkey;
    }

    private static final String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String ivBase64 = "3Q7e9eKBaQcZBXurvjLN2g=="; //IV 는 128, 256 상관없이 16바이트로

    public static String encrypt(String plainText) {
        try {
            return AESUtil.cbcEncrypt(plainText, Base64Util.encode(AESUtil.key) );
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String decrypt(String messageBase64) {
        try{
            return AESUtil.cbcDecrypt(messageBase64, Base64Util.encode(AESUtil.key) );
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String cbcEncrypt(String plainText, String keyBase64) throws Exception {
        byte[] plainTextArray = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyArray = DatatypeConverter.parseBase64Binary(keyBase64);
        byte[] iv = DatatypeConverter.parseBase64Binary(AESUtil.ivBase64);

        SecretKeySpec secretKey = new SecretKeySpec(keyArray, "AES");
        Cipher cipher = Cipher.getInstance(AESUtil.CBC_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return new String(DatatypeConverter.printBase64Binary(cipher.doFinal(plainTextArray)));
    }

    public static String cbcDecrypt(String messageBase64, String keyBase64) throws Exception {
        byte[] messageArray = DatatypeConverter.parseBase64Binary(messageBase64);
        byte[] keyArray = DatatypeConverter.parseBase64Binary(keyBase64);
        byte[] iv = DatatypeConverter.parseBase64Binary(AESUtil.ivBase64);

        SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
        Cipher cipher = Cipher.getInstance(AESUtil.CBC_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return new String(cipher.doFinal(messageArray));
    }

    public static String getSHA256PasswordNoneID(String pwd) {
        try {
            return bytesToHex2(SHA256Encrypt(salt + bytesToHex2(SHA256Encrypt(pwd))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSHA256String(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            return bytesToHex2(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
        }

        return "";
    }

    public static String getSHA256String(String str) {
        byte[] bytes;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = str.getBytes();
        }
        return getSHA256String(bytes);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex2(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] SHA256Encrypt(final String content) {
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.reset();
            sh.update(content.getBytes());
            return sh.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSHA256Password(String uid, String pwd) {
        try {
        
            return bytesToHex2(SHA256Encrypt(uid + salt + bytesToHex2(SHA256Encrypt(pwd))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    public static String getUtPasswordHash(String password) {
        byte[] salt = utsalt.getBytes();
        int iterations = 2000;
        int derivedKeyLength = 16;
        try {
            byte[] pwd = AESUtil.getEncryptedPassword(password, salt, iterations, derivedKeyLength);
            return bytesToHex3(pwd);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getUtHash(String password) {
        byte[] salt = utsalt.getBytes();
        int iterations = 2000;
        int derivedKeyLength = 16;
        try {
            byte[] pwd = AESUtil.getEncrypted(password, salt, iterations, derivedKeyLength);
            return bytesToHex3(pwd);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getEncryptedPassword(String password, byte[] salt, int iterations, int derivedKeyLength)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);

        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return f.generateSecret(spec).getEncoded();
    }

    public static byte[] getEncrypted(String password, byte[] salt, int iterations, int derivedKeyLength)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);

        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return f.generateSecret(spec).getEncoded();
    }

    private final static char[] hexArray3 = "0123456789abcdef".toCharArray();
    public static String bytesToHex3(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray3[v >>> 4];
            hexChars[j * 2 + 1] = hexArray3[v & 0x0F];
        }
        return new String(hexChars);
    }
}
