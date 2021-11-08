package com.example.talentium.API

import com.example.talentium.Model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("login")
    fun seConnecter(@Query("log") login: String, @Query("pwd") password: String): Call<User>

    companion object {

        var BASE_URL = "http://192.168.1.24:6000/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}