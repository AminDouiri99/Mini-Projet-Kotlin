package com.example.talentium

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.Model.ProfilePost
import android.media.MediaMetadataRetriever

import android.os.Build

import android.graphics.Bitmap
import android.view.View
import com.example.talentium.API.ApiInterface
import java.lang.Exception


class ProfilePostAdapter(private val mList: MutableList<ProfilePost>) :
    RecyclerView.Adapter<ProfilePostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_post, parent, false)

        return ProfilePostViewHolder(view)
    }

    @Throws(Throwable::class)

    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
                videoPath,
                HashMap()
            ) else mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(2, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {

        val desc = mList[position].description
        val source = retriveVideoFrameFromVideo(ApiInterface.BASE_URL +mList[position].source)

        val visibility = mList[position].display

        val image=source
        if(!visibility){
            holder.PostVisibility.visibility= View.VISIBLE
        }else{
            holder.PostVisibility.visibility= View.INVISIBLE
        }



        holder.ProfilePostPic.setImageBitmap(image)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProfilVideoPlayer::class.java)
            intent.putExtra("video",ApiInterface.BASE_URL +mList[position].source)
            intent.putExtra("description",mList[position].description)

            holder.itemView.context.startActivity(intent)

        }



    }

    override fun getItemCount() = mList.size
}