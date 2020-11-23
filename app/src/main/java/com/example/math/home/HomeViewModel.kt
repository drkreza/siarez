package com.example.math.home

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import com.example.math.R

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
    private val mediaPlayer = MediaPlayer.create(app, R.raw.tap_enter)

    init {

    }

    fun test() {

    }

    override fun onCleared() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onCleared()
    }

    fun playTapSound(){
        mediaPlayer.start()
    }
}