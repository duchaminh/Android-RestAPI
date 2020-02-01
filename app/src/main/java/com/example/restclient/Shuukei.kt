package com.example.restclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restclient.adapter.ShuukeiAdapter
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import com.example.restclient.model.AggregateByAuthority
import kotlinx.android.synthetic.main.activity_shuukei.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Shuukei : AppCompatActivity() {
    val retrofit: Retrofit = RetrofilClientCreator.getClient()
    var listAggregate = arrayListOf<AggregateByAuthority>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuukei)
        val token = intent.getStringExtra("token")
        var userService = retrofit.create(UserService::class.java)

        var callapi: Call<ArrayList<AggregateByAuthority>> = userService.getShuukei(token)

        callapi.enqueue(object : Callback<ArrayList<AggregateByAuthority>> {
            override fun onResponse(call: Call<ArrayList<AggregateByAuthority>>, response: Response<ArrayList<AggregateByAuthority>>) {
                if (response.isSuccessful()){
                    listAggregate = response.body()!!
                    listviewShuukei.adapter = ShuukeiAdapter(applicationContext, listAggregate)
                }
                else{
                    Toast.makeText(applicationContext, "Token is invalid", Toast.LENGTH_LONG).show()
                    //finish()
                }
            }

            override fun onFailure(call: Call<ArrayList<AggregateByAuthority>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

        btnReset.setOnClickListener {
            var callreset: Call<ArrayList<AggregateByAuthority>> = userService.getShuukei(token)
            callreset.enqueue(object : Callback<ArrayList<AggregateByAuthority>> {
                override fun onResponse(call: Call<ArrayList<AggregateByAuthority>>, response: Response<ArrayList<AggregateByAuthority>>) {
                    if (response.isSuccessful){
                        listAggregate = response.body()!!
                        listviewShuukei.adapter = ShuukeiAdapter(applicationContext, listAggregate)
                    }
                    else{
                        Toast.makeText(applicationContext, "Token is invalid", Toast.LENGTH_LONG).show()
                        //finish()
                    }
                }

                override fun onFailure(call: Call<ArrayList<AggregateByAuthority>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        btnBack.setOnClickListener {
            intent = Intent(this@Shuukei, HomePage::class.java)
            intent.putExtra("token", token.subSequence(7, token.length))
            finish()
            startActivity(intent)
        }


    }
}
