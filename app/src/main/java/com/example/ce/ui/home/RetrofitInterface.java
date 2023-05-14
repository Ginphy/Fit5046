package com.example.ce.ui.home;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface RetrofitInterface {




    @GET("day")
    Call<Reception> getCall(@Query("date") String myDate,@Query("key") String myKey);
}
