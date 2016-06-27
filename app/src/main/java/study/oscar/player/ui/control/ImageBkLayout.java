package study.oscar.player.ui.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by oscar on 2016/6/26.
 */
public class ImageBkLayout extends RelativeLayout {

    public ImageBkLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }
    private final static String TAG = "ImageBkLayout";

    private Bitmap bitmapA = null;
    private Bitmap bitmapB = null;
    private int mode = IMG_NULL;
    private int alphaA = 0;

    final static public int IMG_NULL = 0;
    final static public int IMG_A = 1;
    final static public int IMG_B = 2;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw alphaA:" + alphaA);
        if(mode == IMG_NULL){
            return;
        }
        Paint paint = new Paint();
        if(alphaA == 255){
            if(bitmapA != null) {
                Log.d(TAG, "draw bitmapA");
                canvas.drawBitmap(bitmapA, 0, 0, paint);
            }
            return;
        }
        if(alphaA == 0){
            if(bitmapB != null) {
                Log.d(TAG, "draw bitmapB");
                canvas.drawBitmap(bitmapB, 0, 0, paint);
            }
            return;
        }

        paint.setAlpha(alphaA);
        canvas.drawBitmap(bitmapA, 0, 0, paint);
        paint.setAlpha(255 - alphaA);
        canvas.drawBitmap(bitmapB, 0, 0, paint);
    }

    public void setBitmap(Bitmap bitmap){
        if(mode == IMG_NULL){
            alphaA = 255;
            mode = IMG_A;
            bitmapA = bitmap;
            postInvalidate();
            return;
        }
        if(mode == IMG_A){
            mode = IMG_B;
            bitmapB = bitmap;
            mHandler.postDelayed(mRunnableChange, CHANGE_DELAY);
            return;
        }
        if(mode == IMG_B){
            mode = IMG_A;
            bitmapA = bitmap;
            mHandler.postDelayed(mRunnableChange, CHANGE_DELAY);
            return;
        }
    }


    private final static int CHANGE_DELAY = 20;
    private final static int PER_ALPHA = 15;

    private Handler mHandler = new Handler();

    private Runnable mRunnableChange = new Runnable() {
        @Override
        public void run() {
            if (mode == IMG_B) {
                alphaA -= PER_ALPHA;
                if(alphaA <= 0){
                    alphaA = 0;
                }else {
                    mHandler.postDelayed(mRunnableChange, CHANGE_DELAY);
                }
            }else if(mode == IMG_A){
                alphaA += PER_ALPHA;
                if(alphaA >= 255){
                    alphaA = 255;
                }else {
                    mHandler.postDelayed(mRunnableChange, CHANGE_DELAY);
                }
            }
            postInvalidate();
        }
    };

}
