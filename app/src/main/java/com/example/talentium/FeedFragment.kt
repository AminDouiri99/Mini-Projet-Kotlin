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

import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.ProfilePost
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

data class Post(val name: String, val role: String,val src:String) {

}

class FeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // feedrecyclerview.layoutManager = LinearLayoutManager(view.context);
        thread  (){
            val data = ArrayList<ProfilePost>()

            for (i in 1..10) {
                data.add(ProfilePost(1 , "role " + i,"https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4",true))
            }
            val id = preferences.getString("id", "")
            val apiInterface = ApiInterface.create()
            apiInterface.GetFollowingByUser(ApiInterface.PublicationRequestBodyGet(id.toString())).enqueue(
                object : Callback<ApiInterface.getGollowingResponse>{
                    override fun onResponse(
                        call: Call<ApiInterface.getGollowingResponse>,
                        response: Response<ApiInterface.getGollowingResponse>
                    ) {
                        val list: ArrayList<String>? = response.body()?.following
                        list?.forEach {


                            apiInterface.GetVideosByUser(ApiInterface.PublicationRequestBodyGet(it.toString()))
                                .enqueue(object : Callback<ApiInterface.VideoResponse> {
                                    override fun onResponse(
                                        call: Call<ApiInterface.VideoResponse>,
                                        response: Response<ApiInterface.VideoResponse>
                                    ) {
                                        if (response.code() == 200) {

                                            val list: ArrayList<ProfilePost>? = response.body()?.publications
                                            list?.forEach {
                                                data.add(it)
                                            }
                                            Log.i("feeeeed",list.toString())
                                        }
                                    }
                                    override fun onFailure(call: Call<ApiInterface.VideoResponse>, t: Throwable) {
                                        Log.i("get videos", "failed")
                                    }
                                })







                        }
                        Log.i("feeeeeed",list.toString())
                    }

                    override fun onFailure(
                        call: Call<ApiInterface.getGollowingResponse>,
                        t: Throwable
                    ) {
                        TODO("Not yet implemented")
                    }

                }
            )

            val adapter = FeedAdapter(data)
            feedrecyclerview.adapter = adapter











            }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


}