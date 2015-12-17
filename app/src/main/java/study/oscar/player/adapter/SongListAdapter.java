package study.oscar.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import study.oscar.player.R;
import study.oscar.player.base.SongItem;
import study.oscar.player.manager.SongListManager;

/**
 * Created by oscar on 2015/11/20.
 */
public class SongListAdapter extends BaseAdapter{

    SongListManager songListManager = null;
    Context mContext;

    public class ViewHolder{
        TextView songView;
        TextView singerView;
    }

    public SongListAdapter(Context context){
        songListManager = SongListManager.getInstance();
        mContext = context;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.song_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.songView = (TextView)convertView.findViewById(
                    R.id.item_song_name);
            viewHolder.singerView = (TextView)convertView.findViewById(
                    R.id.item_singer_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        SongItem songItem = songListManager.getSong(position);

        viewHolder.songView.setText(songItem.getSongName());
        viewHolder.singerView.setText(songItem.getSingerName());

        if(position == songListManager.getCurSongIndex()){
            viewHolder.songView.setTextColor(0xFF00C18d);
            viewHolder.singerView.setTextColor(0xFF00815e);
        }else{
            viewHolder.songView.setTextColor(0xFFFFFFFF);
            viewHolder.singerView.setTextColor(0xFFDDDDDD);
        }

        return convertView;
    }
}
