package study.oscar.player.manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by oscar on 2015/9/12.
 */
public class MediaStoreManager {
    final static String TAG = "MediaStoreManager";
    static MediaStoreManager mInstance = null;
    static public MediaStoreManager getInstance(){
        if(mInstance == null){
            mInstance = new MediaStoreManager();
        }
        return mInstance;
    }

    Cursor currentCursor = null;

    /**
     * 通过MP3路径得到指向当前MP3的Cursor
     *
     * @param filePath
     *            MP3路径
     *
     * @return Cursor 返回的Cursor指向当前MP3
     */
    static private Cursor getCursorFromPath(String filePath,Context context) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        // System.out.println(c.getString(c.getColumnIndex("_data")));
        if (c.moveToFirst()) {
            do {
                // 通过Cursor 获取路径，如果路径相同则break；
                path = c.getString(c
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Log.d(TAG, "path:" + path);
                // 查找到相同的路径则返回，此时cursorPosition 便是指向路径所指向的Cursor 便可以返回了
                if (path.equals(filePath)) {
                    // System.out.println("audioPath = " + path);
                    // System.out.println("filePath = " + filePath);
                    // cursorPosition = c.getPosition();
                    break;
                }
            } while (c.moveToNext());
        }
        // 这两个没有什么作用，调试的时候用
        // String audioPath = c.getString(c
        // .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        //
        // System.out.println("audioPath = " + audioPath);
        return c;
    }

    /**
     *
     * 功能 通过album_id查找 album_art 如果找不到返回null
     *
     * @param album_id
     * @return album_art
     */
    static private String getAlbumArt(int album_id,Context context) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

    public void setCursor(String filePath,Context context) {
        currentCursor = getCursorFromPath(filePath, context);
    }

    public void closeCursor() {
        currentCursor.close();
        currentCursor = null;
    }

    public Bitmap getBitmap(Context context){
        int album_id = currentCursor.getInt(currentCursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

        Bitmap bm = null;
        String albumArt = getAlbumArt(album_id, context);
        if (albumArt != null) {
            bm = BitmapFactory.decodeFile(albumArt);
        }

        return bm;
    }

    public String getSongName(){
        return currentCursor.getString(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
    }

    public String getSingerName(){
        return currentCursor.getString(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
    }

    public int getDuration(){
        return currentCursor.getInt(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
    }
}
