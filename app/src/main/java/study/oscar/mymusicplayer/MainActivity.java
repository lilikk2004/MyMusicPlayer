package study.oscar.mymusicplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import co.mobiwise.library.MusicPlayerView;
import study.oscar.mymusicplayer.manager.SongListManager;
import study.oscar.mymusicplayer.service.MediaPlayBackService;

public class MainActivity extends Activity implements View.OnClickListener{

    final static String TAG = "MainActivity";

    MusicPlayerView mpv = null;
    SongListManager mSongListManager = null;
    TextView mTextViewSong = null;
    TextView mTextViewSinger = null;

    Button nextBtn = null;
    Button prevBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mTextViewSong = (TextView) findViewById(R.id.textViewSong);
        mTextViewSinger = (TextView) findViewById(R.id.textViewSinger);
        nextBtn = (Button) findViewById(R.id.next);
        prevBtn = (Button) findViewById(R.id.previous);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);

        mSongListManager = SongListManager.getInstance();
        refreshSongInfo();
        refreshSongBtn();


        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mpv.isRotating()) {
                    mSongListManager.pauseSong();
                    mpv.stop();
                }
                else {
                    mSongListManager.playSong();
                    mpv.start();
                }
            }
        });
/*        mPlayButton = (Button)findViewById(R.id.button_play);
        mStopButton = (Button)findViewById(R.id.button_stop);
        mPauseButton = (Button)findViewById(R.id.button_pause);
        mOpenButton = (Button)findViewById(R.id.button_open_file);
        mSongCover = (ImageView)findViewById(R.id.image_cover);
        mPauseButton.setOnClickListener(this);
        mOpenButton.setOnClickListener(this);*/
    }

    void refreshSongInfo(){
        mpv.setBitmap(mSongListManager.getCurCover());
        mTextViewSong.setText(mSongListManager.getCurSongName());
        mTextViewSinger.setText(mSongListManager.getCurSingerName());

        mpv.setProgress(0);
        mpv.setMax(mSongListManager.getCurDuration() / 1000);
    }

    void refreshSongBtn(){
        if(SongListManager.getInstance().hasNext())
            nextBtn.setEnabled(true);
        else
            nextBtn.setEnabled(false);

        if(SongListManager.getInstance().hasPrev())
            prevBtn.setEnabled(true);
        else
            prevBtn.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next:
                mSongListManager.nextSong();
                refreshSongInfo();
                refreshSongBtn();
                mpv.stop();
                break;
            case R.id.previous:
                mSongListManager.prevSong();
                refreshSongInfo();
                refreshSongBtn();
                mpv.stop();
                break;
        }
    }

    private NotificationManager showCustomView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.statusbar);
        //设置按钮事件

        Intent preIntent = new Intent(this,MediaPlayBackService.class);
        preIntent.putExtra("action", "pre");
        PendingIntent prepi = PendingIntent.getService(this, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_prev, prepi);//----设置对应的按钮ID监控

        Intent pauaseOrStartIntent=new Intent(this,MediaPlayBackService.class);
        pauaseOrStartIntent.putExtra("action", "pause");
        PendingIntent pausepi = PendingIntent.getService(this, 1, pauaseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_pause, pausepi);//----设置对应的按钮ID监控

        Intent nextIntent=new Intent(this,MediaPlayBackService.class);
        nextIntent.putExtra("action", "next");
        PendingIntent nextpi = PendingIntent.getService(this, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.statusBar_next, nextpi);//----设置对应的按钮ID监控

/*        Intent closeIntent=new Intent(this,PlayerReceiver.class);
        closeIntent.putExtra("action", "close");
        closeIntent.setAction("com.lttclaw.playerReceiver");
        PendingIntent closepi=PendingIntent.getBroadcast(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close, closepi);*/

        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        builder.setContent(remoteViews).setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setTicker("music is playing");
        Notification notification=builder.build();
        //MyApplication.getInstance().notification=notification;
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
        return manager;
    }
}
