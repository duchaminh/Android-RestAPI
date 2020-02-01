package com.example.restclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_delete_success.*

class DeleteSuccess : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_success)
        val author = intent.getStringExtra("token")
        Log.i("author", author)
        val token = author.subSequence(7, author.length)
        btnBack.setOnClickListener {
            intent = Intent(this@DeleteSuccess, HomePage::class.java)
            intent.putExtra("token", token)
            finish()
            startActivity(intent)
        }
    }
}
