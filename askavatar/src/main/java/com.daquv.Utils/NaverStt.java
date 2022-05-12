package com.daquv.Utils;


import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class NaverStt {

    String clientId = "0mdochr4xn";             // Application Client ID";
    String clientSecret = "q5wGye9fMBWReJJA5tZOdGHj4MfrJir2NmYIg34p";     // Application Client Secret";
    String fileName ="C:\\Users\\easts\\Downloads\\aaa.mp3";


    public NaverStt()
    {

    }

    public String naverStt(String filePath) throws Exception {

        this.fileName = filePath;
        File voiceFile = new File("C:\\Users\\easts\\Downloads\\aaa.mp3");

        String language = "Kor";        // 언어 코드 ( Kor, Jpn, Eng, Chn )
        String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
        StringBuffer response = new StringBuffer();
        URL url = new URL(apiURL);

        {
            try{

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/octet-stream");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

                OutputStream outputStream = conn.getOutputStream();
                FileInputStream inputStream = new FileInputStream(voiceFile);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                inputStream.close();
                BufferedReader br = null;
                int responseCode = conn.getResponseCode();
                if(responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {  // 오류 발생
                    System.out.println("error!!!!!!! responseCode= " + responseCode);
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                String inputLine;

                if(br != null) {

                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());
                } else {
                    System.out.println("error !!!");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return response.toString();

    }


}
