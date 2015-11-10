package study.oscar.player.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

import study.oscar.player.base.SongItem;
import study.oscar.player.manager.SongListManager;
import study.oscar.player.util.Consts;

/**
 * Created by oscar on 2015/10/6.
 */
public class MusicService extends IntentService{

    static final String TAG =  "MusicService";

    MusicBinder mBinder = new MusicBinder();
    SongListManager mSongListManager = null;
    SongItem mSong;

    MediaPlayer mp = null;

    public void seekTo(int i){
        mp.seekTo(i);
    }

    public MusicService() {
        super("MusicService");
        mp = new MediaPlayer();
        mSongListManager = SongListManager.getInstance();
        mSong = mSongListManager.getCurSong();
        setMediaPath(mSongListManager.getCurSongPath());
    }

    public MusicService(String name){
        super(name);
        mp = new MediaPlayer();
        mSongListManager = SongListManager.getInstance();
        mSong = mSongListManager.getCurSong();
        setMediaPath(mSongListManager.getCurSongPath());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if(action == null) return;
        if(action.equals("pre")){
            preSong();
        }else if(action.equals("next")){
            nextSong();
        }else if(action.equals("pause")){
            //SongListManager.getInstance().
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

    public boolean setMediaPath(String filePath){
        try {
            mp.stop();
            mp.reset();
            mp.setDataSource(filePath);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void play(){
        mp.start();
        Intent playIntent = new Intent();
        playIntent.setAction(Consts.MY_PLAY_ACTION);
        sendBroadcast(playIntent);
    }

    public void pause(){
        mp.pause();
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(Consts.MY_PAUSE_ACTION);
        sendBroadcast(pauseIntent);
    }

    public void stop(){
        mp.pause();
        mp.seekTo(0);
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(Consts.MY_STOP_ACTION);
        sendBroadcast(pauseIntent);
    }

    public void preSong(){
        mSongListManager.prevSong();
        mSong = mSongListManager.getCurSong();
        setMediaPath(mSongListManager.getCurSongPath());
        Intent preIntent = new Intent();
        preIntent.setAction(Consts.MY_PRE_ACTION);
        sendBroadcast(preIntent);
    }

    public void nextSong(){
        mSongListManager.nextSong();
        mSong = mSongListManager.getCurSong();
        setMediaPath(mSongListManager.getCurSongPath());
        Intent nextIntent = new Intent();
        nextIntent.setAction(Consts.MY_NEXT_ACTION);
        sendBroadcast(nextIntent);
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
}
