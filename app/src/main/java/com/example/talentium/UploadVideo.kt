package com.example.talentium

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.talentium.API.ApiInterface
import com.mvp.handyopinion.UploadUtility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload_video.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadVideo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_video)
        val preferences: SharedPreferences =
            this.getSharedPreferences("pref", Context.MODE_PRIVATE)

        val avatar = preferences.getString("avatar", "")
        Picasso.get()
            .load(ApiInterface.BASE_URL + avatar)
            .into(profil_pic)


        val intent: Intent = getIntent()
        val myvideoUri: Uri = Uri.parse(intent.extras?.getString("video"))


        val id = preferences.getString("id", "")

        uploadBtn.setOnClickListener {
            val caption = captionText.text.toString()
            val visiblity = visibility.isChecked
            createPublication(this ,id.toString(), caption, visiblity, myvideoUri)
            uploadBtn.isEnabled=false
        }
        cancel_btn.setOnClickListener {
            // delete the video and return to main camera
            val intent = Intent(this, LandingActivity::class.java)

            startActivity(intent)

        }
    }

    fun updateUi(){
        Log.i("test ui ","ui")
    }


    fun createPublication(activity : Activity ,id: String, caption: String,visiblity:Boolean,uri:Uri) {

        progress_bar.progress=0
        val apiInterface = ApiInterface.create()
        apiInterface.CreatePublicatin(
            ApiInterface.PublicationRequestBody(
                id,
                caption,
                visiblity
            )
        )
            .enqueue(object : Callback<ApiInterface.PublicationResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.PublicationResponse>,
                    response: Response<ApiInterface.PublicationResponse>
                ) {
                    if (response.code() == 200) {
                        UploadUtility(
                            activity,
                            id,
                            response.body()?.publicationId.toString()
                        ).uploadFile(uri, "video")
                        progress_bar.progress=100
                        val intent = Intent(applicationContext, LandingActivity::class.java)

                        startActivity(intent)
                         activity.finish()
                    }
                }

                override fun onFailure(
                    call: Call<ApiInterface.PublicationResponse>,
                    t: Throwable
                ) {
                    Log.i("fail", t.message.toString())
                }


            })
    }

}