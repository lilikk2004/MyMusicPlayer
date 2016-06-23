package study.oscar.player.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by oscar on 2016/6/22.
 */
public class ImageUtil {


    static public Bitmap blurBitmap(Bitmap bitmap,Context context){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        //bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;


    }

    static public Bitmap getScaleImage(Bitmap bitmap,int width,int height){
        if(bitmap == null){
            return null;
        }
        int bitHeight = bitmap.getHeight();
        int bitWidth = bitmap.getWidth();
        int srcWidth, srcHeight = 0;
        if(bitHeight * width > bitWidth * height){
            srcWidth = bitWidth;
            srcHeight = bitWidth * height / width;
        }else{
            srcWidth = bitHeight * width / height;
            srcHeight = bitHeight;
        }

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas imgCanvas = new Canvas(outBitmap);

        Rect srcRect = new Rect((bitWidth - srcWidth) / 2 ,(bitHeight - srcHeight) / 2 ,(bitWidth + srcWidth) / 2 ,(bitHeight + srcHeight) / 2);
        Rect drawRect = new Rect(0, 0, width, height);

        final Paint paint = new Paint();

        imgCanvas.drawBitmap(bitmap, srcRect, drawRect, paint);

        return outBitmap;
    }
}
