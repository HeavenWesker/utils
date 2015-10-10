package com.catchingnow.utils.asynctask;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Heaven on 9/3/15.
 */
public abstract class MyAsyncTask<T> extends AsyncTask<String, Integer, T> {

    OnFailureListener onFailureListener;
    OnFinishListener<T> onFinishListener;
    OnConvertedListener<T> onConvertedListener;
    OnProgressListener onProgressListener;
    int statusCode;

    public MyAsyncTask(OnFinishListener<T> onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public MyAsyncTask(OnFinishListener<T> onFinishListener, OnProgressListener onProgressListener) {
        this(onFinishListener);
        this.onProgressListener = onProgressListener;
    }

    public MyAsyncTask(OnFinishListener<T> onFinishListener, OnConvertedListener<T> onConvertedListener) {
        this(onFinishListener);
        this.onConvertedListener =  onConvertedListener;
    }

    public MyAsyncTask(OnFinishListener<T> onFinishListener, OnProgressListener onProgressListener, OnConvertedListener<T> onConvertedListener) {
        this(onFinishListener, onProgressListener);
//        this.onProgressListener = onProgressListener;
        this.onConvertedListener =  onConvertedListener;
    }
    public MyAsyncTask(OnFinishListener<T> onFinishListener, OnProgressListener onProgressListener, OnConvertedListener<T> onConvertedListener, OnFailureListener onFailureListener) {
        this(onFinishListener, onProgressListener, onConvertedListener);
        this.onFailureListener = onFailureListener;
    }

    @Override
    protected T doInBackground(String... params) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            int length = connection.getContentLength();
            if (connection.getResponseCode() == 200){
                InputStream inputStream = connection.getInputStream();
                byte[] bytes = new byte[1024];
                int currentLen = 0;
                int len;
                while ((len = inputStream.read(bytes)) > 0){
                    byteArrayOutputStream.write(bytes, 0, len);
                    currentLen+= len;
                    publishProgress((int) (currentLen/(length*1.0) * 100));
                }
                T t = convert(byteArrayOutputStream.toByteArray());
                if (onConvertedListener != null){
                    onConvertedListener.onConverted(t);
                }
                return t;
            }else {
//                if (onFailureListener != null){
//                    onFailureListener.onFailure(connection.getResponseCode());
//                }
                statusCode = connection.getResponseCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            if (onFailureListener != null){
//                onFailureListener.onFailure(OnFailureListener.ERR_IOEXCEPTION);
//            }
            statusCode = OnFailureListener.ERR_IOEXCEPTION;
//            cancel(true);
        } catch (JSONException e) {
            e.printStackTrace();
//            if (onFailureListener != null){
//                onFailureListener.onFailure(OnFailureListener.ERR_JSONEXCEPTION);
//            }
            statusCode = OnFailureListener.ERR_JSONEXCEPTION;
//            cancel(true);
        }

        return null;
//        try {
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    protected abstract T convert(byte[] bytes) throws JSONException;

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (statusCode != 0){
            if (onFailureListener != null){
                onFailureListener.onFailure(statusCode);
            }
            return;
        }
        if (onFinishListener == null){
//            throw new RuntimeException("ProgressListener is null");
            return;
        }
        onFinishListener.onFinish(t);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (onProgressListener == null){
            return;
        }
        onProgressListener.onProgressUpdate(values[0]);
    }
}
