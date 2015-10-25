package com.idiotsapps.chaoenglish.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.idiotsapps.chaoenglish.R;

/**
 * Created by vantuegia on 10/24/2015.
 */
public class SoundHelper {

    private SoundPool mSoundPool;
    private float mVolume;
    private int[] mSoundId;
    public final static int SOUND_CHOOSE_RIGHT = 0;

    public SoundHelper (Context context) {
        mSoundId = new int[10]; // max: ten sound
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        } else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).build());
            builder.setMaxStreams(1);
            mSoundPool = builder.build();
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundId[0] = mSoundPool.load(context, R.raw.sound_choose_right, 1);
    }

    public int playSound(int id) {
        return mSoundPool.play(mSoundId[0], // temp hard code
                mVolume,
                mVolume,
                1,
                0, // loop = 0 - no loop
                1);
    }

}
