package com.example.restclient

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.restclient.adapter.UserAdapter
import com.example.restclient.api.AuthService
import com.example.restclient.api.AuthorityService
import com.example.restclient.api.RetrofilClientCreator
import com.example.restclient.api.UserService
import com.example.restclient.model.Authority
import com.example.restclient.model.User
import kotlinx.android.synthetic.main.activity_homepage.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import android.widget.LinearLayout
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import okhttp3.ResponseBody


class HomePage : AppCompatActivity() {

    private var drawerLayout:DrawerLayout? = null
    private var toggle:ActionBarDrawerToggle? = null
    private var users = arrayListOf<User>()
    private var listAuthority = arrayListOf<Authority>()

    val retrofit: Retrofit = RetrofilClientCreator.getClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        createMenu()
        var spinner = findViewById<Spinner>(R.id.spAuthority)
        val authentication:String  =  createAuthentication()
        Log.i("authen", authentication)
        var result:String? = null
        var userService = retrofit.create(UserService::class.java)

        getListAuthority(authentication)

        var call: Call<ResponseBody> = userService.getCurrentUser(authentication, authentication.subSequence(7, authentication.length).toString())
        call.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    result = response.body()?.string()
                }
            }

        })
       // Log.i("result", result)

        var callapi: Call<ArrayList<User>> = userService.getUsers(authentication)

        callapi.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful){
                    users = response.body()!!
                    listview.adapter = UserAdapter(applicationContext, users)
                    listview.setOnItemClickListener { parent, view, position, id ->
                        var user = parent.getItemAtPosition(position) as User // The item that was clicked
                        val intent = Intent(this@HomePage, ShousaiActivity::class.java)
                        intent.putExtra("userid", user.userId)
                        intent.putExtra("token", authentication)
                        startActivity(intent)
                    }
                }
                else{
                    Toast.makeText(applicationContext, "トークンが無効です", Toast.LENGTH_LONG).show()
                    //finish()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
        btnSearch.setOnClickListener {
            var familyName = edtFamilyName.text.toString().trim()
            var firstName = edtFirstName.text.toString().trim()
            var authorityName = spinner.selectedItem.toString()
            if(authorityName.equals("Choose Authority") && familyName.isEmpty() && firstName.isEmpty()){
                    Toast.makeText(applicationContext, "検索するため、キーを入力してください", Toast.LENGTH_LONG).show()
            }else {
                if(authorityName.equals("Choose Authority"))
                    authorityName = ""
                callapi = userService.search(familyName, firstName, authorityName, authentication)

                callapi.enqueue(object : Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        if (response.isSuccessful()) {
                            if (response.code() == 204) {
                                Toast.makeText(applicationContext, "ユーザーを見つけない", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                users = response.body()!!
                                listview.adapter = UserAdapter(applicationContext, users)
                            }
                        } else {
                            var intent = Intent(this@HomePage, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(applicationContext, "トークンが無効です", Toast.LENGTH_LONG)
                                .show()
                            finish()

                        }
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_ichiran -> {
                    // handle click
                    val intent = Intent(applicationContext, HomePage::class.java)
                    intent.putExtra("token", authentication.subSequence(7, authentication.length))
                    startActivity(intent)
                    true
                }
                R.id.nav_touroku -> {

                    val intent = Intent(this@HomePage, Add::class.java)
                    intent.putExtra("token", authentication)
                    intent.putExtra("updateUserID", result)
                    startActivity(intent)
                    true
                }
                R.id.nav_shuukei -> {
                    val intent = Intent(this@HomePage, Shuukei::class.java)
                    intent.putExtra("token", authentication)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this@HomePage, MainActivity::class.java)
                    intent.putExtra("token", authentication)
                    //authentication.reversed()
                    finish()
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }

    private fun getListAuthority(authentication:String) {
        var authorityService = retrofit.create(AuthorityService::class.java)
        var callapi: Call<ArrayList<Authority>> = authorityService.getListAuthority(authentication)
        var auth = Authority()
        auth.authorityId = -10
        auth.authorityName = "Choose Authority"
        callapi.enqueue(object : Callback<ArrayList<Authority>> {
            override fun onResponse(call: Call<ArrayList<Authority>>, response: Response<ArrayList<Authority>>) {
                if (response.isSuccessful){
                    listAuthority = response.body()!!

                    listAuthority.add(0,auth)

                    var spinner = findViewById<Spinner>(R.id.spAuthority)
                    val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listAuthority)
                    spinner.adapter = adapter
                    for( i in 0..listAuthority.size-1){
                        if(listAuthority.get(i).authorityName.equals("Choose Authority")){
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

    private fun createAuthentication():String{
        val intent = intent
        val token = intent.getStringExtra("token")
        Log.i("token", token)
        return "Bearer $token"
    }

    private fun createMenu(){
        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //headerText.text = "Ha Duc"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
           when(item.itemId){
               R.id.nav_ichiran -> {
                   var intent = Intent(this, UpdateSuccess::class.java)
                   //intent.putExtra("token",token)
                   startActivity(intent)
                   finish()
                   return true
               }
           }
        }

        return super.onOptionsItemSelected(item)
    }
}
