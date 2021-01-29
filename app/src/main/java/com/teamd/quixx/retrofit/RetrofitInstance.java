package com.teamd.quixx.retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit=null;
    //emulator
    //private static final String BASE_URL="http://10.0.2.2:8080/api/quixx/v1/";
    //device
    //private static final String BASE_URL="http://10.1.0.251:8080/api/quixx/v1/";

    //server
    //private static final String BASE_URL="http://206.189.128.203:8081/api/quixx/v1/";
    //server 2
    //private static final String BASE_URL="https://prod.quixx.xyz/api/quixx/v1/";
    private static final String BASE_URL="https://api-new.quixx.xyz/api/quixx/v1/";
    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
