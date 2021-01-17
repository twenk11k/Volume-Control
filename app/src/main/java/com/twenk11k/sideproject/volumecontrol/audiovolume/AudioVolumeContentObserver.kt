package com.twenk11k.sideproject.volumecontrol.audiovolume

import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler

class AudioVolumeContentObserver(handler: Handler, private val audioManager: AudioManager, private val audioStreamType: Int, private val listener: OnAudioVolumeChangedListener) : ContentObserver(handler) {

    private var lastVolume = audioManager.getStreamVolume(audioStreamType)

    override fun onChange(selfChange: Boolean) {
        val maxVolume: Int = audioManager.getStreamMaxVolume(audioStreamType)
        val currentVolume: Int = audioManager.getStreamVolume(audioStreamType)
        if (currentVolume != lastVolume) {
            lastVolume = currentVolume
            listener.onAudioVolumeChanged(currentVolume, maxVolume)
        }
    }

}