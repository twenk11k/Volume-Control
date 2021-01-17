package com.twenk11k.sideproject.volumecontrol.audiovolume

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings

class AudioVolumeObserver(private val context: Context) {

    private var audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var audioVolumeContentObserver: AudioVolumeContentObserver? = null

    fun register(audioStreamType: Int,
                 listener: OnAudioVolumeChangedListener) {
        val handler = Handler(Looper.getMainLooper())
        audioVolumeContentObserver = AudioVolumeContentObserver(
                handler,
                audioManager,
                audioStreamType,
                listener)
        context.contentResolver.registerContentObserver(
                Settings.System.CONTENT_URI,
                true,
                audioVolumeContentObserver!!)
    }

    fun unregister() {
        if (audioVolumeContentObserver != null) {
            context.contentResolver.unregisterContentObserver(audioVolumeContentObserver!!)
            audioVolumeContentObserver = null
        }
    }

}