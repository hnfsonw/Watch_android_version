package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.WifiModel;
import com.loybin.baidumap.presenter.SearchStoryPresenter;
import com.loybin.baidumap.ui.adapter.SearchStoryAdapter;
import com.loybin.baidumap.ui.view.DefaultFooter;
import com.loybin.baidumap.ui.view.SpringView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/17 上午10:14
 * 描   述: 搜索故事专辑界面
 */
public class SearchStoryActivity extends BaseActivity implements AdapterView.OnItemClickListener, TextWatcher, SpringView.OnFreshListener {


    private static final String TAG = "SearchStoryActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_search)
    AutoCompleteTextView mKeyWorldsView;

    @BindView(R.id.tv_search)
    TextView mTvSearch;

    @BindView(R.id.listview)
    ListView mListview;

    @BindView(R.id.springview)
    SpringView mSpringView;

    public SharedPreferences mGlobalvariable;
    private String mCict;
    private List<Album> mAlbums;
    private String mAddress;
    private List<HashMap<String, Object>> mListString;
    private SearchStoryPresenter mSearchStoryPresenter;
    private int mPage = 1;
    private SearchStoryAdapter mSearchStoryAdapter;
    private int mTotalCount;
    private Intent mIntent;
    private Gson mGson;
    private boolean isId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }


    @Override
    protected void init() {
        if (mSearchStoryPresenter == null) {
            mSearchStoryPresenter = new SearchStoryPresenter(this, this);
        }
        ButterKnife.bind(this);
        mAlbums = new ArrayList<>();

        initView();
        initListener();
        initData();
    }


    private void initData() {
        mSearchStoryAdapter = new SearchStoryAdapter(this, mAlbums);
        mListview.setAdapter(mSearchStoryAdapter);

    }


    private void initListener() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setFooter(new DefaultFooter(this));
        mKeyWorldsView.addTextChangedListener(this);
        mListview.setOnItemClickListener(this);

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.search_album));
        mKeyWorldsView.setHint(getString(R.string.search_story_album));
    }


    @OnClick({R.id.iv_back, R.id.tv_search, R.id.et_search})
    public void onViewClicked(View view) {
        String keyword = mKeyWorldsView.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_search:
                mAlbums.clear();
                mPage = 1;
                searchCh(mAddress);
                break;

            case R.id.et_search:
                LogUtils.e(TAG, "点击了输入框");
                break;
        }
    }


    private void searchCh(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            printn(getString(R.string.please_enter_your_search_address));
            return;
        }
        showLoading("",mContext);

        mSearchStoryPresenter.getSearchedAlbums(keyword, mPage);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LogUtils.e(TAG, "onItemClick" + mAlbums.get(position).getId());
            if (mIntent == null) {
                mIntent = new Intent(this, StoryListActivity.class);
            }
            Album album = mAlbums.get(position);
            mIntent.putExtra("id", album.getId());
            mIntent.putExtra("title", album.getAlbumTitle());
            mIntent.putExtra("imageUrl", album.getCoverUrlLarge());
            mIntent.putExtra("intro", album.getAlbumIntro());
            mIntent.putExtra("count", album.getIncludeTrackCount() + "");
            startActivity(mIntent);

            //保存搜索的点击记录
            saveAlbums(mAlbums.get(position));
        } catch (Exception e) {
            LogUtils.e(TAG, "onItemClick 异常 " + e.getMessage());
        }

    }

    /**
     * 保存搜索的点击专辑
     *
     * @param album
     */
    private void saveAlbums(final Album album) {
        if (mGson == null) {
            mGson = new Gson();
        }
        isId = false;
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //先去本地查看 是否有保存
                String jsonAlbum = loadDataFromLocal("Recommended.Album", Constant.PROTOCOL_TIMEOUT_MONTH);
                if (jsonAlbum == null) {
                    LogUtils.e(TAG, "jsonAlbum == null");
                    //第一次 创新新的保存
                    List<Album> alba = new ArrayList<>();
                    alba.add(album);
                    String toJson = mGson.toJson(alba);
                    saveData2Local("Recommended.Album", toJson);
                    EventBus.getDefault().post(new WifiModel());

                } else {
                    LogUtils.e(TAG, "jsonAlbum != null ");
                    List<Album> list = mGson.fromJson(jsonAlbum, new TypeToken<List<Album>>() {
                    }.getType());
                    LogUtils.d(TAG, "Album " + list.size());
                    for (Album albumList : list) {
                        if (albumList.getId() == album.getId()) {
                            isId = true;
                            return;
                        }
                        LogUtils.d(TAG, "getAlbumTitle " + albumList.getAlbumTitle());
                    }


                    if (!isId) {
                        list.add(album);
                        String toJson = mGson.toJson(list);
                        LogUtils.e(TAG, "toJson " + toJson);
                        saveData2Local("Recommended.Album", toJson);
                        EventBus.getDefault().post(new WifiModel());
                    }

                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAddress = s.toString().trim();

        LogUtils.e(TAG, "" + mAddress);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public void onSuccess(List<Album> albums, int totalCount) {
        if (totalCount < 1) {
            printn(getString(R.string.no_story));
            return;
        } else {
            mSpringView.onFinishFreshAndLoad();
            mTotalCount = totalCount;
            mPage++;
            if (mAlbums.size() < 0) {
                //第一次加载
                mAlbums = albums;
                mSearchStoryAdapter.setData(mAlbums);
                mSearchStoryAdapter.notifyDataSetChanged();
            } else {
                mAlbums.addAll(albums);
                mSearchStoryAdapter.setData(mAlbums);
                mSearchStoryAdapter.notifyDataSetChanged();
                mListview.smoothScrollToPosition(mAlbums.size() - 18);
            }
        }

    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void onLoadmore() {
        if (mPage < mTotalCount / 20) {
            mSearchStoryPresenter.getSearchedAlbums(mAddress, mPage);
        } else {
            if (mSpringView != null) {
                mSpringView.onFinishFreshAndLoad();
                printn(getString(R.string.no_more));
            }
        }
    }


    /**
     * 搜索失败的通知
     */
    public void onError() {
        printn(getString(R.string.network_error));
    }
}
