package study.oscar.player.manager;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import study.oscar.player.base.SongItem;
import study.oscar.player.util.FileUtil;

/**
 * Created by oscar on 2015/11/8.
 */
public class SongListManager {
    final static String TAG = "RemoteViewManager";
    static SongListManager mInstance = null;
    static public SongListManager getInstance(){
        if(mInstance == null){
            mInstance = new SongListManager();
        }
        return mInstance;
    }

    int mCurSongIndex = 0;
    boolean mbLoaded = false;
    List<SongItem> songList = new ArrayList<SongItem>();
    String curMusicFolder = FileUtil.getMusicFolder();

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

        mbLoaded = true;

        return true;
    }

    boolean isLoaded(){
        return mbLoaded;
    }

    public SongItem getSong(int index){
        if(!mbLoaded){
            return null;
        }

        if( songList.size() <= 0) return null;

        if(index >= songList.size() || index < 0) return null;

        return songList.get(index);
    }

    public SongItem getCurSong(){
        return getSong(mCurSongIndex);
    }

    public String getCurSongPath(){
        return curMusicFolder + getSong(mCurSongIndex).getFileName();
    }

    public SongItem nextSong(){
        if(hasNext()) mCurSongIndex++;
        return getCurSong();
    }

    public SongItem prevSong(){
        if(hasPrev()) mCurSongIndex--;
        return getCurSong();
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
}
