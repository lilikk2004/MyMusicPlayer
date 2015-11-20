package study.oscar.player.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import study.oscar.player.manager.SongListManager;

/**
 * Created by oscar on 2015/11/20.
 */
public class SongListAdapter extends BaseAdapter{

    SongListManager songListManager = null;

    SongListAdapter(){
        songListManager = SongListManager.getInstance();
    }

    @Override
    public int getCount() {
        return songListManager.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
