package com.example.talentium

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ProfilePic: ImageView
    val UserName: TextView
    val follow: AppCompatImageButton
    val unfollow: AppCompatImageButton

    init {
        ProfilePic = itemView.findViewById<ImageView>(R.id.imageprofileInsearch)
        UserName = itemView.findViewById<TextView>(R.id.UserNameInsearch)
        follow = itemView.findViewById(R.id.btnFollowInseatch)
        unfollow = itemView.findViewById(R.id.btnUnFollowInseatch)

    }
}