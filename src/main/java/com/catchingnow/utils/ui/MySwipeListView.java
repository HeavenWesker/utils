package com.catchingnow.utils.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.ref.WeakReference;

/**
 * Created by Heaven on 9/14/15.
 */
public class MySwipeListView extends ListView implements View.OnTouchListener, AbsListView.OnScrollListener, View.OnHoverListener {

    private float startY;
    private static final String TAG = "TextActivity";
    private boolean atTop;
    private boolean atBottom;
    private int scrollState;
    private int what = 0;
    private SwipeDownToRefreshListener swipeDownToRefreshListener;
    private SwipeUpToLoadMoreListener swipeUpToLoadMoreListener;

    @Override
    public boolean onHover(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_HOVER_ENTER:
                what++;
//                event.offsetLocation(0, getScrollY());
                startY = event.getY();
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                if ((atTop && getY() >= 0 && startY - event.getY() < 0) || (atBottom && startY-event.getY() > 0)){
                    setScrollY((int)(startY - event.getY()));
                    return true;
                }
        }
        return false;
    }

    private static class MyHandler extends Handler {
        WeakReference<MySwipeListView> mySwipeListViewWeakReference;

        public MyHandler(MySwipeListView mySwipeListView) {
            super();
            mySwipeListViewWeakReference = new WeakReference<>(mySwipeListView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mySwipeListViewWeakReference != null){
                MySwipeListView mySwipeListView = mySwipeListViewWeakReference.get();
                mySwipeListView.setScrollY(msg.arg1);
            }
        }
    }
    private MyHandler handler = new MyHandler(this);

    public MySwipeListView(Context context) {
        super(context);
    }

    public MySwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        setOnScrollListener(this);
        setOnHoverListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        MyLog.d("ATTOP", atTop()+"");
//        MyLog.d("ATBOTTOM", atBottom()+"");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                what++;
                event.offsetLocation(0, getScrollY());
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                scrollBack();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((atTop() && getY() >= 0 && startY - event.getY() < 0) || (atBottom() && startY-event.getY() > 0)){
                    //TODO THIS IS A BUG
                    setScrollY((int)(startY - event.getY()));
                    return true;
                } else {
//                    event.offsetLocation(0, event.getY()-startY);
                }
        }
        return false;
    }

    private boolean atTop() {
//        MyLog.d("TOP", getChildAt(0).getTop()+"");
        Log.d("TOP", getChildAt(0).getTop()+"");
//        return getChildAt(0).getTop()+getPaddingTop() == 0;
        Log.d( "X",  getChildAt(0).getX()+"");
        Log.d( "Y",  getChildAt(0).getY()+"");
        Log.d( "scrollX",  getChildAt(0).getScrollX()+"");
        Log.d( "scrollY",  getChildAt(0).getScrollY()+"");
//        return getChildAt(0).getTop()+getPaddingTop() == 0;
        return getChildAt(0).getY() == 0;
    }

    private boolean atBottom() {
        return (getLastVisiblePosition() == getAdapter().getCount()-1 &&
                getChildAt(getChildCount()-1).getBottom() <= getHeight());
    }


    private void scrollBack() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                float offset = getScrollY();
                int newWhat = what;
                for (int i = 60; i > 0; i--) {
                    if (newWhat != what){
                        return;
                    }
                    Message message = new Message();
                    message.what = newWhat;
                    message.arg1 = (int) (offset/ 60 *i);
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message message = new Message();
                message.what = newWhat;
                message.arg1 = 0;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0) {
            atTop = true;
        } else {
            atTop = false;
        }
        if (firstVisibleItem + visibleItemCount == totalItemCount){
            atBottom = true;
        } else {
            atBottom = false;
        }
    }
    interface SwipeDownToRefreshListener {
        void onSwipeDown();
    }
    interface SwipeUpToLoadMoreListener {
        void onSwipeUp();
    }

    public SwipeDownToRefreshListener getSwipeDownToRefreshListener() {
        return swipeDownToRefreshListener;
    }

    public void setSwipeDownToRefreshListener(SwipeDownToRefreshListener swipeDownToRefreshListener) {
        this.swipeDownToRefreshListener = swipeDownToRefreshListener;
    }

    public SwipeUpToLoadMoreListener getSwipeUpToLoadMoreListener() {
        return swipeUpToLoadMoreListener;
    }

    public void setSwipeUpToLoadMoreListener(SwipeUpToLoadMoreListener swipeUpToLoadMoreListener) {
        this.swipeUpToLoadMoreListener = swipeUpToLoadMoreListener;
    }
}
