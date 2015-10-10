package com.catchingnow.utils.file;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Heaven on 9/29/15.
 */
public class RawReader {
    Context context;

    public RawReader(Context context) {
        this.context = context;
    }

    public String read(int id) throws IOException {
        InputStream inputStream = context.getResources().openRawResource(id);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        byte[] bytes = new byte[1024*1024];
        while ((len = inputStream.read(bytes)) > 0){
            byteArrayOutputStream.write(bytes, 0, len);
        }
        return byteArrayOutputStream.toString("UTF-8");
    }
}
