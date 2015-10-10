package com.catchingnow.utils.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Heaven on 9/3/15.
 */
public class JSONObjectAsyncTask extends MyAsyncTask<JSONObject> {
    public JSONObjectAsyncTask(OnFinishListener<JSONObject> onFinishListener) {
        super(onFinishListener);
    }

    @Override
    protected JSONObject convert(byte[] bytes) throws JSONException {
        return new JSONObject(new String(bytes));
    }
}
