package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.ui.fragment.FamilyFragment;
import com.loybin.baidumap.ui.fragment.FriendFragment;
import com.loybin.baidumap.util.LogUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/26 下午2:31
 * 描   述: 新  手表通讯录
 */
public class WatchLeisuresActivity extends BaseActivity {


    private static final String TAG = "WatchLeisuresActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.pager)
    ViewPager mPager;

    private String mIsAdmin;
    private int mPosition;
    private FriendFragment mFriendFragment;
    private boolean mIsEditor = false;
    private FamilyFragment mFamilyFragment;
    private IWXAPI mApi;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_watch_leisure;
    }


    @Override
    protected void init() {
        mIsAdmin = getIntent().getStringExtra(STRING);
        LogUtils.d(TAG, "mIsAdmin " + mIsAdmin);
        if (mIsAdmin == null) {
            mIsAdmin = "0";
        }

        initView();
        initViewpager();

    }


    private void initViewpager() {
        mPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mPager);

        final MyOnpageChangeListener myOnpageChangeListener = new MyOnpageChangeListener();
        mPager.setOnPageChangeListener(myOnpageChangeListener);

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.watch_the_address_book));

        if (mIsAdmin.equals("0")) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(getString(R.string.add_leisure));
        }
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
                if (mPosition == 0 && mIsAdmin.equals("1")) {
                    //添加联系人
                    toActivity(100, AddLeisureActivity.class, "add");
                } else {

                    if (mIsEditor) {
                        mTvRight.setText(getString(R.string.the_editor));
                    } else {
                        mTvRight.setText(getString(R.string.cancel));
                    }
                    mIsEditor = !mIsEditor;
                    mFriendFragment.editor(mIsEditor);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            String phone = data.getStringExtra("phone");
            mFamilyFragment.setPhone(phone);
            LogUtils.d(TAG, "phone " + phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            LogUtils.d(TAG, " position = " + position);
            if (position == 0) {
                mIsEditor = false;
                mTvRight.setText(getString(R.string.add_leisure));
            } else {
                mTvRight.setText(getString(R.string.the_editor));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }


    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mFamilyFragment = new FamilyFragment(mIsAdmin);
                    return mFamilyFragment;

                case 1:
                default:
                    mFriendFragment = new FriendFragment(mIsAdmin);
                    return mFriendFragment;
            }
        }


        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.family);
                case 1:
                default:
                    return getString(R.string.friend);
            }
        }
    }
}
