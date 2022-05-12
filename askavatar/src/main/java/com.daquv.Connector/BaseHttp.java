package com.daquv.Connector;

import retrofit2.http.GET;
import org.json.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.io.IOException;
import java.util.Map;

public interface BaseHttp {
	
	/**
     * Address 주소조회 OPEN API
     * @param confmKey
     * @param currentPage
     * @param countPerPage
     * @param keyword
     * @param resultType
     * @return
     * @throws IOException
     */
    @GET("/addrlink/addrEngApi.do")
    Call<Map> getAddress(@Query("confmKey") String confmKey,
                         @Query("currentPage") String currentPage,
                         @Query("countPerPage") String countPerPage,
                         @Query("keyword") String keyword,
                         @Query("resultType") String resultType
    ) throws IOException;

    /**
     * Msg 이메일 발송 API
     * @param headerMap
     * @param emailForm
     * @return
     * @throws IOException
     */
//    @POST("/api/utransfer/mail")
//    Call<Map> setEmail(@HeaderMap Map<String, Object> headerMap, @Body EmailForm emailForm) throws IOException;

    /**
     * Msg SMS 발송 API
     * @param headerMap
     * @param mobileForm
     * @return
     * @throws IOException
     */
//    @POST("/api/utransfer/sms")
//    Call<Map> setSms(@HeaderMap Map<String, Object> headerMap, @Body MobileForm mobileForm) throws IOException;

}
