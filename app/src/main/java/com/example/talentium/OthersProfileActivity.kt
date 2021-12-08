package com.example.talentium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface
import kotlinx.android.synthetic.main.activity_others_profile.*
import kotlinx.android.synthetic.main.activity_others_profile.textView10
import kotlinx.android.synthetic.main.activity_others_profile.textView7
import kotlinx.android.synthetic.main.activity_others_profile.textView9
import kotlinx.android.synthetic.main.activity_others_profile.username
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others_profile)
        val id = intent.getStringExtra("id")
        val idConnected = intent.getStringExtra("idConnected")
        val apiInterface = ApiInterface.create()
        if (id == idConnected) {
            FollowImageButton.visibility = View.INVISIBLE
            MessageBtn.visibility = View.INVISIBLE
            unFollowImageButton.visibility = View.INVISIBLE
            reportPerson.visibility = View.INVISIBLE
        }
        unFollowImageButton.setOnClickListener {
            apiInterface.unFollow(ApiInterface.FollowBody(idConnected!!, id!!)).enqueue(object :
                Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
            unFollowImageButton.visibility = View.GONE
            FollowImageButton.visibility = View.VISIBLE
        }
        backWrapper.setOnClickListener {
            onBackPressed()hfnn
        }
        FollowImageButton.setOnClickListener {
            apiInterface.Follow(ApiInterface.FollowBody(idConnected!!, id!!)).enqueue(object :
                Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("followFaile","true")
                }

            })
            FollowImageButton.visibility = View.GONE
            unFollowImageButton.visibility = View.VISIBLE
        }
        apiInterface.getUser(ApiInterface.GetUserBody(id!!)).enqueue(
            object : Callback<ApiInterface.GetUserResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.GetUserResponse>,
                    response: Response<ApiInterface.GetUserResponse>
                ){
                    username.text = "@" + response.body()?.user?.username.toString()
                    textView7.text = "@" + response.body()?.user?.username.toString()
                    textView9.text = response.body()?.user?.following?.size.toString()
                    textView10.text = response.body()?.user?.followers?.size.toString()
                  //  if(response.body()?.user?)
                   /* Glide.with(OthersProfileActivity())
                        .load(ApiInterface.BASE_URL + response.body()?.user?.avatar).fitCenter()
                        .into(imageprofileothers)*/
                }

                override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                }

            })
        apiInterface.getUser(ApiInterface.GetUserBody(idConnected!!)).enqueue(
            object : Callback<ApiInterface.GetUserResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.GetUserResponse>,
                    response: Response<ApiInterface.GetUserResponse>
                ){
                  //  username.text = "@" + response.body()?.user?.username.toString()
                    val userFollowers = response.body()?.user?.following!!
                    if(id in userFollowers){
                        FollowImageButton.visibility = View.GONE
                        unFollowImageButton.visibility = View.VISIBLE
                    }                }

                override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                }

            })
        pulltorefreshOther.setOnRefreshListener {
            val apiInterface = ApiInterface.create()
            apiInterface.getUser(ApiInterface.GetUserBody(id!!)).enqueue(
                object : Callback<ApiInterface.GetUserResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.GetUserResponse>,
                        response: Response<ApiInterface.GetUserResponse>
                    ) {

                        textView9.text = response.body()?.user?.following?.size.toString()
                        textView10.text = response.body()?.user?.followers?.size.toString()
                        pulltorefreshOther.isRefreshing = false

                        Log.i("initprofile", "aaaasucces")
                    }

                    override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                        Log.i("initprofile", "aaaasucces")

                    }

                }
            )
        }

    }

}