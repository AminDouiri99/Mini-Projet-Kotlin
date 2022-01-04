package com.example.talentium

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.talentium.Adapter.FeedAdapter
import kotlinx.android.synthetic.main.fragment_feed.*
import android.widget.AbsListView
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.ViewPager2

import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.ProfilePost
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

data class Post(val name: String, val role: String, val src: String) {

}

class FeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var preferences: SharedPreferences
    lateinit var feedView: View

    lateinit var feedrecyclerviewFeed: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = preferences.getString("id", "")
        val apiInterface = ApiInterface.create()

        apiInterface.GetFollowingVideos(ApiInterface.PublicationRequestBodyGet(id.toString()))
            .enqueue(
                object : Callback<ApiInterface.VideoResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.VideoResponse>,
                        response: Response<ApiInterface.VideoResponse>
                    ) {
                        if (response.code() == 200) {
                            val list: ArrayList<ProfilePost>? = response.body()?.publications
                            if (list == null) {
                                if (list != null) {
                                    Log.i("listvideo", list.size.toString())
                                }
                            } else {
                                Log.i("listvideo", list!!.size.toString())
                                val adapter = FeedAdapter(list, id.toString())
                                if(list!!.size==0){
                                    noContentFeed.visibility=View.VISIBLE
                                }else{
                                    noContentFeed.visibility=View.GONE
                                    feedrecyclerview.visibility=View.VISIBLE
                                    feedrecyclerviewFeed.adapter = adapter
                                }

                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ApiInterface.VideoResponse>,
                        t: Throwable
                    ) {
                        //  TODO("Not yet implemented")
                    }

                }
            )


        //   }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        feedView = inflater.inflate(R.layout.fragment_feed, container, false)
        feedrecyclerviewFeed = feedView.findViewById(R.id.feedrecyclerview)
        return feedView
    }


}