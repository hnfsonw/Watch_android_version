package com.loybin.baidumap.model;

/**
 * Created by  LoyBin
 */
public class ProgressBean {

    private long mBytesRead;
    private long mContentLength;
    private boolean mDone;

    public long getBytesRead() {
        return mBytesRead;
    }


    public void setBytesRead(long bytesRead) {
        this.mBytesRead = bytesRead;
    }


    public long getContentLength() {
        return mContentLength;
    }


    public void setContentLength(long contentLength) {
        this.mContentLength = contentLength;
    }


    public boolean isDone() {
        return mDone;
    }


    public void setDone(boolean done) {
        this.mDone = done;
    }
}
