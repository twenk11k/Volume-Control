package com.twenk11k.sideproject.volumecontrol;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarVolume = null;
    private AudioManager audioManager = null;
    private int sbarProgress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setSeekBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(seekBarVolume!=null){
            if(audioManager!=null){
                seekBarVolume.setProgress(audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        }
    }

    private void setSeekBar() {
        try
        {
            seekBarVolume = (SeekBar)findViewById(R.id.seekbar);
            seekBarVolume.setSecondaryProgress(100);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seekBarVolume.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            sbarProgress, AudioManager.FLAG_PLAY_SOUND);
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    Log.d("progres_changed",String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                    sbarProgress = progress;

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
