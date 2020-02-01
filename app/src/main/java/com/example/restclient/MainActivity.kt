package com.example.restclient

import android.R.attr.bitmap
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.example.restclient.api.AuthService
import com.example.restclient.api.RetrofilClientCreator

import retrofit2.Retrofit
import okhttp3.ResponseBody;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.R.attr.fillColor
import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener{
            val email = etUserId.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if(email.isEmpty()){
                etUserId.error = "ユーザーIDが入力されていません"
                etUserId.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                etPassword.error = "パスワードが入力されていません"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            val retrofit: Retrofit = RetrofilClientCreator.getClient()
            val authService = retrofit.create(AuthService::class.java)
            val callapi: Call<ResponseBody> = authService.getToken(email, password)

            //var btnLogin: CircularProgressButton = findViewById(R.id.btnLogin)

            btnLogin.startAnimation()
            //myProgressBar.visibility = View.INVISIBLE
            //myProgressBar.visibility = VISIBLE

            callapi.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful){
                        val token = response.body()!!.string()
                        val intent = Intent(applicationContext, HomePage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("token", token)
                        btnLogin.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_48dp))
                        //btnLogin.setProgress(100.toFloat())
                        startActivity(intent)
                    }
                    else{
                        btnLogin.revertAnimation{
                            btnLogin.setBackgroundResource(R.drawable.shape_default)
                        }
                        Toast.makeText(applicationContext, "ユーザー名またはパスワードが間違っています", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

}
