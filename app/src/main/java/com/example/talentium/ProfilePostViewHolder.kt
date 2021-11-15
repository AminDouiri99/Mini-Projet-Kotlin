package com.example.talentium

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfilePostViewHolder(itemView : View): RecyclerView.ViewHolder(itemView  ) {
    val ProfilePostPic : ImageView
    val ProfilePostDesc : TextView
    init {
        ProfilePostPic = itemView.findViewById<ImageView>(R.id.PostImage)
        ProfilePostDesc = itemView.findViewById<TextView>(R.id.PostDescription)
    }
}