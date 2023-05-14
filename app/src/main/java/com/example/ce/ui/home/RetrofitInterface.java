package com.example.ce.ui.home;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface RetrofitInterface {

//    static String url = "http://apis.juhe.cn/fapig/calendar/day"; // 请求链接
//    static String KEY = "a63283c74d3bb2ad9ba9a173b46e9741"; // 请求参数


    @GET("day")
    Call<Reception> getCall(@Query("date") String myDate,@Query("key") String myKey);
}
