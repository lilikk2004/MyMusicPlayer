package study.oscar.mymusicplayer.service;

import android.app.IntentService;
import android.content.Intent;

import study.oscar.mymusicplayer.manager.SongListManager;

/**
 * Created by oscar on 2015/10/6.
 */
public class MediaPlayBackService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MediaPlayBackService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if(action.equals("pre")){
            SongListManager.getInstance().prevSong();
        }else if(action.equals("next")){
            SongListManager.getInstance().nextSong();
        }else if(action.equals("pause")){
            //SongListManager.getInstance().
        }
    }
}
