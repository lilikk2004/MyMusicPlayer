package study.oscar.player.manager;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import study.oscar.player.base.SongItem;
import study.oscar.player.util.FileUtil;

/**
 * Created by oscar on 2015/9/13.
 */
public class SongListManager {
    final static String TAG = "SongListManager";

    static SongListManager mInstance = null;
    static public SongListManager getInstance(){
        if(mInstance == null){
            mInstance = new SongListManager();
        }
        return mInstance;
    }

    List<SongItem> songList = new ArrayList<SongItem>();
    String curMusicFolder = FileUtil.getMusicFolder();
    boolean isLoaded = false;
    int mCurSongIndex = 0;
    MediaPlayerManager mPlayManager;

    SongListManager(){
        mPlayManager = MediaPlayerManager.getInstance();
    }

    public String getFolderPath(){
        return curMusicFolder;
    }

    public boolean loadSongList(Context context){
        File file = new File(getFolderPath());
        File[] subFile = file.listFiles();

        if(subFile.length <= 0) return false;

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                SongItem songItem = new SongItem();
                // 判断是否为MP4结尾
                songItem.setFileName(filename);

                MediaStoreManager.getInstance().setCursor(getFolderPath() + filename, context);
                Bitmap cover = MediaStoreManager.getInstance().getBitmap(context);
                String songName = MediaStoreManager.getInstance().getSongName();
                String singerName = MediaStoreManager.getInstance().getSingerName();
                int duration = MediaStoreManager.getInstance().getDuration();
                MediaStoreManager.getInstance().closeCursor();

                songItem.setCover(cover);
                songItem.setSongName(songName);
                songItem.setSingerName(singerName);
                songItem.setDuration(duration);
                songList.add(songItem);
            }
        }

        isLoaded = true;

        mPlayManager.setMediaByFilePath(curMusicFolder + getCurSong().getFileName());

        return true;
    }

    public SongItem getSongPath(int index){
        if(!isLoaded){
            return null;
        }

        if( songList.size() <= 0) return null;

        if(index >= songList.size() || index < 0) return null;

        return songList.get(index);
    }

    public SongItem getCurSong(){
        return getSongPath(mCurSongIndex);
    }

    public void nextSong(){
        if(mCurSongIndex < songList.size() - 1) mCurSongIndex++;
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return;
        mPlayManager.setMediaByFilePath(curMusicFolder + getCurSong().getFileName());
    }

    public void prevSong(){
        if(mCurSongIndex > 0) mCurSongIndex--;
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return;
        mPlayManager.setMediaByFilePath(curMusicFolder + getCurSong().getFileName());
    }

    public boolean hasNext(){
        if( songList.size() <= 0) return false;
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return false;
        return true;
    }

    public boolean hasPrev(){
        if( songList.size() <= 0) return false;
        if(mCurSongIndex >= songList.size() || mCurSongIndex <= 0) return false;
        return true;
    }

    public boolean playSong(){
        if(!isLoaded){
            return false;
        }
        mPlayManager.start();
        return true;
    }

    public boolean pauseSong(){
        if(!isLoaded){
            return false;
        }
        mPlayManager.pause();
        return true;
    }

    public String getCurSongName(){
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return "";
        return getCurSong().getSongName();
    }

    public String getCurSingerName(){
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return "";
        return getCurSong().getSingerName();
    }

    public int getCurDuration(){
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return 0;
        return getCurSong().getDuration();
    }

    public Bitmap getCurCover(){
        if(mCurSongIndex >= songList.size() - 1 || mCurSongIndex < 0) return null;
        return getCurSong().getCover();
    }
}