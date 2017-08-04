package com.example.admin.music.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 音乐播放类
 */

public class MyPlayer {
    public static final int INDEX_STONE_ENTER = 0;
    public static final int INDEX_STONE_CANCEL = 1;
    public static final int INDEX_STONE_COIN = 2;
    //歌曲播放
    private static MediaPlayer mediaPlayer;
    //音效文件的名字
    private final static String[] SONG_NAMES = {"enter.mp3", "cancel.mp3", "coin.mp3"};
    //音效
    private static MediaPlayer[] toneMediaPlayer = new MediaPlayer[SONG_NAMES.length];

    /**
     * 歌曲播放
     *
     * @param context
     * @param songFileName
     */
    public static void palyString(Context context, String songFileName) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        //强制重置状态
        mediaPlayer.reset();
        //加载声音
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(songFileName);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mediaPlayer.prepare();
            //声音波放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopSong(Context context) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 播放音效
     *
     * @param context
     * @param index
     */
    public static void playTone(Context context, int index) {
        //加载声音
        AssetManager assetManager = context.getAssets();
        if (toneMediaPlayer[index] == null) {
            toneMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd(SONG_NAMES[index]);
                toneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                toneMediaPlayer[index].prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //声音波放
        toneMediaPlayer[index].start();
    }
}
