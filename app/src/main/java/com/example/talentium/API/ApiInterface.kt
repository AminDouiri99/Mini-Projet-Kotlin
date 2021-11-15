package com.example.talentium.API

import android.util.Log
import com.example.talentium.Model.User
import com.google.gson.JsonObject
import org.json.JSONObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    data class LoginResponse(
        @SerializedName("token")
        val token: String,
        @SerializedName("user")
        val user: User
    )
    data class LoginBody(val email:String,val password:String)
    data class registerBody(val email:String,val password :String,val username:String)
    @POST("api/users/login")
    fun seConnecter(@Body credential : LoginBody): Call<LoginResponse>

    @POST("api/users/register")
    fun Register(@Body registerBody: registerBody):Call<LoginResponse>






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