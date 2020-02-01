package com.example.restclient

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.restclient.api.AuthorityService
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import com.example.restclient.model.Authority
import com.example.restclient.model.Gender
import com.example.restclient.model.UserForAddAndUpdate
import com.example.restclient.model.UserInfo
import kotlinx.android.synthetic.main.activity_add.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Add : AppCompatActivity() {
    val retrofit: Retrofit = RetrofilClientCreator.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val userService = retrofit.create(UserService::class.java)
        val token = intent.getStringExtra("token")
        var userCreateId = intent.getStringExtra("updateUserID")
        getListAuthority(retrofit, token)
        getListGender(retrofit)

        Log.i("result", userCreateId)
        btnAdd.setOnClickListener {
            val userid = edtUserId.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val familyName = edtFamilyName.text.toString().trim()
            val firstName = edtFirstName.text.toString().trim()
            if(userid.isEmpty()){
                edtUserId.error = "ユーザーIDが入力されていません"
                edtUserId.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                edtPassword.error = "パスワードが入力されていません"
                edtPassword.requestFocus()
                return@setOnClickListener
            }
            if(familyName.isEmpty()){
                edtFamilyName.error = "姓が入力されていません"
                edtFamilyName.requestFocus()
                return@setOnClickListener
            }
            if(firstName.isEmpty()){
                edtFirstName.error = "名が入力されていません"
                edtFirstName.requestFocus()
                return@setOnClickListener
            }
            
            var user:UserForAddAndUpdate = UserForAddAndUpdate()
            user.userId = userid
            user.password = password
            user.familyName = familyName
            user.firstName = firstName
            if(edtAge.text.toString().isNotEmpty())
                user.age = edtAge.text.toString().toInt()
            if(cbAdmin.isChecked)
                user.admin = 1
            else
                user.admin = 0
            //user.createDate = 20200114
            //user.updateDate = 20200114
            val date = Date()
            val formatter = SimpleDateFormat("yyyyMMddHHmmss")
            user.createDate = java.lang.Long.parseLong(formatter.format(date))
            user.updateDate = java.lang.Long.parseLong(formatter.format(date))

            user.createUserId = userCreateId
            user.updateUserID = userCreateId
            var authoritySelected = spRole.selectedItem as Authority
            user.authorityId = authoritySelected.authorityId
            var genderSelected = spGender.selectedItem as Gender
            user.genderId = genderSelected.genderId

            var call:Call<UserForAddAndUpdate>
            call = userService.create(user,token)
            call.enqueue(object : Callback<UserForAddAndUpdate>{
                override fun onFailure(call: Call<UserForAddAndUpdate>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<UserForAddAndUpdate>, response: Response<UserForAddAndUpdate>) {
                    if(response.isSuccessful){
                        var intent = Intent(this@Add, UpdateSuccess::class.java)
                        intent.putExtra("token",token)
                        startActivity(intent)
                    }else{
                        if(response.code() == 409)
                            Toast.makeText(applicationContext, "ユーザーIDが重複しています", Toast.LENGTH_LONG).show()
                        else{
                            var intent = Intent(this@Add, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(applicationContext, "トークンが無効です", Toast.LENGTH_LONG)
                                .show()
                            finish()
                        }
                    }
                }

            })

        }

    }

    private fun getListAuthority(retrofit: Retrofit, authentication:String) {
        var authorityService = retrofit.create(AuthorityService::class.java)
        var callapi: Call<ArrayList<Authority>> = authorityService.getListAuthority(authentication)
        //var listAuthority = arrayListOf<Authority>()
        callapi.enqueue(object : Callback<ArrayList<Authority>> {
            override fun onResponse(call: Call<ArrayList<Authority>>, response: Response<ArrayList<Authority>>) {
                if (response.isSuccessful){
                    var listAuthority = response.body()!!

                    var spinner = findViewById<Spinner>(R.id.spRole)
                    val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listAuthority)
                    spinner.adapter = adapter

                    for( i in 0..listAuthority.size-1){
                        if(listAuthority.get(i).authorityName.equals("None")){
                            spinner.setSelection(i)
                            break
                        }
                    }

                }
                else{
                    Toast.makeText(applicationContext, ":((", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Authority>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun getListGender(retrofit: Retrofit) {
        var listGender = arrayListOf<Gender>()
        var woman = Gender()
        woman.genderId = 0
        woman.genderName = "女"
        var man = Gender()
        man.genderId = 1
        man.genderName = "男"
        var none = Gender()
        none.genderId = -1
        none.genderName = ""

        listGender.add(woman)
        listGender.add(man)
        listGender.add(none)

        var spinner = findViewById<Spinner>(R.id.spGender)
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listGender)
        spinner.adapter = adapter

        for( i in 0..listGender.size-1){
            if(listGender.get(i).genderName.equals("")){
                spinner.setSelection(i)
                break
            }
        }

    }

}
