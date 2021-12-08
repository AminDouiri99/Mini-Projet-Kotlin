package com.example.talentium

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        Log.i("start activity","video player")
         val intent  :Intent = getIntent()
        val uri : Uri = Uri.parse( intent.extras?.getString("video"))
        Log.i("Uri",uri.toString())
        videoPlyaer.setVideoURI(uri)
        videoPlyaer.requestFocus()
        videoPlyaer.start()

        nextBtn.setOnClickListener {
            val intent = Intent(this, UploadVideo::class.java)
            intent.putExtra("video",uri.toString())
            startActivity(intent)
            finish()
        }
    }
}