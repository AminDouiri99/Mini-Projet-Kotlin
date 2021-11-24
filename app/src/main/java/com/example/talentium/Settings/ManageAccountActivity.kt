package com.example.talentium.Settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.example.talentium.API.ApiInterface
import com.example.talentium.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_manage_account.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageAccountActivity : AppCompatActivity() {
    var UpdatePicutreLayout: Boolean = true;
    var UpdateNameLayout: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)
        //openUpdatePicutreLayout()

        UpdatePicutreLayout = true
        UpdateNameLayout = true
        openUpdatePicutreLayout()
        openUpdateInfoLayout()
        gobackarrow.setOnClickListener {
            onBackPressed();
        }
    }

    fun updateUserName() {
        updateusernameBtn.setOnClickListener {
            val preferences: SharedPreferences =
                getSharedPreferences("pref", Context.MODE_PRIVATE)
            val apiInterface = ApiInterface.create()
            apiInterface.updateuser(
                ApiInterface.updateUserBody(
                    preferences.getString("id", "").toString(),
                    updateusernameEditText.text.toString()
                )
            ).enqueue(
                object : Callback<ApiInterface.updateResponse> {
                    override fun onResponse(
                        call: Call<ApiInterface.updateResponse>,
                        response: Response<ApiInterface.updateResponse>
                    ) {
                        preferences.edit().remove("username").commit()
                        Log.i("amineoss",response.body()?.user?.username.toString())
                        preferences.edit().putString("username", response.body()?.user?.username.toString()).commit()
                        Snackbar.make(rootmanageaccount, "Username updated !", Snackbar.LENGTH_SHORT).show()
                        updateinfolayout.visibility = View.GONE
                        updatenamearrow.animate().rotationBy(-90f).setDuration(300)
                            .setInterpolator(LinearInterpolator()).start();
                        UpdateNameLayout = true

                    }
                    override fun onFailure(call: Call<ApiInterface.updateResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                }
            )

        }
    }

    fun openUpdateInfoLayout() {
        updatenameemenu.setOnClickListener {
            if (UpdateNameLayout == false) {
                updateinfolayout.visibility = View.GONE
                updatenamearrow.animate().rotationBy(-90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();
                UpdateNameLayout = true
            } else {
                updatenamearrow.rotation = 0f
                UpdateNameLayout = false
                updateinfolayout.visibility = View.VISIBLE
                updatenamearrow.animate().rotationBy(90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();
                val preferences: SharedPreferences =
                    getSharedPreferences("pref", Context.MODE_PRIVATE)

                updateusernameEditText.setText(preferences.getString("username", ""))
                updateUserName()
            }
        }
    }

    fun openUpdatePicutreLayout() {
        updatepicturemenu.setOnClickListener {
            Log.i("lennaaa", "tenzelt")
            if (UpdatePicutreLayout == false) {
                updatephotoLayout.visibility = View.GONE
                updatepicarrow.animate().rotationBy(-90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();
                UpdatePicutreLayout = true
            } else {
                updatepicarrow.rotation = 0f

                UpdatePicutreLayout = false
                updatephotoLayout.visibility = View.VISIBLE
                updatepicarrow.animate().rotationBy(90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();

                val preferences: SharedPreferences =
                    getSharedPreferences("pref", Context.MODE_PRIVATE)

                Glide.with(this)
                    .load(ApiInterface.BASE_URL + preferences.getString("avatar", "")).fitCenter()
                    .into(imageprofileinsettings)

            }
        }

    }
}