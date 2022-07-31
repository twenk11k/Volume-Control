package com.twenk11k.sideproject.volumecontrol.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.twenk11k.sideproject.volumecontrol.R
import com.twenk11k.sideproject.volumecontrol.audiovolume.AudioVolumeObserver
import com.twenk11k.sideproject.volumecontrol.audiovolume.OnAudioVolumeChangedListener
import com.twenk11k.sideproject.volumecontrol.databinding.ActivityMainBinding
import com.twenk11k.sideproject.volumecontrol.pref.ProgressPrefs

class MainActivity : AppCompatActivity(), OnAudioVolumeChangedListener {

    private var audioManager: AudioManager? = null
    private var sBarProgress = 0
    private var audioVolumeObserver: AudioVolumeObserver? = null
    private var musicNoteDrawable: Drawable? = null
    private var musicOffDrawable: Drawable? = null
    private val preferences = ProgressPrefs()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        volumeControlStream = AudioManager.STREAM_MUSIC
        setViews()
    }

    override fun onResume() {
        super.onResume()
        if (audioManager != null) {
            val currentVol = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
            binding.seekBar.progress = currentVol
            handleIcon(currentVol)
        }
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(this)
        }
        audioVolumeObserver?.register(AudioManager.STREAM_MUSIC, this)
    }

    override fun onPause() {
        super.onPause()
        audioVolumeObserver?.unregister()
    }

    private fun setViews() {
        setSeekBar()
        setDrawables()
        setViewListeners()
    }

    private fun setDrawables() {
        musicNoteDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_music_note_white_24)
        musicOffDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_music_off_white_24)
    }

    private fun setViewListeners() {
        binding.imgMusicNote.setOnClickListener {
            if (binding.imgMusicNote.drawable == musicNoteDrawable) {
                preferences.seekBarProgress = sBarProgress
                binding.seekBar.progress = 0
                sBarProgress = 0
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    sBarProgress,
                    AudioManager.FLAG_PLAY_SOUND
                )
                binding.imgMusicNote.setImageDrawable(musicOffDrawable)
            } else {
                sBarProgress = preferences.seekBarProgress
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    sBarProgress,
                    AudioManager.FLAG_PLAY_SOUND
                )
                binding.seekBar.progress = sBarProgress
                binding.imgMusicNote.setImageDrawable(musicNoteDrawable)
            }
        }
    }

    private fun setSeekBar() {
        binding.seekBar.secondaryProgress = 100
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.seekBar.max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.seekBar.progress = volume
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sBarProgress = progress
                handleIcon(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    sBarProgress,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        })
    }

    private fun handleIcon(currentVol: Int) {
        if (currentVol == 0) {
            binding.imgMusicNote.setImageDrawable(musicOffDrawable)
        } else {
            binding.imgMusicNote.setImageDrawable(musicNoteDrawable)
        }
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        binding.seekBar.progress = currentVolume
        handleIcon(currentVolume)
    }
}
