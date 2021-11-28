package com.example.talentium.API

import android.os.Looper
import android.util.Log
import com.example.talentium.Model.User
import com.google.gson.JsonObject
import org.json.JSONObject
import com.google.gson.annotations.SerializedName

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileInputStream
import java.util.logging.Handler

interface ApiInterface {
    data class LoginResponse(
        @SerializedName("token")
        val token: String,
        @SerializedName("user")
        val user: User
    )
    data class updateResponse(@SerializedName("user")val user:User)

    data class LoginBody(val email:String,val password:String)
    data class registerBody(val email:String,val password :String,val username:String)
    data class updateUserBody(val id:String,val username: String)
    @POST("api/users/login")
    fun seConnecter(@Body credential: LoginBody): Call<LoginResponse>
    @POST("api/users/updateuser")
    fun updateuser(@Body body:updateUserBody):Call<updateResponse>

    @POST("api/users/register")
    fun Register(@Body registerBody: registerBody): Call<LoginResponse>


    // Facebook login
    @GET("api/auth/facebook/secrets")
    fun FacebookLogin(): Call<LoginResponse>


    /*@Multipart
    @POST("api/users/changeprofile/pic/")
    fun uploadImage(@Part image: MultipartBody.Part, @Query("id") userId : String):Call<User>*/
    companion object {
        var BASE_URL = "http://172.17.2.104:6000/"

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