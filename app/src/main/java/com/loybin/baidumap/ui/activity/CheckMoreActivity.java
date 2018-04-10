package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.WifiModel;
import com.loybin.baidumap.ui.adapter.FragmentItemAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/30 下午2:29
 * 描   述: 故事查看更多 view
 */
public class CheckMoreActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final java.lang.String TAG = "CheckMoreActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.listview)
    ListView mListview;
    private String mJson;
    private List<Album> mAlbums;
    private Intent mIntent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_check_more;
    }

    @Override
    protected void init() {
        Gson gson = new Gson();
        mJson = getIntent().getStringExtra("json");
        mAlbums = gson.fromJson(mJson, new TypeToken<List<Album>>() {
        }.getType());
        Collections.reverse(mAlbums);
        LogUtils.d(TAG, "json = " + mJson);

        initView();
        initListener();
        initData();
    }


    private void initListener() {
        mListview.setOnItemClickListener(this);
    }


    private void initData() {
        FragmentItemAdapter fragmentItemAdapter = new FragmentItemAdapter(this, mAlbums);
        mListview.setAdapter(fragmentItemAdapter);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.view_more_));
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.delete_cc));
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                saveData2Local("Recommended.Album", "");
                EventBus.getDefault().post(new WifiModel());
                printn(getString(R.string.delete_success));
                finishActivityByAnimation(this);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LogUtils.e(TAG, "onItemClick" + mAlbums.get(position).getId());
            if (mIntent == null) {
                mIntent = new Intent(this, StoryListActivity.class);
            }
            mIntent.putExtra("id", mAlbums.get(position).getId());
            mIntent.putExtra("title", mAlbums.get(position).getAlbumTitle());
            mIntent.putExtra("imageUrl", mAlbums.get(position).getCoverUrlLarge());
            mIntent.putExtra("intro", mAlbums.get(position).getAlbumIntro());
            mIntent.putExtra("count", mAlbums.get(position).getIncludeTrackCount() + "");

            startActivity(mIntent);
        } catch (Exception e) {
            LogUtils.e(TAG, "onItemClick 异常 " + e.getMessage());
        }
    }
}
