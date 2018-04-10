package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.ui.activity.SearchStoryActivity;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/28 下午2:26
 * 描   述: 搜索专辑 逻辑
 */
public class SearchStoryPresenter {
    private static final String TAG = "SearchStoryActivity";
    private Context mContext;
    private SearchStoryActivity mSearchStoryActivity;

    public SearchStoryPresenter(Context context, SearchStoryActivity searchStoryActivity) {
        mContext = context;
        mSearchStoryActivity = searchStoryActivity;
    }


    /**
     * 3.9.1 搜索专辑
     *
     * @param keyword
     */
    public void getSearchedAlbums(String keyword, int pageNum) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        map.put(DTransferConstants.CATEGORY_ID, 6 + "");
        map.put(DTransferConstants.PAGE, pageNum + "");
        map.put(DTransferConstants.PAGE_SIZE, 20 + "");
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                LogUtils.e(TAG, "onSuccess");
                mSearchStoryActivity.dismissLoading();
                int totalCount = searchAlbumList.getTotalCount();
                if (searchAlbumList != null && searchAlbumList.getAlbums() != null) {
                    List<Album> albums = searchAlbumList.getAlbums();
                    mSearchStoryActivity.onSuccess(albums, totalCount);
                }

            }

            @Override
            public void onError(int i, String s) {
                mSearchStoryActivity.dismissLoading();
                LogUtils.e(TAG, "code " + i + "onError " + s);
                mSearchStoryActivity.onError();
            }
        });
    }
}
