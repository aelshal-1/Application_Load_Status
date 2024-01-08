package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val intent = intent
        val name = intent.getStringExtra("name")
        Log.d("TEST","filename:$name")
        val status = intent.getBooleanExtra("status", false)

        if(status){
            binding.contentDetail.statusValueTV.setTextColor( Color.GREEN)
        }else{
            binding.contentDetail.statusValueTV.setTextColor( Color.RED)
        }
        val details = Details(name, if (status) "Success" else "Failed")
        binding.contentDetail.details = details

        binding.contentDetail.finishBtn.setOnClickListener {
            finish()
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

    }

    data class Details(val title :String?, val status:String)
}
