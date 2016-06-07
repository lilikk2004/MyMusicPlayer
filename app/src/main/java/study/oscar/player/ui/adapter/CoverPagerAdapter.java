package study.oscar.player.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import study.oscar.player.base.SongItem;
import study.oscar.player.manager.SongListManager;
import study.oscar.player.ui.control.CoverImg;

/**
 * Created by oscar on 2016/5/7.
 */
public class CoverPagerAdapter extends PagerAdapter {

    SongListManager songListManager = null;
    private List<View> coverViewList = new ArrayList<View>();//view数组
    Context mContext;

    public CoverPagerAdapter(Context context){
        songListManager = SongListManager.getInstance();
        mContext = context;

        for(SongItem song:songListManager.getSongList()){
            Bitmap coverBitmap = song.getCover();
            CoverImg coverImg = new CoverImg(mContext);
            coverImg.setCoverImage(coverBitmap);
            coverViewList.add(coverImg);
        }
    }

    @Override
    public int getCount() {
        return coverViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == object;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(coverViewList.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        container.addView(coverViewList.get(position), 0);//添加页卡
        return coverViewList.get(position);
    }
}
