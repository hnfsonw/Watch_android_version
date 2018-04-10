package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.widget.chatrow.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/17 上午10:14
 * 描   述: 地图搜索界面
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener, TextWatcher, Inputtips.InputtipsListener {


    private static final String TAG = "SearchActivity";
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

    public SharedPreferences mGlobalvariable;
    private String mCict;
    private List<Tip> mTipList;
    private String mAddress;
    private List<HashMap<String, Object>> mListString;
    private SimpleAdapter mAAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void init() {
        ButterKnife.bind(this);
        mCict = getIntent().getStringExtra("String");
        Log.d(TAG, "城市名: " + mCict);
        mTipList = new ArrayList<>();

        initView();
        initListener();
    }


    private void initListener() {
        mKeyWorldsView.addTextChangedListener(this);
        mListview.setOnItemClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mTipList = (List<Tip>) loadDataFromLocalBean("SearchList",
                Constant.PROTOCOL_TIMEOUT_MONTH, 0);
        if (mTipList == null) {
            mTipList = new ArrayList<>();
        }

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
        mTvTitle.setText(getString(R.string.search_address));
    }


    @OnClick({R.id.iv_back, R.id.tv_search, R.id.et_search})
    public void onViewClicked(View view) {
        String keyword = mKeyWorldsView.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_search:
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
        try {
            InputtipsQuery inputquery = new InputtipsQuery(keyword, mCict);
            inputquery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(mContext, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } catch (Exception e) {
            LogUtils.e(TAG, "搜索异常 " + e.getMessage());
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (mListString.size() > 0) {
                LatLonPoint latLonPoint = (LatLonPoint) mListString.get(position).get("latlon");
                LatLng location = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("location", location);// 序列化
                bundle.putString("address", String.valueOf(mListString.get(position).containsKey("address")));
                intent.putExtras(bundle);// 发送数据
                setResult(RELATION, intent);
                finishActivityByAnimation(this);

                Log.d(TAG, "latLonPoint onItemClick: " + latLonPoint.getLatitude());
                Log.d(TAG, "latLonPoint onItemClick: " + latLonPoint.getLongitude());
            }

        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage() + " 点击地址异常");
        }


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
        InputtipsQuery inputquery = new InputtipsQuery(mAddress, mCict);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(mContext, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        try {
            dismissLoading();
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                mListString = new ArrayList<HashMap<String, Object>>();

                if (tipList.size() > 0) {
                    mTipList.clear();
                    mTipList.addAll(tipList);

                    for (int i = 0; i < tipList.size(); i++) {

                        HashMap<String, Object> map = new HashMap<String, Object>();

                        if (tipList.get(i).getName() != null && !TextUtils.isEmpty(tipList.get(i).getName())) {
                            map.put("name", tipList.get(i).getName());
                        }
                        map.put("address", tipList.get(i).getAddress());
                        if (tipList.get(i).getPoint() != null) {
                            map.put("latlon", tipList.get(i).getPoint());
                        }

                        LogUtils.e(TAG, "name " + tipList.get(i).getName() + "~~" + "address " + tipList.get(i).getAddress());
                        if (tipList.get(i).getAddress() != null && !TextUtils.isEmpty(tipList.get(i).getAddress())
                                && tipList.get(i).getPoint().getLatitude() != 0) {
                            mListString.add(map);
                        }
                    }
                    if (mListString.size() > 0) {
                        mAAdapter = new SimpleAdapter(getApplicationContext(), mListString, R.layout.item_search,
                                new String[]{"name", "address"}, new int[]{R.id.tv_name, R.id.tv_address});
                        mListview.setAdapter(mAAdapter);
                        mAAdapter.notifyDataSetChanged();
                    } else {
                        mListString.clear();
                        mAAdapter = new SimpleAdapter(getApplicationContext(), mListString, R.layout.item_search,
                                new String[]{"name", "address"}, new int[]{R.id.tv_name, R.id.tv_address});
                        mListview.setAdapter(mAAdapter);
                        mAAdapter.notifyDataSetChanged();
                        printn(getString(R.string.did_not_get_to_the_address_information));
                    }


                } else {
                    mListString.clear();
                    mAAdapter = new SimpleAdapter(getApplicationContext(), mListString, R.layout.item_search,
                            new String[]{"name", "address"}, new int[]{R.id.tv_name, R.id.tv_address});
                    mListview.setAdapter(mAAdapter);
                    mAAdapter.notifyDataSetChanged();
                    printn(getString(R.string.did_not_get_to_the_address_information));

                }
            } else {
                LogUtils.e(TAG, "rCode " + rCode);
            }
        } catch (Exception e) {

        }

    }

}
