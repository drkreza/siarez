package com.example.math.main

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import com.example.math.R

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val mediaPlayer = MediaPlayer.create(app, R.raw.main_theme)

    init {
        startMedia()
    }

    private fun startMedia() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onCleared() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onCleared()
    }

    fun test() {

    }
}