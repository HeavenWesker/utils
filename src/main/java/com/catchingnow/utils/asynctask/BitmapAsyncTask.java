package com.catchingnow.utils.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.catchingnow.utils.bitmap.BitmapUtils;

import org.json.JSONException;

/**
 * Created by Heaven on 9/3/15.
 */
public class BitmapAsyncTask extends MyAsyncTask<Bitmap> {
    public static final int RECTANGLE = 0x0;
    public static final int TRIANGLE = 0x1;
    public static final int RING = 0x2;
    int cropType;

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener) {
        super(onFinishListener);
    }

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, OnProgressListener onProgressListener) {
        super(onFinishListener, onProgressListener);
    }
    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, OnConvertedListener<Bitmap> onConvertedListener) {
        super(onFinishListener, onConvertedListener);
    }

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, OnProgressListener onProgressListener, OnConvertedListener<Bitmap> onConvertedListener) {
        super(onFinishListener, onProgressListener, onConvertedListener);
    }

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, OnProgressListener onProgressListener, int cropType) {
        super(onFinishListener, onProgressListener);
        this.cropType = cropType;
    }

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, int cropType) {
        super(onFinishListener);
        this.cropType = cropType;
    }

    public BitmapAsyncTask(OnFinishListener<Bitmap> onFinishListener, OnProgressListener onProgressListener, OnConvertedListener<Bitmap> onConvertedListener, OnFailureListener onFailureListener) {
        super(onFinishListener, onProgressListener, onConvertedListener, onFailureListener);
    }

    @Override
    protected Bitmap convert(byte[] bytes) throws JSONException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        switch (cropType){
            case RECTANGLE:
                break;
            case TRIANGLE:
                //TODO this is not OK
                break;
            case RING:
                bitmap = BitmapUtils.crop2Round(bitmap);
                break;
        }
        return bitmap;
    }
}
