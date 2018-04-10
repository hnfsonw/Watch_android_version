package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.loybin.baidumap.ui.activity.ListenStoryActivity;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.customized.CustomizedAlbumColumnDetail;

import java.util.HashMap;
import java.util.Map;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/30 下午4:33
 * 描   述: 故事专辑业务
 */
public class ListenStoryPresenter {
    private static final String TAG = "ListenStoryActivity";
    private Context mContext;
    private ListenStoryActivity mListenStoryActivity;
    private boolean mLoading;

    public ListenStoryPresenter(Context context, ListenStoryActivity listenStoryActivity) {
        mContext = context;
        mListenStoryActivity = listenStoryActivity;
    }


    public void loadData() {
//        if (mLoading) {
//            return;
//        }
//        mLoading = true;
//        Map<String, String> param = new HashMap<String, String>();
//        param.put(DTransferConstants.ALBUM_IDS, "7821798,257813,468823,7164095,2726438,260744,327278,267101,2868786");
//        CommonRequest.getBatch(param, new IDataCallBack<BatchAlbumList>() {
//            @Override
//            public void onSuccess(BatchAlbumList object) {
//                mListenStoryActivity.onSuccess(object);
//                mLoading = false;
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.e(TAG, "onError " + code + ", " + message);
//                mListenStoryActivity.onError(code,message);
//                mLoading = false;
//            }
//        });

        if (mLoading) {
            return;
        }
        mLoading = true;
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ID, 838 + "");
        CommonRequest.getCustomizedAlbumColumDetail(map, new IDataCallBack<CustomizedAlbumColumnDetail>() {
            @Override
            public void onSuccess(CustomizedAlbumColumnDetail customizedAlbumColumnDetail) {

                mListenStoryActivity.onSuccess(customizedAlbumColumnDetail);
                mLoading = false;
            }


            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError " + code + ", " + message);
                mListenStoryActivity.onError(code, message);
                mLoading = false;
            }
        });
    }
}
