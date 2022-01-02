package com.example.talentium

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_profil_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.videoPlyaer
import java.lang.String
import java.util.*
import java.util.concurrent.TimeUnit

class ProfilVideoPlayer : AppCompatActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_video_player)
        val intent: Intent = intent
        val uri: Uri = Uri.parse(intent.extras?.getString("video"))

        videoPostPlyaer.keepScreenOn = true
        videoPostPlyaer.setVideoURI(uri)
        videoPostPlyaer.requestFocus()

        videoPostPlyaer.setOnPreparedListener {
            videoPostPlyaer.start()
            setDuration()
            timerCounter()
        }
        

        videoPostPlyaer.setOnCompletionListener {
            replay_btn.visibility = View.VISIBLE
        }

        videoCaption.text = intent.extras?.getString("description")
        back_btn.setOnClickListener {
            onBackPressed()
        }

        replay_btn.setOnClickListener {
            Log.i("btnreplay", "replay")
            videoPostPlyaer.start()
            replay_btn.visibility = View.INVISIBLE
        }


    }

    private var timer: Timer? = null
    private fun timerCounter() {
        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    Log.i("updateui", "update ui")

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