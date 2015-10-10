package com.catchingnow.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.catchingnow.utils.asynctask.BitmapAsyncTask;
import com.catchingnow.utils.asynctask.OnConvertedListener;
import com.catchingnow.utils.asynctask.OnFailureListener;
import com.catchingnow.utils.asynctask.OnFinishListener;
import com.catchingnow.utils.asynctask.OnProgressListener;
import com.catchingnow.utils.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * Created by Heaven on 9/9/15.
 */
public class ImagePool {

    private static final String TAG = "ImagePool";
    private static long cacheTime = 432000000;
    private Bitmap errorBitmap;
    private Bitmap holderBitmap;
    private final String cacheDirPath;

    private static ImagePool pool;
    private static boolean mounted;
    private final File cacheDir;

    public Bitmap getHolderBitmap() {
        return holderBitmap;
    }

    public void setHolderBitmap(Bitmap holderBitmap) {
        this.holderBitmap = holderBitmap;
    }

    public static long getCacheTime() {
        return cacheTime;
    }

    public static void setCacheTime(long cacheTime) {
        ImagePool.cacheTime = cacheTime;
    }

    private ImagePool() {
        if(mounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cacheDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ Config.name+"/images/";
            cacheDir = new File(cacheDirPath);
        } else {
            cacheDirPath = null;
            cacheDir = null;
        }
    }
    public static ImagePool getInstance(){
        if (pool == null){
            pool = new ImagePool();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long nowTime = new Date().getTime();
                    if (pool.cacheDirPath != null && pool.cacheDir != null){
                        try {
                            for (File file : pool.cacheDir.listFiles()){
                                if (nowTime - file.lastModified() > cacheTime){
                                    Log.d("TIMESTAMP", nowTime+"-->"+file.lastModified());
                                    file.delete();
                                }
                            }

                        } catch (NullPointerException e){
                            Log.e(TAG, "Permission Denial");
                        }
                    }
                }
            }).start();
        }
        return pool;
    }

    public Bitmap getErrorBitmap() {
        return errorBitmap;
    }

    public void setErrorBitmap(Bitmap errorBitmap) {
        this.errorBitmap = errorBitmap;
    }

    public void getBitmapFromHTTP(String path, final ImageView imageView, final ProgressBar progressBar){
        try {
            final String hashedImageUri = MyDigest.MD5(path);
            final String cachedImagePath = cacheDirPath+hashedImageUri;
            //TODO THIS IS TEST FUNC
            imageView.setTag(hashedImageUri);
            if (progressBar != null){
                progressBar.setVisibility(View.VISIBLE);
            }
            if (imageView != null){
                imageView.setImageBitmap(holderBitmap);
            }
            if (mounted && new File(cachedImagePath).exists()){
                imageView.setImageBitmap(BitmapFactory.decodeFile(cachedImagePath));
                progressBar.setVisibility(View.GONE);
            } else {
                BitmapAsyncTask asyncTask = new BitmapAsyncTask(
                        new OnFinishListener<Bitmap>() {
                            @Override
                            public void onFinish(Bitmap bitmap) {
                                //TODO make sure this
                                assert bitmap != null;
                                if (imageView != null && imageView.getTag().toString().equals(hashedImageUri)) {
                                    Log.d(TAG, hashedImageUri);
                                    Log.d(TAG, imageView.getTag().toString());
                                    imageView.setImageBitmap(bitmap);
                                }
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        },
                        new OnProgressListener() {
                            @Override
                            public void onProgressUpdate(Integer progress) {
                                if (progressBar == null) {
                                    return;
                                }
                                progressBar.setIndeterminate(false);
                                progressBar.setProgress(progress);
                                Log.d("Progress:", "" + progress);
                            }
                        },
                        new OnConvertedListener<Bitmap>() {
                            @Override
                            public void onConverted(final Bitmap bitmap) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (bitmap != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                                                FileOutputStream outputStream = new FileOutputStream(cachedImagePath);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                            }
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(int responseCode) {
                                if (imageView != null && imageView.getTag().toString().equals(hashedImageUri)) {
                                    imageView.setImageBitmap(pool.getErrorBitmap());
                                }
                                if (progressBar != null/* && imageView.getTag().toString().equals(hashedImageUri)*/) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                );
                asyncTask.execute(path);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
