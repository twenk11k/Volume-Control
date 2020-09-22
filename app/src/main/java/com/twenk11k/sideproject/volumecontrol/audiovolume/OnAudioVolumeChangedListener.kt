package com.twenk11k.sideproject.volumecontrol.audiovolume

interface OnAudioVolumeChangedListener {

    fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int)

}