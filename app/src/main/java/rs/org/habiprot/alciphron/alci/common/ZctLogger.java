package rs.org.habiprot.alciphron.alci.common;

import android.util.Log;

/**
 * Created by Rade on 5/5/2015.
 */
public class ZctLogger {

    private String mTag;
    private boolean mDebug;

    public ZctLogger(String tag, Boolean includeTraces) {
        this.mTag = tag;
        this.mDebug = includeTraces;
    }

    public void d(String message) {
        if (mDebug) {
            Log.d(mTag, message == null ? "null" : message);
        }
    }

    public void e(String message) {
        if (mDebug) {
            Log.e(mTag, message == null ? "null" : message);
        }
    }

    public void i(String message) {
        if (mDebug) {
            Log.i(mTag, message == null ? "null" : message);
        }
    }
}
