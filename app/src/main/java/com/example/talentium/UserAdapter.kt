package com.example.talentium

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAdapter(
    private val mList: MutableList<User>,
    private val idConncted: String,
) :
    RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_in_search, parent, false)
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val apiInterface = ApiInterface.create()

        val image = mList[position].avatar
        val username = mList[position].username
        val id = mList[position]._id
        lateinit var userFollowers : Array<String>
        holder.UserName.text = username
        Glide.with(holder.itemView)
            .load(ApiInterface.BASE_URL + image).fitCenter()
            .into(holder.ProfilePic)
        if (id == idConncted) {
            holder.follow.visibility = View.GONE
            holder.unfollow.visibility = View.GONE

        }
        //val apiInterface1 = ApiInterface.create()
        apiInterface.getUser(ApiInterface.GetUserBody(idConncted)).enqueue(
            object : Callback<ApiInterface.GetUserResponse>{
                override fun onResponse(
                    call: Call<ApiInterface.GetUserResponse>,
                    response: Response<ApiInterface.GetUserResponse>
                ){

                    userFollowers = response.body()?.user?.following!!
                    if(id in userFollowers){
                        holder.follow.visibility = View.GONE
                        holder.unfollow.visibility = View.VISIBLE
                    }

                }

                override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                }

            })

            holder.follow.setOnClickListener {
                Log.i("folloew", idConncted + mList[position]._id)
                holder.follow.isClickable=false
                apiInterface.Follow(ApiInterface.FollowBody(idConncted, id)).enqueue(object :
                    Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.i("followFaile","true")
                        holder.follow.isClickable=true
                    }

                })

                holder.follow.isClickable=true
                holder.follow.visibility = View.GONE
                holder.unfollow.visibility = View.VISIBLE
            }

        holder.unfollow.setOnClickListener {
            holder.unfollow.isClickable=false
            apiInterface.unFollow(ApiInterface.FollowBody(idConncted, id)).enqueue(object :
                Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    holder.unfollow.isClickable=true

                }
            })
            holder.follow.visibility = View.VISIBLE
            holder.unfollow.visibility = View.GONE
            holder.unfollow.isClickable=true
        }

    }

    override fun getItemCount() = mList.size
}