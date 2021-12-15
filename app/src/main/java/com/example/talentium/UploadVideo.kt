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
import kotlinx.android.synthetic.main.activity_upload_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadVideo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_video)
        Log.i("start activity", "video upload")
        val intent: Intent = getIntent()
        val myvideoUri: Uri = Uri.parse(intent.extras?.getString("video"))
        val preferences: SharedPreferences =
            this.getSharedPreferences("pref", Context.MODE_PRIVATE)

        val id = preferences.getString("id", "")

        uploadBtn.setOnClickListener {
            val caption = captionText.text.toString()
            val visiblity = visibility.isChecked
            createPublication(this ,id.toString(), caption, visiblity, myvideoUri)
        }
        cancel_btn.setOnClickListener {
            // delete the video and return to main camera
            val intent = Intent(this, LandingActivity::class.java)

            startActivity(intent)

        }
    }

    fun createPublication(activity : Activity ,id: String, caption: String,visiblity:Boolean,uri:Uri) {
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