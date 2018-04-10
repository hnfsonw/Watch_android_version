package com.loybin.baidumap.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.loybin.baidumap.base.BaseFragment;
import com.loybin.baidumap.base.BaseProtocol;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.ThreadUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.customized.ColumnAlbumItem;
import com.ximalaya.ting.android.opensdk.model.customized.CustomizedAlbumColumnDetail;
import com.ximalaya.ting.android.opensdk.model.customized.XmCustomizedModelUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/07 下午4:18
 * 描   述:  故事绘本业务
 */
public class StoryBooksPresenter extends BaseProtocol {
    private static final String TAG = "StoryBooksFramgent";
    private Context mContext;
    private BaseFragment mStoryBooksFramgent;
    private int mPageId;
    private Gson mGson;
    private String mToJson;
    private List<Album> mAlba;
    private String mKey;

    public StoryBooksPresenter(Context context, BaseFragment storyBooksFramgent, String key) {
        mContext = context;
        mStoryBooksFramgent = storyBooksFramgent;
        mKey = key;
    }


    public void loadDataNet(final int page, String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ID, id);
        map.put("page", "" + page);
        map.put("count", "" + 30);
        CommonRequest.getCustomizedAlbumColumDetail(map, new IDataCallBack<CustomizedAlbumColumnDetail>() {
            @Override
            public void onSuccess(CustomizedAlbumColumnDetail object) {
                Log.e(TAG, "onSuccess " + (object != null));
                if (object != null && object.getColumnItemses() != null) {
                    int totalCount = object.getTotalCount();
                    LogUtils.d(TAG, "totalCount " + totalCount);
                    List<ColumnAlbumItem> columnItemses = object.getColumnItemses();
                    mAlba = XmCustomizedModelUtil.customizedAlbumListToAlbumList(columnItemses);
                    LogUtils.e(TAG, "alba.size" + mAlba.size());
                    if (mAlba.size() != 0) {
                        //保存数据到本地
                        if (mGson == null) {
                            mGson = new Gson();
                        }
                        mToJson = mGson.toJson(mAlba);
                        LogUtils.e(TAG, "json = " + mToJson);
                        if (page == 1) {
                            saveData(page, mToJson);
                        }
                        mStoryBooksFramgent.onSuccess(mAlba, totalCount);
                    }

                }
            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError " + code + ", " + message);
                mStoryBooksFramgent.onError();
            }
        });
    }


    @Override
    public List<Album> loadDataFromNet(final int page, String id) {
        LogUtils.e(TAG, "loadDataFromNet");

        if (mToJson == null) {
            LogUtils.e(TAG, "mToJson  " + mToJson);
            loadDataNet(page, id);
            return null;
        } else {

            return parseData(mToJson, mGson);
        }

    }


    /**
     * 保存数据
     *
     * @param page
     * @param toJson
     */
    private void saveData(final int page, String toJson) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //保存数据到本地
                saveData2Local(page, mToJson);
                //保存数据到内存中
                saveData2Mem(page, mToJson);
            }
        });
    }


    @NonNull
    @Override
    public String getInterfaceKey() {
        return mKey;
    }
}
