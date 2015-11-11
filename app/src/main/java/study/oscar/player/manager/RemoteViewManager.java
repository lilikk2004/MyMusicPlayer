package study.oscar.player.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import study.oscar.player.R;
import study.oscar.player.base.SongItem;
import study.oscar.player.service.MusicService;

/**
 * Created by oscar on 2015/11/4.
 */
public class RemoteViewManager {
    final static String TAG = "RemoteViewManager";

    static RemoteViewManager mInstance = null;
    static public RemoteViewManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new RemoteViewManager(context);
        }
        return mInstance;
    }

    Context mContext;
    Notification mNotification = null;
    NotificationManager mNotifyManager = null;

    RemoteViewManager(Context context){
        mContext = context;
    }

    public void initView() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.statusbar);
        //设置按钮事件
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        Intent preIntent = new Intent(mContext,MusicService.class);
        preIntent.putExtra("action", "pre");
        PendingIntent prePi = PendingIntent.getService(mContext, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_prev, prePi);//----设置对应的按钮ID监控

        Intent pauseOrStartIntent=new Intent(mContext,MusicService.class);
        pauseOrStartIntent.putExtra("action", "pause");
        PendingIntent pausePi = PendingIntent.getService(mContext, 1, pauseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_pause, pausePi);//----设置对应的按钮ID监控

        Intent nextIntent=new Intent(mContext,MusicService.class);
        nextIntent.putExtra("action", "next");
        PendingIntent nextPi = PendingIntent.getService(mContext, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_next, nextPi);//----设置对应的按钮ID监控

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                //.setStyle(inboxStyle)
                .setTicker("music is playing");
        mNotification = builder.build();
        mNotification.bigContentView = remoteViews;
        mNotifyManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(1, mNotification);
    }

    public void refreshSongInfo(){
        if(mNotification == null || mNotifyManager == null){
            return;
        }
        SongItem item = SongListManager.getInstance().getCurSong();
        mNotification.contentView.setImageViewBitmap(R.id.remote_cover, item.getCover());
        mNotification.contentView.setTextViewText(R.id.remote_song_name, item.getSongName());
        mNotification.contentView.setTextViewText(R.id.remote_singer_name, item.getSingerName());
        mNotifyManager.notify(1, mNotification);
    }

    public void cancelAll(){
        if(mNotification == null || mNotifyManager == null){
            return;
        }
        mNotifyManager.cancelAll();
        mNotification = null;
        mNotifyManager = null;
    }
}
