package study.oscar.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import study.oscar.player.manager.SongListManager;

/**
 * Created by oscar on 2015/9/14.
 */
public class WelcomeActivity extends Activity implements Animation.AnimationListener {
    /** Called when the activity is first created. */

    final static String TAG = "WelcomeActivity";
    private ImageView  imageView = null;
    private AlphaAnimation alphaAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageView = (ImageView)findViewById(R.id.welcome_image_view);

        alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillEnabled(true); //启动Fill保持
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面
        alphaAnimation.setAnimationListener(this);

        new Thread(initDataTask).start();
    }


    Runnable initDataTask = new Runnable() {
        @Override
        public void run() {
            SongListManager.getInstance().loadSongList(getApplicationContext());
            Message msg = new Message();
            animHandler.sendMessage(msg);
        }
    };

    Handler animHandler = new Handler() {
        public void handleMessage(Message msg) {
            imageView.startAnimation(alphaAnimation);
            Log.d(TAG,"alphaAnimation.start()");
            super.handleMessage(msg);
        }
    };


    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG,"onAnimationStart");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG,"onAnimationEnd");
        //动画结束时结束欢迎界面并转到软件的主界面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d(TAG,"onAnimationRepeat");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在欢迎界面屏蔽BACK键
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }
}
