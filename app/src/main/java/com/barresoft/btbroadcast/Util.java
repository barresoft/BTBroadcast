package com.barresoft.btbroadcast;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

/**
 * Created by BarresofT on 28/12/2014.
 */
public class Util{
    public static void beep(){
        try{
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_ACK);
        }catch (Exception e){
            Log.e(null, e.getMessage());
        }
    }
}
