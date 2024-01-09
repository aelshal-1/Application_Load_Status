package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
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
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }

        //binding.contentDetail.myMotion.transitionToStart()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()



        val autoTransition = AutoTransition()
        autoTransition.setDuration(1000)
        binding.contentDetail.myMotion.postDelayed({
            TransitionManager.beginDelayedTransition(binding.contentDetail.myMotion, autoTransition)
            binding.contentDetail.myMotion.transitionToEnd()
        }, 1000)

    }

    data class Details(val title :String?, val status:String)
}
