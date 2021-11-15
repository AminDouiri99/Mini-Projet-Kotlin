package com.example.talentium.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.Post
import com.example.talentium.R
import kotlinx.android.synthetic.main.feed_item.view.*
import org.w3c.dom.Text

class FeedAdapter(private val ProductList :List<Post>):RecyclerView.Adapter<FeedAdapter.ViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {

        val ItemsViewModel=ProductList[position]

        holder.text.setText(ItemsViewModel.name)
    }
    override fun getItemCount(): Int {
        return ProductList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text: TextView = itemView.findViewById(R.id.textView6)


    }
}