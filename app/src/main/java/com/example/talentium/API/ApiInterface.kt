package com.example.talentium.API

import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.talentium.Model.ProfilePost
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
    data class GetUserResponse(
        @SerializedName("user")
        val user: User
    )

    data class SearchResponse(
        @SerializedName("users")
        val users: MutableList<User>
    )

    data class updateResponse(@SerializedName("user") val user: User)
    data class SearchBody(val text: String)
    data class GetUserBody(val id: String)

    data class FollowBody(val idConnected:String,val idToFollow:String)
    data class LoginBody(val email: String, val password: String)
    data class registerBody(val email: String, val password: String, val username: String)
    data class updateUserBody(val id: String, val username: String)

    data class PublicationRequestBody(val userid :String , val caption :String,val visibility:Boolean)
    data class LikeVideoRequestBody(val userId :String , val videoId :String)
    data class LikeVideoResponseBody(val likeNumber :Int)
    data class PublicationRequestBodyGet(val userid :String )
    data class PublicationResponse(
        @SerializedName("publicationId")
        val publicationId:String)

    data class VideoResponse(
        val publications:ArrayList<ProfilePost>
    )
    data class getGollowingResponse(
        val following:ArrayList<String>
    )

    @POST("api/users/search")
    fun search(@Body text: SearchBody): Call<SearchResponse>

    @POST("api/users/login")
    fun seConnecter(@Body credential: LoginBody): Call<LoginResponse>

    @POST("api/users/updateuser")
    fun updateuser(@Body body: updateUserBody): Call<updateResponse>

    @POST("api/users/register")
    fun Register(@Body registerBody: registerBody): Call<LoginResponse>

    @POST("api/users/follow")
    fun Follow(@Body followBody: FollowBody): Call<String>
    @POST("api/users/unfollow")
    fun unFollow(@Body followBody: FollowBody): Call<String>
    @POST("api/users/getuser")
    fun getUser(@Body getUserBody: GetUserBody): Call<GetUserResponse>
    // Facebook login
    @GET("api/auth/facebook/secrets")
    fun FacebookLogin(): Call<LoginResponse>

    @POST("api/publication/create/video")
    fun CreatePublicatin(@Body publication :PublicationRequestBody):Call<PublicationResponse>
    @POST("api/publication/getfeedbyuser/")
    fun GetFollowingVideos(@Body videos :PublicationRequestBodyGet):Call<VideoResponse>

    @POST("api/publication/byuser/",)
     fun GetVideosByUser(@Body videos :PublicationRequestBodyGet):Call<VideoResponse>

    @POST("api/publication/getotheruser/",)
    fun GetOtherUserVideo(@Body videos :PublicationRequestBodyGet):Call<VideoResponse>

    @PUT("api/publication/update/video/")
    fun UpdateVideo(@Body video:PublicationRequestBody):Call<PublicationResponse>

    @PUT("api/publication/like/video/")
    fun LikeVideo(@Body video:LikeVideoRequestBody):Call<LikeVideoResponseBody>

    @PUT("api/publication/unlike/video/")
    fun UnLikeVideo(@Body video:LikeVideoRequestBody):Call<LikeVideoResponseBody>


    companion object {
        var BASE_URL ="https://miniprojet-server.herokuapp.com/"
           // "http://192.168.1.20:6000/"

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            Log.i("http", "requette")

            return retrofit.create(ApiInterface::class.java)
        }
    }
}