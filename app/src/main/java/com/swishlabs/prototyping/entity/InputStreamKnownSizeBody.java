package com.swishlabs.prototyping.entity;


import java.io.IOException;
import java.io.InputStream;
import org.apache.http.entity.mime.content.InputStreamBody;

public class InputStreamKnownSizeBody extends InputStreamBody {
    private int len;

    public InputStreamKnownSizeBody(InputStream in, String filename)
    throws IOException {
        super(in, filename);
        len = in.available();
    }

    @Override
    public long getContentLength() {
        return len;
    }

}
