package com.ssrlab.audioguide.krokapp.vm

import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentExhibitBinding
import kotlinx.coroutines.*

class PlayerViewModel: ViewModel() {

    private var mpStatus = "play"

    private var mediaPlayer: MediaPlayer? = null

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun initializeMediaPlayer(uri: String, binding: FragmentExhibitBinding) {

        mpStatus = "play"

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(uri)
        mediaPlayer!!.prepare()

        binding.apply {
            exhibitPlayerDuration.max = mediaPlayer!!.duration
            exhibitPlayerDuration.progress = 0
            exhibitPlayerButton.setImageResource(R.drawable.ic_play)
        }

        listenProgress(mediaPlayer!!, binding)
    }

    fun playAudio(binding: FragmentExhibitBinding) {
        scope.launch {
            when (mpStatus) {
                "pause" -> {
                    mediaPlayer!!.pause()
                    binding.exhibitPlayerButton.setImageResource(R.drawable.ic_play)
                    mpStatus = "continue"
                }
                "continue" -> {
                    mediaPlayer!!.start()
                    binding.exhibitPlayerButton.setImageResource(R.drawable.ic_pause)
                    mpStatus = "pause"
                    scope.launch { initProgressListener(mediaPlayer!!, binding) }
                }
                "play" -> {
                    try {
                        mediaPlayer!!.start()
                        scope.launch { initProgressListener(mediaPlayer!!, binding) }
                        binding.exhibitPlayerButton.setImageResource(R.drawable.ic_pause)
                        mpStatus = "pause"
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun mpPause() {
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
    }

    private fun mpStop() {
        mpStatus = "stop"

        if (mediaPlayer?.isPlaying == true){
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        } else mediaPlayer?.release()
    }

    private suspend fun initProgressListener(mediaPlayer: MediaPlayer, binding: FragmentExhibitBinding) {
        while (mpStatus == "pause") {
            binding.apply {
                exhibitPlayerTimer.text = convertToTimerMode(mediaPlayer.currentPosition)
                exhibitPlayerDuration.progress = mediaPlayer.currentPosition
            }
            delay(250)

            binding.exhibitPlayerDuration.apply {
                if (progress == max) {
                    mpStatus = "play"
                    delay(250)

                    mediaPlayer.seekTo(0)
                    binding.apply {
                        exhibitPlayerButton.setImageResource(R.drawable.ic_play)
                        exhibitPlayerDuration.progress = 0
                        exhibitPlayerTimer.text = convertToTimerMode(mediaPlayer.currentPosition)
                    }
                }
            }
        }
    }

    private fun listenProgress(mediaPlayer: MediaPlayer, binding: FragmentExhibitBinding) {
        binding.exhibitPlayerDuration.setOnSeekBarChangeListener(createSeekBarProgressListener {
            mediaPlayer.seekTo(it)
            binding.exhibitPlayerTimer.text = convertToTimerMode(mediaPlayer.currentPosition)
        })
    }

    private fun convertToTimerMode(duration: Int): String {
        val minute = duration % (1000 * 60 * 60) / (1000 * 60)
        val seconds = duration % (1000 * 60 * 60) % (1000 * 60) / 1000

        var finalString = "0"
        finalString += "$minute:"
        if (seconds < 10) finalString += "0"
        finalString += "$seconds"

        return finalString
    }

    private fun createSeekBarProgressListener(onProgressChangeFun: (Int) -> Unit): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    onProgressChangeFun(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    override fun onCleared() {
        super.onCleared()

        mpStop()
    }
}