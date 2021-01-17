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
        // with this handler AudioVolumeContentObserver#onChange()
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907
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