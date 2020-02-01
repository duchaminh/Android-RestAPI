package com.example.restclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import kotlinx.android.synthetic.main.activity_delete.*

import kotlinx.android.synthetic.main.activity_shousai.tvUserId
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DeleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)
        val intent= intent
        val token = intent.getStringExtra("token")
        tvUserId.text = intent.getStringExtra("userId")
        tvUserName.text = intent.getStringExtra("username")

        btnDelete.setOnClickListener {
            val retrofit: Retrofit = RetrofilClientCreator.getClient()
            val userService = retrofit.create(UserService::class.java)
            val userId = intent.getStringExtra("userId")
            val token = intent.getStringExtra("token")
            val callapi: Call<ResponseBody> = userService.delete(userId, token)

            callapi.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful){
                        val intent = Intent(this@DeleteActivity, DeleteSuccess::class.java)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, ".........", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })

        }
    }
}
