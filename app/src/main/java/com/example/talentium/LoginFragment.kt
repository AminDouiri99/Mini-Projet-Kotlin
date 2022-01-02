package com.example.talentium

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Window

import androidx.fragment.app.Fragment
import android.widget.Button
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.User
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import android.content.SharedPreferences
import com.example.talentium.util.AppDataBase
import com.example.talentium.Model.Users


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonEffect(buttonlogin)
        doTheLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    fun doTheLogin() {
        buttonlogin.setOnClickListener {
            if (editTextEmail.text.toString().equals("")) {

                emailRequired.visibility = View.VISIBLE


            } else {
                emailRequired.visibility = View.INVISIBLE

            }
            if (editTextpassword.text.toString().equals("")) {
                passwordRequired.visibility = View.VISIBLE
            } else {
                passwordRequired.visibility = View.INVISIBLE

            }
            if (editTextEmail.text.toString()
                    .equals("") == false && editTextpassword.text.toString().equals("") == false
            ) {
                // elkhedma hne bech tsir
                buttonlogin.visibility = View.GONE
                waiting.visibility = View.VISIBLE
                forgetpassword.visibility = View.GONE
                callLoginApi(editTextEmail.text.toString(), editTextpassword.text.toString())
            }
        }
    }

    fun callLoginApi(email: String, pass: String) {
        val apiInterface = ApiInterface.create()
        getActivity()?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
        Log.i("http", pass)
        Log.i("http", email)
        apiInterface.seConnecter(ApiInterface.LoginBody(email, pass))
            .enqueue(object : Callback<ApiInterface.LoginResponse> {
                override fun onResponse(
                    call: Call<ApiInterface.LoginResponse>,
                    response: Response<ApiInterface.LoginResponse>
                ) {

                    val token = response.body()
                    Log.i("http", response.body()?.token.toString())

                    if (response.code() == 200) {
                        val preferences: SharedPreferences =
                            requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("token", response.body()?.token.toString())


                        editor.putString("username", response.body()?.user?.username.toString())
                            .apply()
                        editor.putInt("followersNumber", response.body()!!.user.followers.size)
                            .apply()
                        editor.putInt("followingNumber", response.body()!!.user.following.size)
                            .apply()
                        editor.putInt("publication", response.body()!!.user.publication.size)
                            .apply()
                        editor.putString("id", response.body()?.user?._id.toString()).apply()
                        editor.putString("avatar", response.body()?.user?.avatar.toString()).apply()
                        //    AppDataBase.getDatabase(requireContext()).userDao()
                        //      .insert(Users(response.body()?.user?._id.toString(), "", "", "", ""))
                        buttonlogin.visibility = View.VISIBLE
                        waiting.visibility = View.GONE


                        val changePage = Intent(requireContext(), LandingActivity::class.java)
                        // Error: "Please specify constructor invocation;
                        // classifier 'Page2' does not have a companion object"

                        startActivity(changePage)
                        requireActivity().finish()


                    } else {
                        progressBar.visibility = View.GONE
                        textWaiting.text = "Login Failed"
                        LoginFailed.visibility = View.VISIBLE

                        waiting.isClickable = true
                        buttonEffect(waiting)
                        waiting.setOnClickListener {
                            waiting.visibility = View.GONE
                            buttonlogin.visibility = View.VISIBLE
                            doTheLogin()
                            forgetpassword.visibility = View.VISIBLE
                            editTextEmail.setText("")
                            editTextpassword.setText("")
                        }
                    }

                    getActivity()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<ApiInterface.LoginResponse>, t: Throwable) {
                    Log.i("http", t.message.toString())
                    getActivity()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })
    }

    fun buttonEffect(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    buttonlogin.setTextColor(Color.WHITE)
                    v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {

                    buttonlogin.setTextColor(resources.getColor(R.color.Roman_Silver))

                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}