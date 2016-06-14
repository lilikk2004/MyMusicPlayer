package study.oscar.player.util;

import android.util.Log;

/**
 * Created by oscar on 2016/6/14.
 */
public class TestUtil {
    static long begin = 0;
    static long end = 0;

    static public void startTime(String TAG, String information) {
        end = System.currentTimeMillis();
        Log.d(TAG, information);
    }

    static public void printTime(String TAG, String information){
        begin = end;
        end = System.currentTimeMillis();
        Log.d(TAG, information + ":" + (end - begin));
    }
}
