package study.oscar.player.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import study.oscar.player.base.SongItem;
import study.oscar.player.manager.SongListManager;
import study.oscar.player.util.Consts;
import study.oscar.player.util.ImageUtil;

/**
 * Created by oscar on 2015/10/6.
 */
public class MusicService extends IntentService{

    static final String TAG =  "MusicService";

    MusicBinder mBinder = new MusicBinder();
    SongListManager mSongListManager = null;
    SongItem mSong;

    MediaPlayer mp = null;
    boolean mbPlaying;
    public static final Object musicLock = new Object();

    public void seekTo(int i){
        mp.seekTo(i);
    }

    public MusicService() {
        this("MusicService");
    }

    public MusicService(String name){
        super(name);
        mbPlaying = false;
        mp = new MediaPlayer();
        mSongListManager = SongListManager.getInstance();
        mSong = mSongListManager.getCurSong();
        setMediaPath(mSongListManager.getCurSongPath(), false);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if(action == null) return;
        if(action.equals("pre")){
            preSong();
        }else if(action.equals("next")){
            nextSong();
        }else if(action.equals("play")){
            if(mbPlaying){
                pause();
            }else {
                play();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    public boolean setMediaPath(String filePath,boolean bPlay){
        try {
            synchronized(musicLock) {
                mp.stop();
                mp.reset();
                mp.setDataSource(filePath);
                mp.prepare();
            }
            if (bPlay) {
                mbPlaying = true;
                play();
            } else {
                mbPlaying = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void play(){
        synchronized(musicLock) {
            mp.start();
        }
        Intent playIntent = new Intent();
        playIntent.setAction(Consts.MY_PLAY_ACTION);
        sendBroadcast(playIntent);
        mbPlaying = true;
    }

    public void pause(){
        mp.pause();
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(Consts.MY_PAUSE_ACTION);
        sendBroadcast(pauseIntent);
        mbPlaying = false;
    }

    public void stop(){
        mp.pause();
        mp.seekTo(0);
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(Consts.MY_STOP_ACTION);
        sendBroadcast(pauseIntent);
        mbPlaying = false;
    }

    public void preSong(){
        mSong = mSongListManager.prevSong();
        startSetPathThread();
    }

    public void nextSong(){
        mSong = mSongListManager.nextSong();
        startSetPathThread();
    }

    public void switchSong(int index){
        if(mSongListManager.getCurSongIndex()  == index){
            return;
        }
        mSong = mSongListManager.switchSong(index);
        startSetPathThread();
    }

    void startSetPathThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "startSetPathThread run");
                setMediaPath(mSongListManager.getCurSongPath(), true);
                Intent switchIntent = new Intent();
                switchIntent.setAction(Consts.MY_SWITCH_ACTION);
                sendBroadcast(switchIntent);
            }
        }).start();
    }

    public String getCurSongName(){
        if(mSong == null){
            return "";
        }
        return mSong.getSongName();
    }

    public String getCurSingerName(){
        if(mSong == null){
            return "";
        }
        return mSong.getSingerName();
    }

    public int getCurDuration(){
        if(mSong == null){
            return 0;
        }
        return mSong.getDuration();
    }

    public Bitmap getCurCover(){
        if(mSong == null){
            return null;
        }
        return mSong.getCover();
    }

    public Bitmap getCurBlurCover(int layoutWidth, int layoutHeight){

        Bitmap scaleBitmap = ImageUtil.getScaleImage(getCurCover(), layoutWidth, layoutHeight);
        Bitmap outBitmap = ImageUtil.blurBitmap(scaleBitmap, getApplicationContext());
        scaleBitmap.recycle();
        return outBitmap;
    }
}
