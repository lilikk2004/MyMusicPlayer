package study.oscar.player.ui.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import study.oscar.player.R;
import study.oscar.player.base.SongItem;

/**
 * Created by oscar on 2016/6/1.
 */
public class CoverImg extends View {
    final static public String TAG = "CoverImg";
    Bitmap mPlayImage = null;
    SongItem songItem;
    int mDrawX;
    int mDrawY;

    public SongItem getSongItem() {
        return songItem;
    }

    public void setSongItem(SongItem songItem) {
        this.songItem = songItem;
    }


    private static final int VELOCITY = 1;
    private int mRotateDegrees = 0;
    private static final int SLOW = 2;

    private float mCenterX;
    private float mCenterY;
    private boolean mbMeasured = false;




    public CoverImg(Context context) {
        super(context);
    }


    public CoverImg(Context context,AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    public void makeDrawCover(){
        int width = getWidth();
        int height = getHeight();
        mCenterX = width / 2;
        mCenterY = height / 2;

        int playLength;
        if(width > height){
            playLength = height;
            mDrawX = (width - height) / 2;
            mDrawY = 0;
        }else{
            playLength = width;
            mDrawX = 0;
            mDrawY = (height - width) / 2;
        }

        Rect playDrawRect = new Rect(0, 0, playLength, playLength);
        Rect imgDrawRect = new Rect(playLength / 6, playLength / 6, playLength * 5 / 6, playLength * 5 / 6);

        Bitmap cover = songItem.getCover();

        int bitWidth = cover.getWidth();
        int bitHeight = cover.getHeight();

        Rect imgSrcRect;
        if(bitHeight > bitWidth){
            imgSrcRect = new Rect(0, (bitHeight - bitWidth) / 2, bitWidth, (bitHeight + bitWidth) / 2);
        }else{
            imgSrcRect = new Rect((bitWidth - bitHeight) / 2, 0, (bitWidth + bitHeight) / 2, bitHeight);
        }
        Bitmap play_disc = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play_disc);

        Rect playSrc = new Rect(0, 0, play_disc.getWidth(), play_disc.getHeight());

        mPlayImage = Bitmap.createBitmap(playLength, playLength, Bitmap.Config.ARGB_8888);

        final Paint paint = new Paint();
        //paint.setAntiAlias(true);
        Canvas imgCanvas = new Canvas(mPlayImage);

        imgCanvas.drawBitmap(cover, imgSrcRect, imgDrawRect, paint);
        imgCanvas.drawBitmap(play_disc, playSrc, playDrawRect, paint);

/*        float scaleSize = (float)(1.0) * imageLength / srcW;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleSize, scaleSize);

        mCoverImage = Bitmap.createBitmap(bitmap,0,0,srcW,
                srcH,matrix,true);

        coverSrcRect = new Rect(0 , 0, mCoverImage.getWidth(), mCoverImage.getHeight());*/
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mbMeasured) {
            Log.d(TAG, "onDraw mWidth:" + getWidth() + " mHeight:" + getHeight() + " " + this);
            makeDrawCover();
            mbMeasured = false;
        }

        if(mPlayImage == null){
            return;
        }

        final Paint paint = new Paint();
        //paint.setAntiAlias(true);

        canvas.rotate(mRotateDegrees * (float)1.0 / SLOW, mCenterX, mCenterY);
        canvas.drawBitmap(mPlayImage, mDrawX, mDrawY, paint);
        //canvas.drawBitmap(mPlay_disc, playSrcRect, playDrawRect, paint);

    }

    public void updateCoverRotate() {
        mRotateDegrees += VELOCITY;
        mRotateDegrees = mRotateDegrees % ( 360 * SLOW);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mbMeasured = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
