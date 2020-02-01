package com.example.restclient


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import com.example.restclient.model.UserInfo
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.edtFamilyName

import kotlinx.android.synthetic.main.activity_shousai.tvUserId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.restclient.api.AuthorityService
import com.example.restclient.model.Authority
import com.example.restclient.model.Gender
import com.example.restclient.model.UserForAddAndUpdate
import kotlinx.android.synthetic.main.activity_delete.*
import okhttp3.ResponseBody
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Edit : AppCompatActivity() {

    val retrofit: Retrofit = RetrofilClientCreator.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val intent = intent
        val userService = retrofit.create(UserService::class.java)
        val userId = intent.getStringExtra("userInfo")
        val token = intent.getStringExtra("token")
        var userUpdateId = intent.getStringExtra("updateUserID")
        Log.i("userUpdateId", userUpdateId)
        var callapi: Call<UserInfo> = userService.getUserInfo(userId, token)

        callapi.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful){
                    val userinfo :UserInfo = response.body()!!
                    getUserInfo(userinfo, token)
                    btnEdit.setOnClickListener {
                        val user = UserForAddAndUpdate()
                        user.userId = tvUserId.text.toString()
                        user.familyName = edtFamilyName.text.toString()
                        user.firstName = edFirstName.text.toString()
                        user.password = edtPassword.text.toString()

                        user.age = edtAge.text.toString().toInt()
                        var genderSelected = spGender.selectedItem as Gender
                        user.genderId = genderSelected.genderId

                        var authoritySelected = spRole.selectedItem as Authority
                        user.authorityId = authoritySelected.authorityId

                        val date = Date()
                        val formatter = SimpleDateFormat("yyyyMMddHHmmss")

                        println(java.lang.Long.parseLong(formatter.format(date)))
                        user.updateDate = java.lang.Long.parseLong(formatter.format(date))

                        user.updateUserID = userUpdateId
                        if(cbAdmin.isChecked)
                            user.admin = 1
                        else
                            user.admin = 0
                        var call:Call<ResponseBody>
                        call = userService.update(user.userId!!, user, token)
                        call.enqueue(object : Callback<ResponseBody>{
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if(response.isSuccessful()){
                                    var intent = Intent(this@Edit, UpdateSuccess::class.java)
                                    intent.putExtra("token",token)
                                    startActivity(intent)
                                }else{
                                    var intent = Intent(this@Edit, MainActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(applicationContext, "トークンが無効です", Toast.LENGTH_LONG)
                                        .show()
                                    finish()
                                }
                            }

                        }
                        )
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

    fun getUserInfo(userinfo:UserInfo, token:String){
        tvUserId.text = userinfo.userId.toString()
        edtPassword.setText(userinfo.password)
        edtFamilyName.setText(userinfo.familyName)
        edFirstName.setText(userinfo.firstName)
        if(userinfo.age != null)
            edtAge.setText(userinfo.age.toString())
        if(userinfo.admin == 1)
            cbAdmin.isChecked = true
        getListAuthority(retrofit, token, userinfo)
        getListGender(retrofit, token, userinfo)

    }

    private fun getListAuthority(retrofit: Retrofit, authentication:String, userinfo: UserInfo) {
        var authorityService = retrofit.create(AuthorityService::class.java)
        var callapi: Call<ArrayList<Authority>> = authorityService.getListAuthority(authentication)
        callapi.enqueue(object : Callback<ArrayList<Authority>> {
            override fun onResponse(call: Call<ArrayList<Authority>>, response: Response<ArrayList<Authority>>) {
                if (response.isSuccessful){
                    var listAuthority = response.body()!!

                    var spinner = findViewById<Spinner>(R.id.spRole)

                    val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listAuthority)
                    spinner.adapter = adapter

                    for( i in 0..listAuthority.size-1){
                        if(listAuthority.get(i).authorityName.equals(userinfo.authorityName)){
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

    private fun getListGender(retrofit: Retrofit, authentication:String, userinfo: UserInfo) {
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

                    for( i in 0 until listGender.size-1){
                        if(listGender[i].genderName == (userinfo.genderName)){
                            spinner.setSelection(i)
                            break
                        }
                    }
        }
}
