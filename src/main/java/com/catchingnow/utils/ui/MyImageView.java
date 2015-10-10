package com.catchingnow.utils.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.catchingnow.utils.ImagePool;


/**
 * Created by Heaven on 9/10/15.
 */
public class MyImageView extends FrameLayout {

    ProgressBar progressBar;
    ImageView imageView;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        progressBar = new ProgressBar(context);
        imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams paramsProgressBar = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        paramsProgressBar.gravity = Gravity.CENTER;
        imageView.setAdjustViewBounds(true);
        ((FrameLayout) getRootView()).addView(imageView, params);
        ((FrameLayout) getRootView()).addView(progressBar, paramsProgressBar);
        progressBar.setVisibility(GONE);
    }

    public void setImage(String path){
        ImagePool pool = ImagePool.getInstance();
//        pool.setHolderBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        pool.setErrorBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_notify_error));
        pool.getBitmapFromHTTP(path, imageView, progressBar);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
