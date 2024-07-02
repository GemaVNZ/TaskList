package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasklist.R
import com.example.tasklist.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    //private lateinit var binding = ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //binding = ActivityDetailBinding.inflate(layoutInflater)
        //setContentView(binding.root)
    }
}