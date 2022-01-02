package com.example.talentium

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import com.mvp.handyopinion.URIPathHelper
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayer : AppCompatActivity() {
    val REQ_PICK_AUDIO = 10001

    companion object {
        const val GALLERY_RESULT = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        val intent  :Intent = getIntent()
        val uri : Uri = Uri.parse( intent.extras?.getString("video"))
        Log.i("Uri",uri.toString())
        val mediaController = MediaController(this)
        mediaController.setBackgroundColor(resources.getColor(R.color.Mango_Tango))
        mediaController.setAnchorView(videoPlyaer)

        videoPlyaer.setMediaController(mediaController)
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

    fun openSystemGalleryToSelectAVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*")

        try {
            startActivityForResult(intent, GALLERY_RESULT)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "No Gallery APP installed",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == RESULT_OK  ) {
            // Open Editor with some uri in this case with an video selected from the system gallery.
            val intent1  :Intent = getIntent()

            var uril: Uri =Uri.parse(intent1!!.extras?.getString("video"))
            val pathFromUri = URIPathHelper().getPath(this, uril)
            var urilaudio: Uri =intent?.data!!
            val pathFromUriaudio = URIPathHelper().getPath(this, urilaudio)


        } else if (resultCode == RESULT_OK ) {
            // Editor has saved an Video.


        } else if (resultCode == RESULT_CANCELED ) {
            // Editor was canceled
            // TODO: Do something with the source...
        }
    }




}