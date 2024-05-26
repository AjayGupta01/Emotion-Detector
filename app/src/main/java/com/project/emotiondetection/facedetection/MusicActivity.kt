package com.project.emotiondetection.facedetection

import android.content.Context
import android.content.Intent
import android.media.JetPlayer
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.emotiondetection.R
import com.project.emotiondetection.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMusicBinding
    private lateinit var mediaPlayer:MediaPlayer
    private var isPlaying = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.happy)
        mediaPlayer.start()
        binding.status.text = "Smiling..."
        binding.playButton.setOnClickListener {
            togglePlayPause()
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause()
            binding.status.text = "Paused"
        } else {
            mediaPlayer.start()
            binding.status.text = "Smiling..."
        }
        isPlaying = !isPlaying
    }

    companion object{
        fun startMusicActivity(context:Context){
            Intent(context,MusicActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized){
            mediaPlayer.release()
        }
    }
}