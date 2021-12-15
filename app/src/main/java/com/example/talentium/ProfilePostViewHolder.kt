package com.example.talentium

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfilePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ProfilePostPic: ImageView
    //val ProfilePostDesc: TextView
    val PostVisibility: ImageView

    init {
        ProfilePostPic = itemView.findViewById<ImageView>(R.id.PostImage)
        PostVisibility=itemView.findViewById(R.id.postVisibility)
        //ProfilePostDesc = itemView.findViewById<TextView>(R.id.PostDescription)
    }
}