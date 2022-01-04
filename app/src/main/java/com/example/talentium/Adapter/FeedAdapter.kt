package com.example.talentium.Adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.ProfilePost
import com.example.talentium.Model.User
import com.example.talentium.OthersProfileActivity
import com.example.talentium.Post
import com.example.talentium.R
import com.squareup.picasso.Picasso
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.android.synthetic.main.activity_manage_account.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedAdapter(private val ProductList: List<ProfilePost>, private val userId: String) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    lateinit var user: User
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {

        val ItemsViewModel = ProductList[position]

        holder.userName.loadSkeleton()
        holder.userAvatar.loadSkeleton()

        val apiInterface = ApiInterface.create()
        apiInterface.getUser(
            ApiInterface.GetUserBody(
                ItemsViewModel.user
            )
        ).enqueue(
            object : Callback<ApiInterface.GetUserResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.GetUserResponse>,
                    response: Response<ApiInterface.GetUserResponse>
                ) {
                    if (response.code() == 200) {
                        user = response.body()?.user!!
                        holder.userName.hideSkeleton()
                        holder.userName.text = user.username



                        holder.userAvatar.hideSkeleton()
                        Picasso.get()
                            .load(ApiInterface.BASE_URL + user.avatar)

                            .into(holder.userAvatar);
                    }
                }

                override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

        if(userId in ItemsViewModel.likes){
            holder.likeIcon.visibility=View.INVISIBLE
            holder.likeIconRed.visibility=View.VISIBLE
        }

        holder.likeIcon.setOnClickListener {
            it.visibility = View.INVISIBLE
            holder.likeIconRed.visibility = View.VISIBLE

            holder.likeIconRed.animate().scaleX(1.25f).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()
            holder.likeIconRed.animate().scaleY(1.25f).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()

            //Like publication
            apiInterface.LikeVideo(ApiInterface.LikeVideoRequestBody(userId, ItemsViewModel._id)).enqueue(object :Callback<ApiInterface.LikeVideoResponseBody>{
                override fun onResponse(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    response: Response<ApiInterface.LikeVideoResponseBody>
                ) {
                    //TODO("Not yet implemented")
                    if(response.code()==201){
                        holder.likesnumber.text=response.body()?.likeNumber.toString()
                    }
                }

                override fun onFailure(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })

        }

        holder.likeIconRed.setOnClickListener {

            it.visibility = View.INVISIBLE
            holder.likeIcon.visibility = View.VISIBLE

            //UnLike publication

            apiInterface.UnLikeVideo(ApiInterface.LikeVideoRequestBody(userId, ItemsViewModel._id)).enqueue(object :Callback<ApiInterface.LikeVideoResponseBody>{
                override fun onResponse(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    response: Response<ApiInterface.LikeVideoResponseBody>
                ) {
                    //TODO("Not yet implemented")
                    if(response.code()==201){
                        holder.likesnumber.text=response.body()?.likeNumber.toString()
                    }
                }

                override fun onFailure(
                    call: Call<ApiInterface.LikeVideoResponseBody>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })

        }

        holder.text.setText(ItemsViewModel.description)
        holder.likesnumber.setText(ItemsViewModel.likes.size.toString())

        holder.video.setVideoURI(Uri.parse(ApiInterface.BASE_URL + ItemsViewModel.source));

        holder.video.setOnPreparedListener {

            holder.progressbar.visibility = View.GONE
            it.start()
            val xScale: Float = holder.video.width.toFloat() / it.videoWidth
            val yScale: Float = holder.video.height.toFloat() / it.videoHeight
            val scale = Math.min(xScale, yScale)
            val scaledWidth: Float = scale * it.videoWidth
            val scaledHeight: Float = scale * it.videoHeight
            val layoutParams: ViewGroup.LayoutParams = holder.video.layoutParams
            layoutParams.width = scaledWidth.toInt()
            layoutParams.height = scaledHeight.toInt()
            holder.video.layoutParams = layoutParams

        }//the string of the URL mentioned above

        holder.userAvatar.setOnClickListener {
            val intent = Intent(holder.itemView.context, OthersProfileActivity::class.java)
            intent.apply {
                putExtra("id", ItemsViewModel.user)
                putExtra("idConnected", userId)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.video.setOnCompletionListener {
            it.start()
        }//the string of the URL mentioned above
        holder.video!!.setOnErrorListener(MediaPlayer.OnErrorListener { mediaPlayer, i, i2 ->
            holder.progressbar.visibility = View.GONE
            holder.probleem.visibility = View.VISIBLE

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
        val userAvatar: ImageView = itemView.findViewById(R.id.userAvatarFeed)
        val text: TextView = itemView.findViewById(R.id.DescriptionPost)
        val userName: TextView = itemView.findViewById(R.id.UserNamePublication)
        val video: VideoView = itemView.findViewById(R.id.videoFeed)
        val progressbar: ProgressBar = itemView.findViewById(R.id.progressvideo)
        val probleem: ImageView = itemView.findViewById(R.id.errorVideo)
        val likesnumber: TextView = itemView.findViewById(R.id.likesnumber)
        val likeIcon: ImageView = itemView.findViewById(R.id.likeIcon)
        val likeIconRed: ImageView = itemView.findViewById(R.id.likeIconRed)

    }
}