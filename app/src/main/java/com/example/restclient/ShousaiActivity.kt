package com.example.restclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import com.example.restclient.model.UserInfo
import kotlinx.android.synthetic.main.activity_shousai.*

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ShousaiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shousai)
        val intent = intent
        //textView2.text = intent.getStringExtra("userid")

        val retrofit: Retrofit = RetrofilClientCreator.getClient()
        val userService = retrofit.create(UserService::class.java)
        val userId = intent.getStringExtra("userid")
        val token = intent.getStringExtra("token")
        val callapi: Call<UserInfo> = userService.getUserInfo(intent.getStringExtra("userid"), intent.getStringExtra("token"))
        Log.i("author", token)


        var call: Call<ResponseBody> = userService.getCurrentUser(token, token.subSequence(7, token.length).toString())
        var result:String? = null
        call.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful()){
                    result = response.body()?.string()
                }
            }

        })
        //Log.i("result", result)
        callapi.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful){
                    val userinfo = response.body()!!
                    getUserInfo(userinfo)
                    btnDelete.setOnClickListener {
                        val intent = Intent(this@ShousaiActivity, DeleteActivity::class.java)
                        intent.putExtra("token", token)
                        intent.putExtra("userId", userId)
                        intent.putExtra("username", userinfo.familyName + " " + userinfo.firstName)
                        startActivity(intent)
                    }
                    btnEdit.setOnClickListener {

                        val intent = Intent(this@ShousaiActivity,Edit::class.java)
                        intent.putExtra("userInfo", userId)
                        intent.putExtra("token", token)
                        Log.i("result", result)
                        intent.putExtra("updateUserID", result)
                        if(userId != null && result!= null && token!=null)
                            startActivity(intent)
                    }

                }
                else{
                    Toast.makeText(applicationContext, ".........", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }
    fun getUserInfo(userinfo:UserInfo){
        tvUserId.text = userinfo.userId.toString()
        tvPassword.text = userinfo.password.toString()
        tvFamilyName.text = userinfo.familyName.toString()
        tvFirstName.text = userinfo.firstName.toString()
        tvAge.text = userinfo.age.toString()
        tvGender.text = userinfo.genderName.toString()
        tvRole.text = userinfo.authorityName.toString()
        if(userinfo.admin == 1)
            tvAdmin.text = "Admin"
        else
            tvAdmin.text = ""
    }
}
