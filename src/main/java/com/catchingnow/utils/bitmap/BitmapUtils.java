package com.catchingnow.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Heaven on 8/28/15.
 */
public class BitmapUtils {
    public static Bitmap crop2Round(Bitmap bitmap){
        Paint paint = new Paint();
        paint.setColor(0xff424242);
        int radius = Math.min(bitmap.getWidth(), bitmap.getHeight())/2;
        Bitmap output = Bitmap.createBitmap(radius*2, radius*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, radius*2, radius*2);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public static Drawable getDrableFromHttp(Context context, String path) throws IOException {
        InputStream inputStream = getInputStream(path);
        return new BitmapDrawable(context.getResources(), inputStream);
    }

    protected static InputStream getInputStream(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    public static Bitmap getBitmapFromHttp(String path) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(getInputStream(path));
        return bitmap;
    }
}
