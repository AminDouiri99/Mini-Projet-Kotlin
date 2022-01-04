package com.example.talentium

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.Model.ProfilePost
import android.media.MediaMetadataRetriever

import android.os.Build

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.talentium.API.ApiInterface
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class ProfilePostAdapter(private val mList: ArrayList<ProfilePost>, private val type: String) :
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
        var source: Bitmap? = null

        holder.ProfilePostPic.loadSkeleton()


           // source = retriveVideoFrameFromVideo(ApiInterface.BASE_URL + mList[position].source)


        //holder.ProfilePostPic.hideSkeleton()
        holder.ProfilePostPic.setImageBitmap(source)


        val visibility = mList[position].display

        if (!visibility) {
            holder.PostVisibility.visibility = View.VISIBLE
        } else {
            holder.PostVisibility.visibility = View.INVISIBLE
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProfilVideoPlayer::class.java)
            intent.putExtra("video", ApiInterface.BASE_URL + mList[position].source)
            intent.putExtra("description", mList[position].description)
            intent.putExtra("visibility", mList[position].display)
            intent.putExtra("likesSize", mList[position].likes.size)
            intent.putExtra("likes", mList[position].likes)
            intent.putExtra("_id", mList[position]._id)
            intent.putExtra("type", type)

            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount() = mList.size
}