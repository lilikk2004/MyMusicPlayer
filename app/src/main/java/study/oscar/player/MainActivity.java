package study.oscar.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.mobiwise.library.MusicPlayerView;
import study.oscar.player.adapter.SongListAdapter;
import study.oscar.player.manager.RemoteViewManager;
import study.oscar.player.manager.SongListManager;
import study.oscar.player.service.MusicService;
import study.oscar.player.util.Consts;

public class MainActivity extends Activity implements View.OnClickListener{

    final static String TAG = "MainActivity";

    MusicPlayerView mpv = null;
    MusicService mPlayService = null;
    RemoteViewManager mRemoteManager = null;
    TextView mTextViewSong = null;
    TextView mTextViewSinger = null;
    ServiceConnection serviceConnection;

    Button nextBtn = null;
    Button prevBtn = null;
    Button songListBtn = null;

    BroadcastReceiver mReceiver;

    class singListHolder{
        RelativeLayout playListLayout;
        TextView titleView;
        ListView songListView;
        RelativeLayout closeLayout;
    }

    singListHolder mSongListHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    void initView(){
        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mTextViewSong = (TextView) findViewById(R.id.textViewSong);
        mTextViewSinger = (TextView) findViewById(R.id.textViewSinger);
        nextBtn = (Button) findViewById(R.id.next);
        prevBtn = (Button) findViewById(R.id.previous);
        songListBtn = (Button) findViewById(R.id.show_list);

        mRemoteManager = RemoteViewManager.getInstance(this);
        mRemoteManager.initView();

        mSongListHolder = new singListHolder();

        mSongListHolder.playListLayout = (RelativeLayout) findViewById(R.id.play_list);
        mSongListHolder.playListLayout.setVisibility(View.GONE);
        mSongListHolder.titleView = (TextView) findViewById(R.id.playlist_top_title);
        mSongListHolder.songListView = (ListView) findViewById(R.id.playlist_list);
        mSongListHolder.songListView.setAdapter(new SongListAdapter(getApplicationContext()));
        mSongListHolder.closeLayout = (RelativeLayout) findViewById(R.id.playlist_bottom_layout);
    }

    void initListener(){
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        songListBtn.setOnClickListener(this);
        mpv.setOnClickListener(this);
        mSongListHolder.closeLayout.setOnClickListener(this);
        mSongListHolder.songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPlayService.switchSong(position);
            }
        });

        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("onServiceConnected", "connected");
                if(service == null){
                    return;
                }
                mPlayService = ((MusicService.MusicBinder)service).getService();
                if(mPlayService != null){
                    refreshSongInfo();
                    refreshSongBtn();
                }
            }
        };
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();
                if(Consts.MY_SWITCH_ACTION.equals(strAction)){
                    refreshSongInfo();
                    refreshSongBtn();
                    mRemoteManager.refreshSongInfo(true, true);
                    mpv.stop();
                    mpv.start();
                }
                else if(Consts.MY_PLAY_ACTION.equals(strAction)){
                    mRemoteManager.refreshSongInfo(true, false);
                    mpv.start();
                }
                else if(Consts.MY_PAUSE_ACTION.equals(strAction)){
                    mRemoteManager.refreshSongInfo(false, false);
                    mpv.stop();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Consts.MY_SWITCH_ACTION);
        filter.addAction(Consts.MY_PLAY_ACTION);
        filter.addAction(Consts.MY_PAUSE_ACTION);
        filter.addAction(Consts.MY_STOP_ACTION);

        registerReceiver(mReceiver,filter);
    }

    void refreshSongInfo(){
        mpv.setBitmap(mPlayService.getCurCover());
        mTextViewSong.setText(mPlayService.getCurSongName());
        mTextViewSinger.setText(mPlayService.getCurSingerName());

        mpv.setProgress(0);
        mpv.setMax(mPlayService.getCurDuration() / 1000);
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
                mPlayService.nextSong();
                break;
            case R.id.previous:
                mPlayService.preSong();
                break;
            case R.id.show_list:
                mSongListHolder.playListLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.mpv:
                if(mPlayService == null){
                    return;
                }
                if (mpv.isRotating()) {
                    mPlayService.pause();
                } else {
                    mPlayService.play();
                }
                break;
            case R.id.playlist_bottom_layout:
                mSongListHolder.playListLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRemoteManager.cancelAll();
        unregisterReceiver(mReceiver);
    }
}
