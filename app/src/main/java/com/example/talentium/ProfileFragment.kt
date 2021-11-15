package com.example.talentium

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface
import com.example.talentium.API.ApiInterface.Companion.BASE_URL
import com.example.talentium.API.UploadRequestBody
import com.example.talentium.Model.ProfilePost
import com.example.talentium.Model.User
import com.google.android.material.snackbar.Snackbar
import com.mvp.handyopinion.UploadUtility
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.logging.Logger
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
    lateinit var recylcerPost: RecyclerView
    lateinit var adapter: ProfilePostAdapter
    var selectedImage :Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //buttonEffect(buttonRegister)
        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        val usernameValue=preferences.getString("username","")
        Log.d("username",usernameValue+"123"+preferences.getBoolean("confirmed",false).toString()+preferences.getInt("followingNumber",45))
        textView9.text=preferences.getInt("followingNumber",45).toString()
        textView10.text=preferences.getInt("followersNumber",45).toString()
        username.text="@"+usernameValue
        textView7.text="@"+usernameValue
       // imageprofile.setImageURI(Uri.)

        updatePhoto()
        buttonlogout.setOnClickListener {
            logou()
            Log.i("logout","logout")
        }

        var postList : MutableList<ProfilePost> = ArrayList()
        recylcerPost=recyclerview
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        postList.add(ProfilePost(image = R.drawable.dummyimage,desc = "My beautifull awesome post"))
        adapter = ProfilePostAdapter(postList)
        Log.i("img",BASE_URL+preferences.getString("avatar",""))
        recylcerPost.adapter = adapter
        recylcerPost.layoutManager=GridLayoutManager(requireContext(),3)
        Glide.with(this)
            .load(BASE_URL+preferences.getString("avatar","")).fitCenter()
            .into(imageprofile)
        Log.i("upload","failure1")

        buttonupdate.setOnClickListener {
            Log.i("upload","failure1")

           // uploadImage()
            Log.i("upload","failure2")

        }

    }
    fun updatePhoto(){
        imageprofile.setOnClickListener {
            launchGallery()
        }

    }
    fun uploadImage(){
        if(selectedImage== null){
//            root.snackbar("select an image first")
            return
        }
       /* val parceFileDescriptor =
            requireActivity().contentResolver
                .openFileDescriptor(selectedImage!!,"r",null) ?:return
        val inputstream = FileInputStream(parceFileDescriptor.fileDescriptor)
        val file = File(requireActivity().cacheDir,requireActivity().contentResolver.getFileName(selectedImage!!))
        val outputstream = FileOutputStream(file)
        inputstream.copyTo(outputstream)
        progressBar2.progress=0
        val body= UploadRequestBody(file,"image",this)
        val apiInterface = ApiInterface.create()
        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        val id = preferences.getString("id","")

        apiInterface.uploadImage(MultipartBody.Part.createFormData("image",file.name,body),id.toString())
            .enqueue(object :Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    progressBar2.progress=100
                    Log.i("upload",response.code().toString())
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("upload","failure")

                  //  root.snackbar("uploaded image succesfully")
                }

            })*/



    }


    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK).also {
            it.type="image/*"
            val mimeType = arrayOf("images/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,mimeType)
        }
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            Log.i("image",data.toString())
            when (requestCode){

                REQUEST_CODE_IMAGE_PICKER->{
                    selectedImage=data?.data
                    imageprofile.setImageURI(selectedImage)
                    UploadUtility(requireActivity()).uploadFile(selectedImage!!)
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)


           }


fun logou(){

        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)


    Log.d("token",preferences.getString("token","defaultName").toString())
        preferences.edit().clear().commit()

    Log.d("token removed",preferences.getString("token","defaultName").toString())


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