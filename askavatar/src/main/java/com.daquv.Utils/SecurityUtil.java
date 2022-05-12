package com.daquv.Utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class SecurityUtil {
    public static byte[] ivBytes = new byte[16];

    public SecurityUtil() {
    }

    public static byte[] EncryptAes256(String input, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (input != null && key != null && input.length() >= 1 && key.length() >= 1) {
            AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(1, k, iv);
            byte[] inBytes = input.getBytes("UTF-8");
            byte[] encBytes = c.doFinal(inBytes);
            return encBytes;
        } else {
            return null;
        }
    }

    public static String DecryptAes256(byte[] input, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (input != null && key != null && input.length >= 1 && key.length() >= 1) {
            AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(2, k, iv);
            String decString = new String(c.doFinal(input), "UTF-8");
            return decString;
        } else {
            return null;
        }
    }

    public static String EncryptAesBase64(String input, String key, boolean urlencode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (input != null && key != null && input.length() >= 1 && key.length() >= 1) {
            AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(1, k, iv);
            byte[] inBytes = input.getBytes("UTF-8");
            byte[] encBytes = c.doFinal(inBytes);
            String b64EncString = Base64.encodeBase64String(encBytes);
            return urlencode ? URLEncoder.encode(b64EncString, "UTF-8") : b64EncString;
        } else {
            return null;
        }
    }

    public static String DecryptAesBase64(String input, String key, boolean urldecode) throws Exception {
        String decString = "";
        boolean decryptFail = false;
        Throwable err = null;
        input = input.replaceAll(" ", "+");
        input = input.replaceAll("_", "/");
        input = input.replaceAll("-", "+");

        try {
            decString = DecryptAesBase64_2(input, key, false);
        } catch (Exception var7) {
            err = var7;
            decryptFail = true;
        }

        if (decryptFail) {
            try {
                decString = DecryptAesBase64_2(input, key, true);
            } catch (Exception var8) {
                if (urldecode) {
                    var8.printStackTrace();
                } else {
                    err.printStackTrace();
                }

                throw var8;
            }
        }

        return decString;
    }

    public static String DecryptAesBase64_2(String input, String key, boolean urldecode) throws Exception {
        String decString = "";

        try {
            if (input != null && key != null && input.length() >= 1 && key.length() >= 1) {
                AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
                SecretKeySpec k = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(2, k, iv);
                String b64EncString = urldecode ? URLDecoder.decode(input, "UTF-8") : input;
                byte[] encBytes = Base64.decodeBase64(b64EncString);
                decString = new String(c.doFinal(encBytes), "UTF-8");
                return decString;
            } else {
                return null;
            }
        } catch (Exception var9) {
            throw var9;
        }
    }

    public static String getHmacSha256(String input, String key, boolean urlencode) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        if (input != null && key != null && input.length() >= 1 && key.length() >= 1) {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] inBytes = input.getBytes("UTF-8");
            byte[] encBytes = mac.doFinal(inBytes);
            String b64EncString = Base64.encodeBase64String(encBytes);
            return urlencode ? URLEncoder.encode(b64EncString, "UTF-8") : b64EncString;
        } else {
            return null;
        }
    }

    public static boolean VerifyMac(String skey, String data, String hmac, String vm, boolean urldecode) throws Exception {
        boolean isSuccess = true;
        Throwable err = null;
        data = data.replaceAll(" ", "+");
        data = data.replaceAll("_", "/");
        data = data.replaceAll("-", "+");
        hmac = hmac.replaceAll(" ", "+");
        hmac = hmac.replaceAll("_", "/");
        hmac = hmac.replaceAll("-", "+");

        try {
            isSuccess = VerifyMac2(skey, data, hmac, vm, false);
        } catch (Exception var8) {
            isSuccess = false;
            err = var8;
        }

        if (!isSuccess) {
            try {
                isSuccess = VerifyMac2(skey, data, hmac, vm, true);
            } catch (Exception var9) {
                isSuccess = false;
                if (urldecode) {
                    var9.printStackTrace();
                } else {
                    err.printStackTrace();
                }

                throw var9;
            }
        }

        return isSuccess;
    }

    public static boolean VerifyMac2(String skey, String data, String hmac, String vm, boolean urldecode) throws Exception {
        String decryptedData = DecryptAesBase64(data, skey, urldecode);
        String checkHmac = getHmacSha256(decryptedData.substring(14, decryptedData.length()), skey, urldecode);
        checkHmac = URLDecoder.decode(checkHmac, "UTF-8");
        hmac = URLDecoder.decode(hmac, "UTF-8");
        return hmac.equals(checkHmac);
    }

    public static String null2void(Object objValue, String strDefValue) {
        String strReturn = "";
        if (objValue == null) {
            strReturn = strDefValue;
        } else {
            strReturn = objValue.toString();
        }

        return strReturn;
    }

    public static String null2void(Object objValue) {
        String strReturn = "";
        if (objValue == null) {
            strReturn = "";
        } else {
            strReturn = objValue.toString();
        }

        return strReturn;
    }

    public static String connect(String url) {
        HttpURLConnection con = null;
        BufferedWriter bwriter = null;
        DataInputStream in = null;
        ByteArrayOutputStream bout = null;

        try {
            URL req = new URL(url);
            con = (HttpURLConnection)req.openConnection();
            con.setConnectTimeout(120000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
            bwriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bwriter.flush();
            in = new DataInputStream(con.getInputStream());
            bout = new ByteArrayOutputStream();

            while(true) {
                byte[] buf = new byte[2048];
                int n = in.read(buf);
                if (n == -1) {
                    bout.flush();
                    break;
                }

                bout.write(buf, 0, n);
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            try {
                if (bwriter != null) {
                    bwriter.close();
                }

                if (in != null) {
                    in.close();
                }

                if (bout != null) {
                    bout.close();
                }

                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception var15) {
            }

        }

        return new String(bout.toByteArray());
    }
}