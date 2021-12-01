package com.example.talentium

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.User
import kotlinx.android.synthetic.main.activity_discover.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverActivity : AppCompatActivity() {
    lateinit var recylcerUsers: RecyclerView
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        var userList: MutableList<User> = ArrayList()
        recylcerUsers = usersRecyclerView
        goBack()
        val apiInterface = ApiInterface.create()
        simpleSearchView.setBackgroundResource(R.drawable.btndark)
        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                apiInterface.search(ApiInterface.SearchBody(newText)).enqueue(
                    object : Callback<ApiInterface.SearchResponse> {
                        override fun onResponse(
                            call: Call<ApiInterface.SearchResponse>,
                            response: Response<ApiInterface.SearchResponse>
                        ) {
                            userList.clear()

                            val preferences: SharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)

                            val userConnected =preferences.getString("id", "").toString()

                            if(userList.size==0){
                                noresulttext.visibility = View.VISIBLE

                            }
                            Log.i("recyler","aaaaaaaaaaaaaaaaa"+response.code()+response.body().toString())
                            if (response.code() == 200) {
                                recylcerUsers.visibility = View.VISIBLE

                                 response.body()!!.users.forEach {

                                    userList.add(it)
                                }

                                adapter = UserAdapter(userList,userConnected)
                              //  Log.i("img", ApiInterface.BASE_URL + preferences.getString("avatar", ""))
                                recylcerUsers.adapter = adapter
                                recylcerUsers.setLayoutManager( LinearLayoutManager(DiscoverActivity()))
                                noresulttext.visibility = View.GONE
                            } else {
                                recylcerUsers.visibility = View.GONE
                                noresulttext.visibility = View.VISIBLE
                                userList.clear()

                            }
                        }

                        override fun onFailure(
                            call: Call<ApiInterface.SearchResponse>,
                            t: Throwable
                        ) {
                            recylcerUsers.visibility = View.GONE
                            noresulttext.visibility = View.VISIBLE
                            userList.clear()

                        }

                    }
                )
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })
}
    fun goBack(){
        backButtonWraperDiscover.setOnClickListener {
            onBackPressed()
        }
    }
}