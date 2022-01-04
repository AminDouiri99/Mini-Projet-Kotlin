package com.example.talentium

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.ProfilePost
import com.squareup.picasso.Picasso
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.android.synthetic.main.activity_others_profile.*
import kotlinx.android.synthetic.main.activity_others_profile.textView10
import kotlinx.android.synthetic.main.activity_others_profile.textView7
import kotlinx.android.synthetic.main.activity_others_profile.textView9
import kotlinx.android.synthetic.main.activity_others_profile.username
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersProfileActivity : AppCompatActivity() {

    lateinit var id: String
    lateinit var recylcerPost: RecyclerView
    lateinit var adapter: ProfilePostAdapter
    lateinit var apiInterface: ApiInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others_profile)
        id = intent.getStringExtra("id").toString()
        //Add videos for other user in recycle view
        val idConnected = intent.getStringExtra("idConnected")
        apiInterface = ApiInterface.create()
        if (id == idConnected) {
            FollowImageButton.visibility = View.INVISIBLE
            MessageBtn.visibility = View.INVISIBLE
            unFollowImageButton.visibility = View.INVISIBLE
            reportPerson.visibility = View.INVISIBLE
        }

        textView9.loadSkeleton()
        username.loadSkeleton()
        textView7.loadSkeleton()
        textView10.loadSkeleton()
        textView8.loadSkeleton()
        imageprofileothers.loadSkeleton()

        unFollowImageButton.setOnClickListener {
            apiInterface.unFollow(ApiInterface.FollowBody(idConnected!!, id!!)).enqueue(object :
                Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
            unFollowImageButton.visibility = View.GONE
            FollowImageButton.visibility = View.VISIBLE
        }
        backWrapper.setOnClickListener {
            onBackPressed()
        }

        FollowImageButton.setOnClickListener {
            apiInterface.Follow(ApiInterface.FollowBody(idConnected!!, id!!)).enqueue(object :
                Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("followFaile", "true")
                }

            })
            FollowImageButton.visibility = View.GONE
            unFollowImageButton.visibility = View.VISIBLE
        }


            getUserInformation()


        getVideos(this)



        apiInterface.getUser(ApiInterface.GetUserBody(idConnected!!)).enqueue(
            object : Callback<ApiInterface.GetUserResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.GetUserResponse>,
                    response: Response<ApiInterface.GetUserResponse>
                ) {
                    //  username.text = "@" + response.body()?.user?.username.toString()
                    val userFollowers = response.body()?.user?.following!!
                    if (id in userFollowers) {
                        FollowImageButton.visibility = View.GONE
                        unFollowImageButton.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                }

            })

        pulltorefreshOther.setOnRefreshListener {
            val apiInterface = ApiInterface.create()
            apiInterface.getUser(ApiInterface.GetUserBody(id!!)).enqueue(
                object : Callback<ApiInterface.GetUserResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.GetUserResponse>,
                        response: Response<ApiInterface.GetUserResponse>
                    ) {

                        textView9.text = response.body()?.user?.following?.size.toString()

                        textView10.text = response.body()?.user?.followers?.size.toString()
                        pulltorefreshOther.isRefreshing = false

                        Log.i("initprofile", "aaaasucces")
                    }

                    override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                        Log.i("initprofile", "aaaasucces")

                    }

                }
            )
        }



        recylcerPost = recyclerOtherProfilview


    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }

     fun getUserInformation() {

             apiInterface.getUser(ApiInterface.GetUserBody(id!!)).enqueue(
                 object : Callback<ApiInterface.GetUserResponse> {
                     override fun onResponse(
                         call: Call<ApiInterface.GetUserResponse>,
                         response: Response<ApiInterface.GetUserResponse>
                     ) {
                         textView9.hideSkeleton()
                         username.hideSkeleton()
                         textView7.hideSkeleton()
                         textView10.hideSkeleton()
                         textView8.hideSkeleton()
                         username.text = "@" + response.body()?.user?.username.toString()
                         textView7.text = "@" + response.body()?.user?.username.toString()
                         textView9.text = response.body()?.user?.following?.size.toString()
                         textView10.text = response.body()?.user?.followers?.size.toString()
                         textView8.text=response.body()?.user?.publication?.size.toString()
                         imageprofileothers.hideSkeleton()

                         Picasso.get()
                             .load(ApiInterface.BASE_URL + response.body()?.user?.avatar)
                             .into(imageprofileothers)
                     }

                     override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                     }

                 })


    }


    fun getVideos(context: Context) {


            val apiInterface = ApiInterface.create()
            apiInterface.GetOtherUserVideo(ApiInterface.PublicationRequestBodyGet(id))
                .enqueue(object : Callback<ApiInterface.VideoResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.VideoResponse>,
                        response: Response<ApiInterface.VideoResponse>
                    ) {
                        if (response.code() == 200) {

                            val list: ArrayList<ProfilePost>? = response.body()?.publications
                            val listToDisplay: ArrayList<ProfilePost>? = null
                            list?.forEach {
                                Log.i("post", it.toString())
                                if (it.display)
                                    listToDisplay?.add(it)
                            }
                            Log.i("post", listToDisplay.toString())

                            if (list == null) {
                                noContentProfile.visibility = View.VISIBLE
                                recylcerPost.visibility = View.GONE
                            } else {

                                adapter = ProfilePostAdapter(list, "other")
                                recylcerPost.adapter = adapter
                                recylcerPost.layoutManager = GridLayoutManager(context, 3)

                            }

                        }
                    }

                    override fun onFailure(call: Call<ApiInterface.VideoResponse>, t: Throwable) {
                        Log.i("get videos", "failed")
                    }
                })

    }

}