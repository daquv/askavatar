package com.daquv.Connector;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import org.json.*;
import org.apache.commons.codec.binary.Hex;


import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class BaseConnect {
    public HashMap sourcePurposeJob = new HashMap();

    ObjectMapper objectMapper = new ObjectMapper();

    public BaseConnect() {


    }



    public JacksonConverterFactory buildJacksonConverter(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return JacksonConverterFactory.create(objectMapper);
    }

    protected GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson myGson = gsonBuilder.create();
        return GsonConverterFactory.create(myGson);
    }

    public String getBase64Encoder(String plainText) {
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedString = encoder.encodeToString(plainText.getBytes("UTF-8"));
            return encodedString;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getBase64Encoder(byte[] plainText) {
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedString = encoder.encodeToString(plainText);
            return encodedString;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getBase64Decoder(String base64Text) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            String decodedString = new String(decoder.decode(base64Text), "UTF-8");
            return decodedString;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] hashHmac(String plainText, String key, String algorithm, boolean hex) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secret);

        if(hex){
            return mac.doFinal(plainText.getBytes("UTF-8"));
        }
        return new Hex().encode(mac.doFinal(plainText.getBytes("UTF-8")));
    }

    public String getMD5(String str) throws Exception {
        String MD5 = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < byteData.length ; i++){
            sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private String encryptSks(String plainText, String sksKey, String salt){
        try {
            char[] sksKeyChars = sksKey.toCharArray();

            PBEKeySpec spec = new PBEKeySpec( sksKeyChars, salt.getBytes(StandardCharsets.UTF_8), 1024, 256 );
            SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedKey = key.generateSecret(spec).getEncoded();

            SecretKeySpec secretKey = new SecretKeySpec(hashedKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8)));
            return new String(DatatypeConverter.printBase64Binary(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String decryptSks(String encText, String sksKey, String salt){
        try {
            char[] sksKeyChars = sksKey.toCharArray();

            PBEKeySpec spec = new PBEKeySpec( sksKeyChars, salt.getBytes(StandardCharsets.UTF_8), 1024, 256 );
            SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedKey = key.generateSecret(spec).getEncoded();

            SecretKeySpec secretKey = new SecretKeySpec(hashedKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8)));
            return new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(encText)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject objectToMap(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map_params = objectMapper.readValue(objectMapper.writeValueAsString(obj), new TypeReference<Map>(){});
            JSONObject params = objectMapper.readValue(objectMapper.writeValueAsString(map_params), new TypeReference<JSONObject>(){});
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   


}
