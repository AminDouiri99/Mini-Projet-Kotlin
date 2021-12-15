package com.example.talentium

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_profil_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.videoPlyaer

class ProfilVideoPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_video_player)
        val intent: Intent = getIntent()
        val uri: Uri = Uri.parse(intent.extras?.getString("video"))



        videoPostPlyaer.setVideoURI(uri)
        videoPostPlyaer.requestFocus()
        videoPostPlyaer.start()
        videoPostPlyaer.setOnCompletionListener {
            replay_btn.visibility= View.VISIBLE
        }

        videoCaption.setText(intent.extras?.getString("description"))
        back_btn.setOnClickListener {
            onBackPressed()
        }

        replay_btn.setOnClickListener {
            Log.i("btnreplay","replay")
            videoPostPlyaer.start()
            replay_btn.visibility= View.INVISIBLE
        }


    }

}