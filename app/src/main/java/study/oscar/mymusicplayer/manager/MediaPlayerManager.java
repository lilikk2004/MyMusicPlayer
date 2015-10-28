package study.oscar.mymusicplayer.manager;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by oscar on 2015/9/12.
 */
public class MediaPlayerManager {
    static MediaPlayerManager mInstance = null;
    static public MediaPlayerManager getInstance(){
        if(mInstance == null){
            mInstance = new MediaPlayerManager();
        }
        return mInstance;
    }

    MediaPlayer mp = null;

    MediaPlayerManager(){
        mp = new MediaPlayer();
    }

    public boolean setMediaByFilePath(String filePath){
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

    public void start(){
        mp.start();
    }

    public void pause(){
        mp.pause();
    }

    public void stop(){
        mp.pause();
        mp.seekTo(0);
    }

    public void seekTo(int i){
        mp.seekTo(i);
    }

}
