package com.example.talentium

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.example.talentium.API.ApiInterface
import kotlinx.android.synthetic.main.activity_profil_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.videoPlyaer
import java.lang.String
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class ProfilVideoPlayer : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = this.getSharedPreferences("pref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_video_player)
        val intent: Intent = intent
        val uri: Uri = Uri.parse(intent.extras?.getString("video"))

        val videoId = intent.extras?.getString("_id")
        val videoDescription = intent.extras?.getString("description")
        val videoVisibility = intent.extras?.getBoolean("visibility")
        val videoType = intent.extras?.getString("type")
        if(videoType=="other"){
         editVideo.visibility=View.INVISIBLE
        }
        val apiInterface = ApiInterface.create()
        videoCaption.text = intent.extras?.getString("description")

        videoPostPlyaer.keepScreenOn = true
        videoPostPlyaer.setVideoURI(uri)
        videoPostPlyaer.requestFocus()

        videoPostPlyaer.setOnPreparedListener {

            progressvideo.visibility = View.GONE
            videoPostPlyaer.start()

            val xScale: Float = videoPostPlyaer.width.toFloat() / it.videoWidth
            val yScale: Float = videoPostPlyaer.height.toFloat() / it.videoHeight
            val scale = Math.min(xScale, yScale)
            val scaledWidth: Float = scale * it.videoWidth
            val scaledHeight: Float = scale * it.videoHeight
            val layoutParams: ViewGroup.LayoutParams = videoPostPlyaer.layoutParams
            layoutParams.width = scaledWidth.toInt()
            layoutParams.height = scaledHeight.toInt()
            videoPostPlyaer.layoutParams = layoutParams
            setDuration()
            timerCounter()
        }

        videoPostPlyaer.setOnCompletionListener {
            replay_btn.visibility = View.VISIBLE
        }


        back_btn.setOnClickListener {
            onBackPressed()
        }

        replay_btn.setOnClickListener {
            Log.i("btnreplay", "replay")
            videoPostPlyaer.start()
            replay_btn.visibility = View.INVISIBLE
        }

        editVideo.setOnClickListener {
            videoCaption.visibility = View.INVISIBLE
            editCaption.setText(videoDescription)
            editCaption.visibility = View.VISIBLE
            edit_Btn.visibility = View.VISIBLE
            visibility.isChecked = videoVisibility!!
            visibility.visibility = View.VISIBLE

        }


        edit_Btn.setOnClickListener {
            editCaption.visibility = View.INVISIBLE
            visibility.visibility = View.INVISIBLE
            //Edit Video and go the landing page
            val apiInterface = ApiInterface.create()
            apiInterface.UpdateVideo(
                ApiInterface.PublicationRequestBody(
                    videoId.toString(),
                    editCaption.text.toString(),
                    visibility.isChecked
                )
            ).enqueue(object:Callback<ApiInterface.PublicationResponse>{
                override fun onResponse(
                    call: Call<ApiInterface.PublicationResponse>,
                    response: Response<ApiInterface.PublicationResponse>
                ) {
                   // TODO("Not yet implemented")

                    if(response.code()==200){
                        Log.i("success",response.body()?.publicationId.toString())

                    }
                }

                override fun onFailure(call: Call<ApiInterface.PublicationResponse>, t: Throwable) {
                   // TODO("Not yet implemented")
                }
            })
            edit_Btn.visibility = View.INVISIBLE
        }


        likesnumber.setText(intent.extras?.getInt("likesSize").toString())
        val Likes = intent.extras?.getStringArray("likes")
        if( preferences.getString("id", "").toString() in Likes!!){
            likeIcon.visibility=View.INVISIBLE
            likeIconRed.visibility=View.VISIBLE
        }

        likeIcon.setOnClickListener {
            it.visibility = View.INVISIBLE
            likeIconRed.visibility = View.VISIBLE

            likeIconRed.animate().scaleX(1.25f).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()
            likeIconRed.animate().scaleY(1.25f).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()

            apiInterface.LikeVideo(ApiInterface.LikeVideoRequestBody(   preferences.getString("id", "").toString(), videoId.toString())).enqueue(object :Callback<ApiInterface.LikeVideoResponseBody>{
                override fun onResponse(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    response: Response<ApiInterface.LikeVideoResponseBody>
                ) {
                    //TODO("Not yet implemented")
                    if(response.code()==201){
                        likesnumber.text=response.body()?.likeNumber.toString()
                    }
                }
                override fun onFailure(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })
        }

        likeIconRed.setOnClickListener {
            it.visibility = View.INVISIBLE
            likeIcon.visibility = View.VISIBLE

            apiInterface.UnLikeVideo(ApiInterface.LikeVideoRequestBody( preferences.getString("id", "").toString(), videoId.toString())).enqueue(object :Callback<ApiInterface.LikeVideoResponseBody>{
                override fun onResponse(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    response: Response<ApiInterface.LikeVideoResponseBody>
                ) {
                    //TODO("Not yet implemented")
                    if(response.code()==201){
                        likesnumber.text=response.body()?.likeNumber.toString()
                    }
                }

                override fun onFailure(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private var timer: Timer? = null
    private fun timerCounter() {
        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    //add stop timer
                     updateUI()
                }
            }
        }
        timer!!.schedule(task, 0, 1000)
    }

    private var duration = 0

    private fun setDuration() {
        duration = videoPostPlyaer.duration
    }

    @SuppressLint("DefaultLocale")
    private fun updateUI() {
        val current: Int = videoPostPlyaer.currentPosition

        if (current == duration) {
            timer?.cancel()
        } else {
            var time = String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(current.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(current.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(current.toLong()))
            )
            videoLegnth.text = time
        }

    }

}