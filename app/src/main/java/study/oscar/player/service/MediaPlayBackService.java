package study.oscar.player.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import study.oscar.player.manager.SongListManager;
import study.oscar.player.util.Consts;

/**
 * Created by oscar on 2015/10/6.
 */
public class MediaPlayBackService extends IntentService{

    static final String TAG =  "MediaPlayBackService";

    public MediaPlayBackService() {
        super("MediaPlayBackService");
    }
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
        if(action == null) return;
        if(action.equals("pre")){
            SongListManager.getInstance().prevSong();
            Intent preIntent = new Intent();
            preIntent.setAction(Consts.MY_PRE_ACTION);
            sendBroadcast(preIntent);
        }else if(action.equals("next")){
            SongListManager.getInstance().nextSong();
            Intent nextIntent = new Intent();
            nextIntent.setAction(Consts.MY_NEXT_ACTION);
            sendBroadcast(nextIntent);
        }else if(action.equals("pause")){
            //SongListManager.getInstance().
        }
    }
}
