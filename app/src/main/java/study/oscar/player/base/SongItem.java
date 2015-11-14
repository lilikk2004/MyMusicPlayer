package study.oscar.player.base;

import android.graphics.Bitmap;

/**
 * Created by oscar on 2015/9/14.
 */
public class SongItem {

    String fileName;
    String songName;
    String singerName;
    int duration;
    Bitmap cover = null;
    Bitmap bigRemoteCover = null;
    Bitmap normalRemoteCover = null;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String sinegerName) {
        this.singerName = sinegerName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public Bitmap getBigRemoteCover() {
        return bigRemoteCover;
    }

    public void setBigRemoteCover(Bitmap bigRemoteCover) {
        this.bigRemoteCover = bigRemoteCover;
    }

    public Bitmap getNormalRemoteCover() {
        return normalRemoteCover;
    }

    public void setNormalRemoteCover(Bitmap normalRemoteCover) {
        this.normalRemoteCover = normalRemoteCover;
    }
}
