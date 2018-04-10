package com.loybin.baidumap.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loybin.baidumap.base.BaseProtocol;
import com.loybin.baidumap.ui.fragment.RecommendedsFramgent;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/08 上午10:52
 * 描   述: 推荐故事逻辑
 */
public class RecommendedsPresenter extends BaseProtocol {
    private static final String TAG = "RecommendedFramgent";
    private Context mContext;
    private RecommendedsFramgent mRecommendedFramgent;
    private int mPageId;
    private Gson mGson;
    private String mToJson;

    public RecommendedsPresenter(Context context, RecommendedsFramgent recommendedFramgent) {
        mContext = context;
        mRecommendedFramgent = recommendedFramgent;
    }

    @Override
    public List<Album> loadDataFromNet(int index, String id) {
        LogUtils.e(TAG, "RecommendedFramgent loadDataFromNet" + mToJson);
        if (mToJson == null) {
            loadDataNet(index, id);
            return null;
        }
        return parseData(mToJson, mGson);
    }


    @NonNull
    @Override
    public String getInterfaceKey() {
        return "Recommended";
    }


    /**
     * 获取数据
     *
     * @param page
     */
    public void loadDataNet(final int page, String id) {
        int max = 100;
        int min = 1;
        Random random = new Random();

        int pages = random.nextInt(max) % (max - min + 1) + min;
        LogUtils.d(TAG, "pages  = " + pages);
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, 6 + "");
        map.put(DTransferConstants.CALC_DIMENSION, 3 + "");
        map.put("page", "" + pages);
        map.put("count", "" + 6);
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList object) {
                if (object != null && object.getAlbums() != null) {
                    int totalCount = object.getTotalCount();
                    if (object.getAlbums().size() != 0) {
                        LogUtils.e(TAG, "alba.size" + object.getAlbums().size());
                        mRecommendedFramgent.onSuccess(object.getAlbums(), totalCount);
                    }

                }
            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError " + code + ", " + message);
                mRecommendedFramgent.onError();
            }
        });
    }


    public void initListView(String jsonAlbum, Gson gson) {
        if (jsonAlbum == null || jsonAlbum.equals("")) {
            mRecommendedFramgent.jsonAlbumNull();
        } else {
            List<Album> list = gson.fromJson(jsonAlbum, new TypeToken<List<Album>>() {
            }.getType());
            mRecommendedFramgent.successAlbum(list);
        }
    }
}
