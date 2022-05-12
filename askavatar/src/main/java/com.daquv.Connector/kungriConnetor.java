package com.daquv.Connector;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.daquv.Common.CustomLoggerInterceptor;
import com.daquv.Dto.ResponseResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class kungriConnetor extends BaseConnect {
	
	   protected final BaseHttp baseHttp;
	    protected final String confirmKey;
	    protected final String resultType = "json";

	    public kungriConnetor(String host, String confirmKey) {
	        this.confirmKey = confirmKey;
	        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS);

	        CustomLoggerInterceptor logging = new CustomLoggerInterceptor();
	        builder.addInterceptor(logging);

	        OkHttpClient client = builder.build();
	        Retrofit retrofit = new Retrofit.Builder().baseUrl(host)
	                .addConverterFactory(buildJacksonConverter()).client(client).build();
	        this.baseHttp = retrofit.create(BaseHttp.class);
	    }
	    
	    /**
	     * 주소조회
	     * @param currentPage
	     * @param countPerPage
	     * @param keyword
	     * @return
	     * @throws IOException
	     */
	    public ResponseResult getAddress(String currentPage, String countPerPage,String keyword) throws IOException {
	        Call<Map> call = baseHttp.getAddress(this.confirmKey, currentPage, countPerPage, keyword, this.resultType);
	        Response<Map> response = call.execute();
	        return CommonUtils.returnResponse(response);
	    }

}
