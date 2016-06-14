package study.oscar.player.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.mobiwise.library.MusicPlayerView;
import study.oscar.player.R;
import study.oscar.player.manager.RemoteViewManager;
import study.oscar.player.manager.SongListManager;
import study.oscar.player.service.MusicService;
import study.oscar.player.ui.adapter.CoverPagerAdapter;
import study.oscar.player.ui.adapter.SongListAdapter;
import study.oscar.player.ui.control.CoverViewPager;
import study.oscar.player.util.Consts;

public class MainActivity extends Activity implements View.OnClickListener{

    private final static String TAG = "MainActivity";

    private MusicPlayerView mpv = null;
    private MusicService mPlayService = null;
    private RemoteViewManager mRemoteManager = null;
    private TextView mTextViewSong = null;
    private TextView mTextViewSinger = null;
    private ServiceConnection serviceConnection;
    private SongListAdapter mAdapter;

    private Button nextBtn = null;
    private Button prevBtn = null;
    private Button songListBtn = null;

    private CoverViewPager mCoverViewPager = null;
    private CoverPagerAdapter coverPagerAdapter = null;


    private BroadcastReceiver mReceiver;
    private boolean mbShowingAni = false;

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
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initReceiver();
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

        mAdapter = new SongListAdapter(getApplicationContext());

        mSongListHolder = new singListHolder();
        mSongListHolder.playListLayout = (RelativeLayout) findViewById(R.id.play_list);
        mSongListHolder.playListLayout.setVisibility(View.GONE);
        mSongListHolder.titleView = (TextView) findViewById(R.id.playlist_top_title);
        mSongListHolder.songListView = (ListView) findViewById(R.id.playlist_list);
        mSongListHolder.songListView.setAdapter(mAdapter);
        mSongListHolder.closeLayout = (RelativeLayout) findViewById(R.id.playlist_bottom_layout);

        mCoverViewPager = (CoverViewPager)findViewById(R.id.coverViewPager);
        coverPagerAdapter = new CoverPagerAdapter(this);
        mCoverViewPager.setAdapter(coverPagerAdapter);

        mCoverViewPager.addOnPageChangeListener(pageChangeListener);

        mHandlerRotate.postDelayed(mRunnableRotate, ROTATE_DELAY);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "onPageScrollStateChanged:" + state);
            switch (state){
                case ViewPager.SCROLL_STATE_IDLE:
                    if(!isRotating){
                        mHandlerRotate.postDelayed(mRunnableRotate, ROTATE_DELAY);
                    }
                    isRotating = true;
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                case ViewPager.SCROLL_STATE_SETTLING:
                    isRotating = false;
                    break;
            }
        }
    };

    boolean isRotating = true;

    /**
     * Handler will post runnable object every @ROTATE_DELAY seconds.
     */
    private static int ROTATE_DELAY = 20;
    /**
     * Handler for posting runnable object
     */
    private Handler mHandlerRotate = new Handler();

    /**
     * Runnable for turning image (default velocity is 10)
     */
    private Runnable mRunnableRotate = new Runnable() {
        @Override
        public void run() {
            if (isRotating) {
                mCoverViewPager.updateCurrentCoverRotate();
                mHandlerRotate.postDelayed(mRunnableRotate, ROTATE_DELAY);
            }
        }
    };

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
                mAdapter.notifyDataSetChanged();
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
    }

    void initReceiver(){
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();
                if(Consts.MY_SWITCH_ACTION.equals(strAction)){
                    refreshSongInfo();
                    refreshSongBtn();
                    mRemoteManager.refreshSongInfo(true, true);
                    //mpv.invalidate();
                    //mpv.start();
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
            case R.id.show_list:{
                int listHeight = mSongListHolder.playListLayout.getHeight();
                Animation animation = new TranslateAnimation(0, 0, listHeight, 0);
                animation.setDuration(300);
                mSongListHolder.playListLayout.setVisibility(View.VISIBLE);
                mSongListHolder.playListLayout.startAnimation(animation);
                break;
            }
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
            case R.id.playlist_bottom_layout: {
                int listHeight = mSongListHolder.playListLayout.getHeight();
                Animation animation = new TranslateAnimation(0, 0, 0, listHeight);
                animation.setDuration(300);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mbShowingAni = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mbShowingAni = false;
                        mSongListHolder.playListLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mSongListHolder.playListLayout.startAnimation(animation);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRemoteManager.cancelAll();
        unregisterReceiver(mReceiver);
        unbindService(serviceConnection);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
