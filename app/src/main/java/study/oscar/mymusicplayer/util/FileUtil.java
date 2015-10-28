package study.oscar.mymusicplayer.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by oscar on 2015/9/12.
 */
public class FileUtil {

    final static int QQMUSICENVIR = 0;
    final static int CLOUDENVIR = 1;

    final static int TESTENVIR = CLOUDENVIR;


    final static String QQMusicFolderPath = "/qqmusic/song/";
    final static String cloudMusicFolderPath = "/netease/cloudmusic/Music/";

    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    static public String getExtSDCardPath()
    {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("sdcard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    return path;
                }
            }
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    static public String getMusicFolder(){
        switch (TESTENVIR){
            case QQMUSICENVIR:
                return getExtSDCardPath() + QQMusicFolderPath;
            case CLOUDENVIR:
                return getSDPath() + cloudMusicFolderPath;
        }
        return getExtSDCardPath() + QQMusicFolderPath;
    }
}
