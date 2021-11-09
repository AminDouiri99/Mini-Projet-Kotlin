package com.example.talentium

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import com.example.talentium.API.ApiInterface
import com.example.talentium.Model.User
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.emailRequired
import kotlinx.android.synthetic.main.fragment_register.passwordRequired
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

   /* private lateinit var email:EditText
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var confirmPassword:EditText
    private lateinit var register :Button*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView :View = inflater.inflate(R.layout.fragment_register, container, false)

        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonEffect(buttonlogin)
        buttonRegister.setOnClickListener {

            if(validateForm()){
                //make the call
            }
        }
    }

    private fun Register(){
        val apiInterface = ApiInterface.create()
        getActivity()?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
        Log.i("http","requette0")

        val map: HashMap<String, String> = HashMap()

        map["email"] = editRegisterEmail.text.toString()
        map["password"] = editPassword.text.toString()
        /*apiInterface.Register(map).enqueue(JsonArray : Callback<User> {
            override fun onResponse(call: Call<JsonArray>, response: Response<User>) {

                val user = response.body()
                Log.i("http",user.toString())

                if (user != null){
                }else{
                }

                getActivity()?.window?.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("http",t.message.toString())
                getActivity()?.window?.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        })*/
    }

    fun  validateForm():Boolean{
        if(editRegisterEmail.text.toString().equals("")){
            emailRequired.visibility=View.VISIBLE
            return false
        }else{
            emailRequired.visibility=View.INVISIBLE
        }
        if(editTextUsername.text.toString()==""){
            usernameRequired.visibility=View.VISIBLE
            return false
        }

        else{
            usernameRequired.visibility=View.INVISIBLE
        }

            if(editPassword.text.toString()=="") {
            passwordRequired.visibility=View.VISIBLE
                return false
        }else{
                passwordRequired.visibility=View.INVISIBLE
            }

            if(editConfirmPassword.text.toString()==""){
            confirmpasswordRequired.visibility=View.VISIBLE
                return false
        }else{
                confirmpasswordRequired.visibility=View.INVISIBLE

            }

            if(editConfirmPassword.text.toString()!=editPassword.text.toString()){
            confirmpasswordRequired.visibility=View.VISIBLE
                return false
        }else{
                confirmpasswordRequired.visibility=View.INVISIBLE

            }
        return true

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
}