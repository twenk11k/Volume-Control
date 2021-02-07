package com.twenk11k.sideproject.volumecontrol.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.twenk11k.sideproject.volumecontrol.R
import com.twenk11k.sideproject.volumecontrol.audiovolume.AudioVolumeObserver
import com.twenk11k.sideproject.volumecontrol.audiovolume.OnAudioVolumeChangedListener
import com.twenk11k.sideproject.volumecontrol.databinding.ActivityMainBinding
import com.twenk11k.sideproject.volumecontrol.pref.ProgressPrefs

class MainActivity : DataBindingActivity(), OnAudioVolumeChangedListener {

    // views
    private lateinit var seekBar: SeekBar
    private lateinit var imgMusicNote: ImageView

    private var audioManager: AudioManager? = null
    private var sBarProgress = 0
    private var audioVolumeObserver: AudioVolumeObserver? = null
    private var musicNoteDrawable: Drawable? = null
    private var musicOffDrawable: Drawable? = null
    private val preferences = ProgressPrefs()

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        volumeControlStream = AudioManager.STREAM_MUSIC
        setViews()
    }

    override fun onResume() {
        super.onResume()
        if (audioManager != null) {
            val currentVol = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
            seekBar.progress = currentVol
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
        seekBar = binding.seekBar
        imgMusicNote = binding.imgMusicNote
        setSeekBar()
        setDrawables()
        setViewListeners()
    }

    private fun setDrawables() {
        musicNoteDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_music_note_white_24)
        musicOffDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_music_off_white_24)
    }

    private fun setViewListeners() {
        imgMusicNote.setOnClickListener {
            if (imgMusicNote.drawable == musicNoteDrawable) {
                preferences.seekBarProgress = sBarProgress
                seekBar.progress = 0
                sBarProgress = 0
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, sBarProgress, AudioManager.FLAG_PLAY_SOUND)
                imgMusicNote.setImageDrawable(musicOffDrawable)
            } else {
                sBarProgress = preferences.seekBarProgress
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, sBarProgress, AudioManager.FLAG_PLAY_SOUND)
                seekBar.progress = sBarProgress
                imgMusicNote.setImageDrawable(musicNoteDrawable)
            }
        }
    }

    private fun setSeekBar() {
        seekBar.secondaryProgress = 100
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        seekBar.max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        seekBar.progress = volume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sBarProgress = progress
                handleIcon(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, sBarProgress, AudioManager.FLAG_PLAY_SOUND);
            }
        })
    }

    private fun handleIcon(currentVol: Int) {
        if (currentVol == 0) {
            imgMusicNote.setImageDrawable(musicOffDrawable)
        } else {
            imgMusicNote.setImageDrawable(musicNoteDrawable)
        }
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        seekBar.progress = currentVolume
        handleIcon(currentVolume)
    }

}