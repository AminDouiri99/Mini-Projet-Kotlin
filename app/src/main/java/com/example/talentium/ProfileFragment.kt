package com.example.talentium

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talentium.API.ApiInterface
import com.example.talentium.API.ApiInterface.Companion.BASE_URL
import com.example.talentium.Model.ProfilePost
import com.example.talentium.Model.Users
import com.example.talentium.util.AppDataBase
import com.mvp.handyopinion.UploadUtility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ly.img.android.pesdk.utils.runOnMainThread
import okhttp3.MultipartBody
import retrofit2.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.logging.Logger
import kotlin.concurrent.thread
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var profilView: View

    lateinit var recylcerPost: RecyclerView
    lateinit var adapter: ProfilePostAdapter
    var selectedImage: Uri? = null
    val READ_EXTERNEL_CODE = 41

    lateinit var user: MutableList<Users>
    lateinit var noContentResponse: ConstraintLayout

    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        gotoSettings()

        getVideos(requireContext())

        val id = preferences.getString("id", "")
        val apiInterface = ApiInterface.create()
       /* var list:ArrayList<ProfilePost> = ArrayList()
        GlobalScope.launch {
            list=  apiInterface.GetVideosByUser(ApiInterface.PublicationRequestBodyGet(id.toString()))
                .await().publications
        }



        runOnMainThread {
            adapter = ProfilePostAdapter(list, "mine")
            recylcerPost.adapter = adapter
            recylcerPost.layoutManager = GridLayoutManager(context, 3)
        }*/

        recylcerPost = recyclerview


        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        gotToDiscover()
        pulltorefresh.setOnRefreshListener {
            val apiInterface = ApiInterface.create()
            apiInterface.getUser(
                ApiInterface.GetUserBody(
                    preferences.getString("id", "").toString()
                )
            ).enqueue(
                object : Callback<ApiInterface.GetUserResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.GetUserResponse>,
                        response: Response<ApiInterface.GetUserResponse>
                    ) {
                        editor.putString("avatar", response.body()?.user?.avatar.toString()).apply()

                        editor.putString("username", response.body()?.user?.username.toString())
                            .apply()
                        editor.putInt("followersNumber", response.body()!!.user.followers.size)
                            .apply()
                        editor.putInt("followingNumber", response.body()!!.user.following.size)
                            .apply()
                        editor.putInt("publicationsize", response.body()!!.user.publication.size)
                            .apply()
                        val set: MutableSet<String> = HashSet()
                        set.addAll(response.body()!!.user.publication)
                        editor.putStringSet("publication", set)
                            .apply()

                        Log.i("avatar", response.body()?.user?.avatar.toString())
                        username.text = "@" + response.body()?.user?.username.toString()
                        textView7.text = "@" + response.body()?.user?.username.toString()
                        textView9.text = response.body()?.user?.following?.size.toString()
                        textView10.text = response.body()?.user?.followers?.size.toString()
                        PostsNumber.text = response.body()?.user?.publication?.size.toString()
                        pulltorefresh.isRefreshing = false


                    }

                    override fun onFailure(call: Call<ApiInterface.GetUserResponse>, t: Throwable) {
                        Log.i("initprofile", "aaaasucces")

                    }

                }
            )

        }


        val usernameValue = preferences.getString("username", "")

        textView9.text = preferences.getInt("followingNumber", 0).toString()
        textView10.text = preferences.getInt("followersNumber", 0).toString()
        PostsNumber.text = preferences.getInt("publicationsize", 0).toString()
        username.text = "@" + usernameValue
        textView7.text = "@" + usernameValue
        updatePhoto()
        buttonlogout.setOnClickListener {
            logou()
        }

        //get publications


        Picasso.get()
            .load(BASE_URL + preferences.getString("avatar", ""))
            .into(imageprofile)

    }
    //4147768
    fun getVideos(context: Context) {

    val id = preferences.getString("id", "")

    val apiInterface = ApiInterface.create()

    apiInterface.GetVideosByUser(ApiInterface.PublicationRequestBodyGet(id.toString()))
        .enqueue(object : Callback<ApiInterface.VideoResponse> {

            override fun onResponse(
                call: Call<ApiInterface.VideoResponse>,
                response: Response<ApiInterface.VideoResponse>
            ) {
                if (response.code() == 200) {
                    var list = response.body()?.publications!!
                    Log.i("list", list.toString())
                    Log.i("list2", response.body()?.publications!!.toString())
                    if (list == null) {

                    } else {

                        noContentResponse.visibility = View.GONE

                        recylcerPost.visibility = View.VISIBLE
                        adapter = ProfilePostAdapter(list, "mine")
                        recylcerPost.adapter = adapter
                        recylcerPost.layoutManager = GridLayoutManager(context, 3)

                    }

                }

            }

            override fun onFailure(call: Call<ApiInterface.VideoResponse>, t: Throwable) {
                Log.i("failresponse", t.toString())
            }
        })

    }

    fun gotToDiscover() {
        addFriendWrapper.setOnClickListener {
            val changePage = Intent(requireContext(), DiscoverActivity::class.java)
            startActivity(
                changePage,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )

        }
    }

    fun updatePhoto() {
        imageprofile.setOnClickListener {
            permission()
            launchGallery()
        }

    }

    fun gotoSettings() {
        settingsWrapper.setOnClickListener {
            val changePage = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(
                changePage,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )

        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //  Toast.makeText(requireActivity(),"$name permission granted ",Toast.LENGTH_LONG).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        when (requestCode) {
            READ_EXTERNEL_CODE -> innerCheck("externel storage")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun permission() {
        checkForPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            "storage",
            READ_EXTERNEL_CODE
        )
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeType = arrayOf("images/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        }
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        val id = preferences.getString("id", "")

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    imageprofile.setImageURI(selectedImage)
                    //Log.i("profile",selectedImage)
                    UploadUtility(requireActivity(), id.toString(), "").uploadFile(
                        selectedImage!!,
                        "image"
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        profilView = inflater.inflate(R.layout.fragment_profile, container, false)
        noContentResponse = profilView.findViewById(R.id.noContentProfile)


        return profilView

    }


    fun logou() {

        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)



        preferences.edit().clear().commit()


        val changePage = Intent(requireContext(), MainActivity::class.java)
        // Error: "Please specify constructor invocation;
        // classifier 'Page2' does not have a companion object"

        startActivity(changePage)


    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /* override fun onProgressUpdate(percentage: Int) {
         TODO("Not yet implemented")
     }*/
}