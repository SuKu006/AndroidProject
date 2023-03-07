package com.basicapp.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("insert.php")
    Call<Response> registerUserApi(@Query("FullName") String name,
                                   @Query("UserName") String userName,
                                   @Query("Password") String password,
                                   @Query("ConfirmPassword") String confirmPassword,
                                   @Query("Email") String email);

    @POST("search.php")
    Call<SearchResponse> callSearchApi(@Query("Email") String email);

}