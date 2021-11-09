package com.example.talentium.API

import android.util.Log
import com.example.talentium.Model.User
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("api/users/login")
    fun seConnecter(@Query("log") login: String, @Query("pwd") password: String): Call<User>

    @POST("api/users/register")
    fun Register(@Body map:HashMap<String,String>):Call<JsonObject>






    companion object {

        var BASE_URL = "http://192.168.1.20:6000/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            Log.i("http","requette")

            return retrofit.create(ApiInterface::class.java)
        }
    }
}