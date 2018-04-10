package com.loybin.baidumap.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UIUtils;


/**
 * 类    名:  RatioFrameLayout
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/7 14:14
 * 描    述：已知宽度,动态计算高度
 * 描    述：已知高度,动态计算宽度
 * 描    述：公式  图片的宽高比 = 自身的width/自身的height
 */
public class RatioFrameLayout extends FrameLayout {
    public static final int RELATIVE_WIDTH = 0;//相对于宽度-->已知宽度,动态计算高度
    public static final int RELATIVE_HEIGHT = 1;//相对于高度-->已知高度,动态计算宽度
    float mPicRatio = 1f;
    int relative = RELATIVE_WIDTH;

    /**
     * 设置图片的宽高比
     *
     * @param picRatio
     */
    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    /**
     * 设置计算的相对值
     *
     * @param relative
     */
    public void setRelative(int relative) {
        this.relative = relative;
    }

    public RatioFrameLayout(Context context) {
        this(context, null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取出自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout);

        mPicRatio = typedArray.getFloat(R.styleable.RatioFrameLayout_picRatio, 1);
        relative = typedArray.getInt(R.styleable.RatioFrameLayout_relative, RELATIVE_WIDTH);


        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        UNSPECIFIED wrap_content
        EXACTLY match_parent 100dp 100px
        AT_MOST
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && relative == RELATIVE_WIDTH) {
            //得到自身的宽度
            int selfWidth = MeasureSpec.getSize(widthMeasureSpec);

            //计算应有的高度
            //图片的宽高比 = 自身的width/自身的height
            int selfHeight = (int) (selfWidth / mPicRatio + .5f);

            LogUtils.sf("容器应有的高度:" + UIUtils.px2Dip(selfHeight) + "dp");//px

            //保存测绘结果
            setMeasuredDimension(selfWidth, selfHeight);

            //孩子的宽度和高度
            int childWidth = selfWidth - getPaddingRight() - getPaddingLeft();
            int childHeight = selfHeight - getPaddingBottom() - getPaddingTop();

            //测绘孩子
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        } else if (heightMode == MeasureSpec.EXACTLY && relative == RELATIVE_HEIGHT) {
            //得到自身的宽度
            int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

            //计算应有的高度
            //图片的宽高比 = 自身的width/自身的height
            int selfWidth = (int) (selfHeight * mPicRatio + .5f);

            LogUtils.sf("容器应有的宽度:" + UIUtils.px2Dip(selfWidth) + "dp");//px

            //保存测绘结果
            setMeasuredDimension(selfWidth, selfHeight);

            //孩子的宽度和高度
            int childWidth = selfWidth - getPaddingRight() - getPaddingLeft();
            int childHeight = selfHeight - getPaddingBottom() - getPaddingTop();

            //测绘孩子
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
