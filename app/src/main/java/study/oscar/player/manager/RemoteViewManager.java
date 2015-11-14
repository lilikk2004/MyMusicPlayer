package study.oscar.player.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    public RemoteViews getBigView(){
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.big_remote_view);

        Intent preIntent = new Intent(mContext,MusicService.class);
        preIntent.putExtra("action", "pre");
        PendingIntent prePi = PendingIntent.getService(mContext, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.big_remote_prev, prePi);//----设置对应的按钮ID监控

        Intent pauseOrStartIntent=new Intent(mContext,MusicService.class);
        pauseOrStartIntent.putExtra("action", "play");
        PendingIntent playPi = PendingIntent.getService(mContext, 1, pauseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.big_remote_play, playPi);//----设置对应的按钮ID监控

        Intent nextIntent=new Intent(mContext,MusicService.class);
        nextIntent.putExtra("action", "next");
        PendingIntent nextPi = PendingIntent.getService(mContext, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.big_remote_next, nextPi);//----设置对应的按钮ID监控


        SongItem item = SongListManager.getInstance().getCurSong();
        remoteViews.setImageViewBitmap(R.id.big_remote_cover, item.getBigRemoteCover());
        remoteViews.setTextViewText(R.id.big_remote_song, item.getSongName());
        remoteViews.setTextViewText(R.id.big_remote_singer, item.getSingerName());

        return remoteViews;
    }

    public RemoteViews getNormalView(){
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.normal_remote_view);

        Intent pauseOrStartIntent=new Intent(mContext,MusicService.class);
        pauseOrStartIntent.putExtra("action", "play");
        PendingIntent playPi = PendingIntent.getService(mContext, 1, pauseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.normal_remote_play, playPi);//----设置对应的按钮ID监控

        Intent nextIntent=new Intent(mContext,MusicService.class);
        nextIntent.putExtra("action", "next");
        PendingIntent nextPi = PendingIntent.getService(mContext, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.normal_remote_next, nextPi);//----设置对应的按钮ID监控


        SongItem item = SongListManager.getInstance().getCurSong();
        remoteViews.setImageViewBitmap(R.id.normal_remote_cover, item.getNormalRemoteCover());
        remoteViews.setTextViewText(R.id.normal_remote_song, item.getSongName());
        remoteViews.setTextViewText(R.id.normal_remote_singer, item.getSingerName());

        return remoteViews;
    }

    public void initView() {

        RemoteViews bigRemoteView = getBigView();
        RemoteViews normalView = getNormalView();

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContent(normalView)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                        //.setStyle(inboxStyle)
                .setPriority(Notification.PRIORITY_MAX)
                .setTicker("music is playing");
        mNotification = builder.build();

        mNotification.bigContentView = bigRemoteView;
        mNotifyManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(1, mNotification);
    }

    public void refreshSongInfo(boolean isPlaying,boolean bFresh){
        if(mNotification == null || mNotifyManager == null){
            return;
        }
        if(mNotification.bigContentView == null){
            return;
        }

        if(isPlaying){
            mNotification.bigContentView.setImageViewResource(R.id.big_remote_play, android.R.drawable.ic_media_pause);
            mNotification.contentView.setImageViewResource(R.id.normal_remote_play, android.R.drawable.ic_media_pause);
        }else {
            mNotification.bigContentView.setImageViewResource(R.id.big_remote_play, android.R.drawable.ic_media_play);
            mNotification.contentView.setImageViewResource(R.id.normal_remote_play, android.R.drawable.ic_media_play);
        }
        if(bFresh) {
            SongItem item = SongListManager.getInstance().getCurSong();

            mNotification.bigContentView.setImageViewBitmap(R.id.big_remote_cover, item.getBigRemoteCover());
            mNotification.bigContentView.setTextViewText(R.id.big_remote_song, item.getSongName());
            mNotification.bigContentView.setTextViewText(R.id.big_remote_singer, item.getSingerName());
            mNotification.contentView.setImageViewBitmap(R.id.normal_remote_cover, item.getNormalRemoteCover());
            mNotification.contentView.setTextViewText(R.id.normal_remote_song, item.getSongName());
            mNotification.contentView.setTextViewText(R.id.normal_remote_singer, item.getSingerName());
        }
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
