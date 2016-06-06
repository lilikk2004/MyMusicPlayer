package study.oscar.player.ui.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import study.oscar.player.R;

/**
 * Created by oscar on 2016/6/1.
 */
public class CoverImg extends View {
    Bitmap mCoverImage;
    final static private int PLAY_IMG_LENGTH = 360;
    final static private int PLAY_COVER_LENGTH = 240;
    final static private Rect PLAY_IMG_RECT = new Rect(0, 0, PLAY_IMG_LENGTH, PLAY_IMG_LENGTH);
    final static private Rect PLAY_COVER_RECT = new Rect( (PLAY_IMG_LENGTH - PLAY_COVER_LENGTH) / 2,
                                                                (PLAY_IMG_LENGTH - PLAY_COVER_LENGTH) / 2,
                                                                (PLAY_IMG_LENGTH + PLAY_COVER_LENGTH) / 2,
                                                                (PLAY_IMG_LENGTH + PLAY_COVER_LENGTH) / 2);

    public Bitmap getCoverImage() {
        return mCoverImage;
    }
/*
    public Bitmap getCoverBitmap(Bitmap bitmap) {
        Bitmap play_disc = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play_disc);

        Bitmap output = Bitmap.createBitmap(PLAY_IMG_LENGTH,
                PLAY_IMG_LENGTH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        //paint.setAntiAlias(true);
        paint.setColor(color);

        Bitmap roundBitmap = getRoundBitmap(bitmap);


        canvas.drawBitmap(play_disc, new Rect(0, 0, play_disc.getWidth(), play_disc.getHeight()), PLAY_IMG_RECT, paint);
        if(roundBitmap != null) {
            canvas.drawBitmap(roundBitmap, new Rect(0, 0, roundBitmap.getWidth(), roundBitmap.getHeight()), PLAY_COVER_RECT, paint);
        }
        return output;
    }

    public Bitmap getRoundBitmap(Bitmap bitmap){
        if(bitmap == null){
            return null;
        }
        int outLength;
        Rect imgRect;
        Rect srcRect;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if(height > width){
            outLength = width;
            imgRect = new Rect(0, 0, width, width);
            srcRect = new Rect((width - height) / 2, 0, (width + height) / 2, height);
        }else{
            outLength = width;
            imgRect = new Rect(0, 0, height, height);
            srcRect = new Rect((width - height) / 2, 0, (width + height) / 2, height);
        }

        Bitmap roundImage = Bitmap.createBitmap(outLength, outLength, Bitmap.Config.ARGB_8888);
        Canvas imageCanvas = new Canvas(roundImage);
        imageCanvas.drawARGB(0, 0, 0, 0);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        //paint.setAntiAlias(true);
        paint.setColor(color);

        final RectF rectF = new RectF(imgRect);
        imageCanvas.drawRoundRect(rectF, outLength / 2, outLength / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageCanvas.drawBitmap(bitmap, srcRect, imgRect, paint);

        return roundImage;
    }*/

    public void setCoverImage(Bitmap coverImage) {
        this.mCoverImage = coverImage;
    }


    public CoverImg(Context context) {
        super(context);
    }


    public CoverImg(Context context,AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mCoverImage == null){
            return;
        }

        int width = getWidth();
        int height = getHeight();

        Rect imageRect, playDiscRect;
        if(width > height){
            playDiscRect = new Rect((width - height) / 2, 0, (width + height) / 2, height);
            int imageLength = height * 2 / 3;
            imageRect = new Rect((width - imageLength) / 2,
                    (height - imageLength) / 2,
                    (width + imageLength) / 2,
                    (height + imageLength) / 2);
        }else{
            playDiscRect = new Rect(0, (height - width) / 2, width, (height + width) / 2);
            int imageLength = width * 2 / 3;
            imageRect = new Rect((width - imageLength) / 2,
                    (height - imageLength) / 2,
                    (width + imageLength) / 2,
                    (height + imageLength) / 2);
        }

        int bitWidth = mCoverImage.getWidth();
        int bitHeight = mCoverImage.getHeight();

        Rect srcSquare ;
        if(bitHeight > bitWidth){
            srcSquare = new Rect(0, (bitHeight - bitWidth) / 2, bitWidth, (bitHeight + bitWidth) / 2);
        }else{
            srcSquare = new Rect((bitWidth - bitHeight) / 2, 0, (bitWidth + bitHeight) / 2, bitHeight);
        }

        final int color = 0xff424242;
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap play_disc = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play_disc);

        canvas.drawBitmap(mCoverImage, srcSquare, imageRect, paint);
        canvas.drawBitmap(play_disc, new Rect(0, 0, play_disc.getWidth(), play_disc.getHeight()), playDiscRect, paint);

    }
}
