package study.oscar.player.ui.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    Bitmap mCoverImage = null;
    Bitmap mPlay_disc = null;
    SongItem songItem;

    Rect coverSrcRect = null;
    Rect coverDrawRect = null;
    Rect playSrcRect = null;
    Rect playDrawRect = null;

    public SongItem getSongItem() {
        return songItem;
    }

    public void setSongItem(SongItem songItem) {
        this.songItem = songItem;
    }


    /**
     * mRotateDegrees count increase 1 by 1 default.
     * I used that parameter as velocity.
     */
    private static int VELOCITY = 1;

    /**
     * Cover image is rotating. That is why we hold that value.
     */
    private int mRotateDegrees = 0;

    /**
     * Center values for cover image.
     */
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
        int imageLength
        if(width > height){
            playDrawRect = new Rect((width - height) / 2, 0, (width + height) / 2, height);
            imageLength = height * 2 / 3;
            coverDrawRect = new Rect((width - imageLength) / 2,
                    (height - imageLength) / 2,
                    (width + imageLength) / 2,
                    (height + imageLength) / 2);
        }else{
            playDrawRect = new Rect(0, (height - width) / 2, width, (height + width) / 2);
            imageLength = width * 2 / 3;
            coverDrawRect = new Rect((width - imageLength) / 2,
                    (height - imageLength) / 2,
                    (width + imageLength) / 2,
                    (height + imageLength) / 2);
        }

        Bitmap cover = songItem.getCover();

        int bitWidth = cover.getWidth();
        int bitHeight = cover.getHeight();

        int srcX,srcY,srcW,srcH;
        if(bitHeight > bitWidth){
            srcX = 0;
            srcY = (bitHeight - bitWidth) / 2;
            srcW = bitWidth;
            srcH = bitWidth;
        }else{
            srcX = (bitWidth - bitHeight) / 2;
            srcY = 0;
            srcW = bitHeight;
            srcH = bitHeight;
        }
        mPlay_disc = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play_disc);
        playSrcRect = new Rect(0 , 0, mPlay_disc.getWidth(), mPlay_disc.getHeight());

        Bitmap bitmap = Bitmap.createBitmap(cover, srcX, srcY, srcW ,srcH);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mbMeasured) {
            Log.d(TAG, "onDraw mWidth:" + getWidth() + " mHeight:" + getHeight() + " " + this);
            makeDrawCover();
            mbMeasured = false;
        }

        if(mCoverImage == null || mPlay_disc == null){
            return;
        }

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.rotate(mRotateDegrees, mCenterX, mCenterY);
        canvas.drawBitmap(mCoverImage, coverSrcRect, coverDrawRect, paint);
        canvas.drawBitmap(mPlay_disc, playSrcRect, playDrawRect, paint);

    }

    public void updateCoverRotate() {
        mRotateDegrees += VELOCITY;
        mRotateDegrees = mRotateDegrees % 360;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mbMeasured = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
