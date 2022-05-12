package com.daquv.Utils;


import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

@Component
public class NaverTts {

    String clientId = "0mdochr4xn";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "q5wGye9fMBWReJJA5tZOdGHj4MfrJir2NmYIg34p";//애플리케이션 클라이언트 시크릿값";
    String sttText="";

    public NaverTts(){
    }

    public String naverTTS(String param) throws Exception
    {
        this.sttText = param;
        String text = URLEncoder.encode(sttText, "UTF-8");
        System.out.println("text = " + text);
        StringBuffer response = new StringBuffer();
        {
            try{
                String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                // post request
                String postParams = "speaker=nara&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    InputStream is = con.getInputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    // 랜덤한 이름으로 mp3 파일 생성
                    String tempname = Long.valueOf(new Date().getTime()).toString();
                    File f = new File("C:\\Users\\easts\\Downloads\\"+tempname + ".mp3");
                    f.createNewFile();
                    OutputStream outputStream = new FileOutputStream(f);
                    while ((read =is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                    is.close();
                } else {  // 오류 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;

                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return response.toString();

    }





}
