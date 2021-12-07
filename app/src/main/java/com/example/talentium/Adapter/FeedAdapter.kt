package com.example.talentium.Adapter

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.Post
import com.example.talentium.R

class FeedAdapter(private val ProductList: List<Post>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {

        val ItemsViewModel = ProductList[position]

        holder.text.setText(ItemsViewModel.name)
        holder.video.setVideoURI(Uri.parse(ItemsViewModel.src));
        holder.video.setOnPreparedListener {
            holder.progressbar.visibility=View.GONE
            it.start()
            val xScale: Float = holder.video.width.toFloat() / it.videoWidth
            val yScale: Float = holder.video.height.toFloat() / it.videoHeight
            val scale = Math.min(xScale, yScale)
            val scaledWidth: Float = scale * it.videoWidth
            val scaledHeight: Float = scale * it.videoHeight
            val layoutParams: ViewGroup.LayoutParams = holder.video.layoutParams
            layoutParams.width = scaledWidth.toInt()
            layoutParams.height = scaledHeight.toInt()
            holder.video.layoutParams=layoutParams

        }//the string of the URL mentioned above
        holder.video.setOnCompletionListener {
            it.start()
        }//the string of the URL mentioned above
        holder.video!!.setOnErrorListener(MediaPlayer.OnErrorListener {
                mediaPlayer, i, i2 ->
            holder.progressbar.visibility=View.GONE
            holder.probleem.visibility=View.VISIBLE

            // mediaPlayer is refer to your MediaPlayer instance.
            //You should restart it in here.
            true
            // how to restart the player here?!
        })
    }

    override fun getItemCount(): Int {
        return ProductList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text: TextView = itemView.findViewById(R.id.textView6)
        val video :VideoView = itemView.findViewById(R.id.videoFeed)
        val progressbar : ProgressBar = itemView.findViewById(R.id.progressvideo)
        val probleem : ImageView = itemView.findViewById(R.id.errorVideo)
    }
}