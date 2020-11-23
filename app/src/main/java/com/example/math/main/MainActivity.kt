package com.example.math.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.math.GameUtil
import com.example.math.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      viewModel.test()
    }

    override fun onStart() {
        super.onStart()
        hideStatusAndNavigationBar()
    }

    private fun hideStatusAndNavigationBar() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                /* or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                 or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                 or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*/
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}