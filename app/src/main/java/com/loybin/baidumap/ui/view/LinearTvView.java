package com.loybin.baidumap.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/11 下午6:57
 * 描   述: LinearLayout
 */
public class LinearTvView extends LinearLayout {


    public boolean mIsToggle;//默认就是关着
    public final TextView mTvTitle;
    public final ImageView mIvSwitch;
    public final ImageView mIvRegiht;
    public final TextView mTvChekUpdate;
    public final ImageView mMageview;


    public LinearTvView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View.inflate(context, R.layout.view_item_setting, this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvSwitch = (ImageView) findViewById(R.id.iv_switch);
        mIvRegiht = (ImageView) findViewById(R.id.iv_regiht);
        mTvChekUpdate = (TextView) findViewById(R.id.check_the_update);
        mMageview = (ImageView) findViewById(R.id.iv_imageview);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);

        //取title的属性
        String title = (String) typedArray.getText(R.styleable.SettingItemView_title);
        String text = (String) typedArray.getText(R.styleable.SettingItemView_attribute);
        Drawable bg = typedArray.getDrawable(R.styleable.SettingItemView_bg);
        int color = typedArray.getColor(R.styleable.SettingItemView_bb, R.color.the_main_color);

        //读取bg2 对应的值
        int bg2 = typedArray.getInt(R.styleable.SettingItemView_bg2, 0);


        //控制是否显示右边的开关
        boolean isShow = typedArray.getBoolean(R.styleable.SettingItemView_isshow, false);

        boolean aBoolean = typedArray.getBoolean(R.styleable.SettingItemView_right, false);

        boolean aText = typedArray.getBoolean(R.styleable.SettingItemView_text, false);

        //释放资源
        typedArray.recycle();

        mTvTitle.setText(title);
        mTvChekUpdate.setText(text);

        mTvChekUpdate.setTextColor(color);
        mIvRegiht.setVisibility(aBoolean ? VISIBLE : GONE);
        mTvChekUpdate.setVisibility(aText ? VISIBLE : GONE);

    }

    /**
     * 设置开关状态
     *
     * @param isToggle true : 打开开关， false , 关闭开关
     */
    public void setToggle(boolean isToggle) {
        mIsToggle = isToggle;
        if (isToggle) {
            mIvSwitch.setImageResource(R.mipmap.on);
        } else {
            mIvSwitch.setImageResource(R.mipmap.off);
        }


    }

    public void setAttribute(String s) {
        mTvChekUpdate.setText(s);
    }


    /**
     * 用于返回当前开关的状态
     *
     * @return true :开关是开着， false : 开关是关着
     */
    public boolean getToggle() {
        return mIsToggle;
    }


    public void toggle() {
        setToggle(!mIsToggle);
    }
}
