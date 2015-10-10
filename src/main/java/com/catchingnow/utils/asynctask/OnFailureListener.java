package com.catchingnow.utils.asynctask;

/**
 * Created by Heaven on 9/13/15.
 */
public interface OnFailureListener {
    int ERR_IOEXCEPTION = 1001;
    int ERR_JSONEXCEPTION = 1002;
    void onFailure(int responseCode);
}
