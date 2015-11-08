package study.oscar.player.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import study.oscar.player.base.SongItem;
import study.oscar.player.manager.MediaPlayerManager;
import study.oscar.player.manager.MediaStoreManager;
import study.oscar.player.util.Consts;
import study.oscar.player.util.FileUtil;

/**
 * Created by oscar on 2015/10/6.
 */
public class MusicPlayService extends IntentService{

    static final String TAG =  "MusicPlayService";

    MediaPlayerManager mPlayManager;
    MusicBinder mBinder = new MusicBinder();

    public MusicPlayService() {
        super("MusicPlayService");
        mPlayManager = MediaPlayerManager.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if(action == null) return;
        if(action.equals("pre")){
            prevSong();
        }else if(action.equals("next")){
            nextSong();
            Intent nextIntent = new Intent();
            nextIntent.setAction(Consts.MY_NEXT_ACTION);
            sendBroadcast(nextIntent);
        }else if(action.equals("pause")){
            //SongListManager.getInstance().
        }
    }

    public class MusicBinder extends Binder{
        public MusicPlayService getService(){
            return MusicPlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean playSong(){
        if(!isLoaded){
            return false;
        }
        mPlayManager.start();
        return true;
    }

    public boolean pauseSong(){
        if(!isLoaded){
            return false;
        }
        mPlayManager.pause();
        return true;
    }
}
