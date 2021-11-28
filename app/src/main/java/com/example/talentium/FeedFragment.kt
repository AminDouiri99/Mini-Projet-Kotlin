package com.example.talentium

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talentium.Adapter.FeedAdapter
import kotlinx.android.synthetic.main.fragment_feed.*
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearSnapHelper

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_main.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


data class Post(val name: String, val role: String) {

}

class FeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedrecyclerview.layoutManager = LinearLayoutManager(view.context);
        val data = ArrayList<Post>()
        for (i in 1..10) {
            data.add(Post("artical " + i, "role " + i))
        }
        val adapter = FeedAdapter(data)
        feedrecyclerview.adapter = adapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(feedrecyclerview)

        // recyclerview.smoothScrollToPosition(adapter.getItemCount())
        /* feedrecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
             override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 super.onScrolled(recyclerView, dx, dy)
                 if (dy > 0) {
                     // Scrolling up
                     Log.i("RecyclerView scrolled: ", "scroll up!");

                    //recyclerView.smoothScrollToPosition(position)
                 } else {
                     // Scrolling down

                     Log.i("RecyclerView scrolled: ", "scroll down!");

                     //recyclerView.smoothScrollToPosition(position)
                 }
             }
             var position =0



            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val linearLayoutManager =recyclerView.linearLayout;

                 Log.i("int",newState.toString())
                 super.onScrollStateChanged(recyclerView, newState)

                 if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                     // Do something
                     recyclerView.smoothScrollToPosition(linearLayoutManager.)
                 } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                     // Do something

                 } else {
                     // Do something

                 }
             }


         })*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}