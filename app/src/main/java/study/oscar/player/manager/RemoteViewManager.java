package study.oscar.player.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import study.oscar.player.R;
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
        builder.setContent(remoteViews).setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setTicker("music is playing");
        mNotification = builder.build();
        mNotifyManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(1, mNotification);
    }

    public void setSongCover(Bitmap bitmap){
        if(mNotification == null || mNotifyManager == null){
            return;
        }
        mNotification.contentView.setImageViewBitmap(R.id.remote_icon, bitmap);
        mNotifyManager.notify(1, mNotification);
    }
}
