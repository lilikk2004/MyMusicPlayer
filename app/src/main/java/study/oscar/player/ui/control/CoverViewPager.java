package study.oscar.player.ui.control;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import study.oscar.player.ui.adapter.CoverPagerAdapter;

/**
 * Created by oscar on 2016/6/10.
 */
public class CoverViewPager extends ViewPager {
    final static public String TAG = "CoverViewPager";

    private CoverPagerAdapter mCoverAdapter = null;

    public CoverViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(CoverPagerAdapter adapter) {
        mCoverAdapter = adapter;
        super.setAdapter(adapter);
    }

    public void updateCurrentCoverRotate(){
        int currentIndex = getCurrentItem();
        if(mCoverAdapter == null){
            Log.d(TAG, "Adapter is null");
            return;
        }
        CoverImg coverImg = mCoverAdapter.getCoverItem(currentIndex);
        coverImg.updateCoverRotate();
    }
}
