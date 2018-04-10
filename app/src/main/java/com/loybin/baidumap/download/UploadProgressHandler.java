package com.loybin.baidumap.download;

import android.os.Looper;
import android.os.Message;

import com.loybin.baidumap.model.ProgressBean;

/**
 * 创 建 者: LoyBin
 */
public abstract class UploadProgressHandler extends ProgressHandler {

    private static final int UPLOAD_PROGRESS = 0;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());


    @Override
    protected void sendMessage(ProgressBean progressBean) {
        mHandler.obtainMessage(UPLOAD_PROGRESS, progressBean).sendToTarget();

    }


    @Override
    protected void handleMessage(Message message) {
        switch (message.what) {
            case UPLOAD_PROGRESS:
                ProgressBean progressBean = (ProgressBean) message.obj;
                onProgress(progressBean.getBytesRead(), progressBean.getContentLength(), progressBean.isDone());
        }
    }

}
