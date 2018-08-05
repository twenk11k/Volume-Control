package com.twenk11k.sideproject.volumecontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    private static SharedPreferences settings;
    public static int getSeekbarProgress(Context context) {

        settings = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        return settings.getInt("sbar_progress", 0);

    }
    public static void setSeekbarProgress(Context context, int pos) {

        settings = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        settings.edit().putInt("sbar_progress", pos).apply();

    }
}