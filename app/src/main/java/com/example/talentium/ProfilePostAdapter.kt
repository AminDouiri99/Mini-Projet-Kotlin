package com.example.talentium

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.Model.ProfilePost

class ProfilePostAdapter(private val mList: MutableList<ProfilePost>) :
    RecyclerView.Adapter<ProfilePostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_post, parent, false)

        return ProfilePostViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        val imageTest = R.drawable.dummyimage
        val image = imageTest
        val desc = mList[position].description


        holder.ProfilePostPic.setImageResource(image)
        holder.ProfilePostDesc.text = desc

    }

    override fun getItemCount() = mList.size
}