package com.loybin.baidumap.factory;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UIUtils;



/**
 * 类    名:  LoadingController
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/8 10:04
 * 描    述： 1.提供视图,视图是4种视图中的一种,提供自身
 * 描    述： 2.加载数据
 * 描    述： 3.数据和视图的绑定(空视图,错误视图,加载视图,无需数据绑定,只有成功视图需要绑定)
 * 描    述： 把7个Framgent里面视图展示和数据加载挪动到这个类里面来
 */
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_SUCCESS = 1;//成功
    public static final int STATE_EMPTY   = 2;//空
    public static final int STATE_ERROR   = 3;//错误
    private static final String TAG = "StoryBooksFramgent";
    public              int mCurState     = STATE_LOADING;//默认状态是加载中视图
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mSuccessView;
    private LoadingPager mLoadingPager;
//    private LoadDataTask mLoadDataTask;
    private static int a = 0;

    public LoadingPager(Context context,LoadingPager loadingPager ) {
        super(context);
        mLoadingPager = loadingPager;
        a++;
        LogUtils.e(TAG, "a ====  " +a);
        initCommontView();
    }


    public int getCurState() {
        return mCurState;
    }


    public void setCurState(int curState) {
        mCurState = curState;
    }

    /**
     * @des 创建3个静态视图(加载中, 错误, 空视图)
     * @call LoadingPager一旦创建的时候
     */
    private void initCommontView() {
        //加载视图
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);

        //空视图
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);

        //错误视图
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        this.addView(mErrorView);

        //添加点击重试操作
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新触发加载数据
                triggerLoadData();
            }
        });

        refreshUIByState();
    }


    /**
     * @des 根据当前状态, 显示对应的视图
     * @call 1.LoadingPager一旦创建的时候
     * @call 2.数据加载之前-->loading视图
     * @call 3.数据加载完成之后
     */
    public void refreshUIByState() {

        LogUtils.e(TAG,"根据当前状态, 显示对应的视图 " + mCurState);
        //隐藏所有的
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        if (mSuccessView != null) {
            mSuccessView.setVisibility(View.GONE);
        }
        switch (mCurState) {
            case STATE_LOADING://显示加载中视图
                mLoadingView.setVisibility(View.VISIBLE);
                break;

            case STATE_EMPTY://显示空视图
                mEmptyView.setVisibility(View.VISIBLE);
                break;

            case STATE_ERROR://显示错误视图
                mErrorView.setVisibility(View.VISIBLE);
                break;

            case STATE_SUCCESS://显示成功视图
                LogUtils.e(TAG,"显示成功视图 " + mSuccessView);
                if (mSuccessView == null) {
                    //创建
                    mSuccessView = initSuccessView();
                    if (mSuccessView != null){
                    //加到容器
                    this.addView(mSuccessView);
                    }else {
                        LogUtils.d(TAG,"mSuccessView  == null");
                    }
                }
                //显示成功视图
                mSuccessView.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }


    /**
     * @des 触发加载数据
     * @call 需要我们的loadingPager完成数据加载的时候
     */
    public void triggerLoadData() {
        //没有成功才去加载
        LogUtils.e(TAG,"没有成功才去加载  mCurState" +mCurState);
        LogUtils.e(TAG,"没有成功才去加载  STATE_SUCCESS" +STATE_SUCCESS);

        if (mCurState != STATE_SUCCESS) {
            LogUtils.e(TAG,"触发加载数据-->triggerLoadData");

            //重置状态
            mCurState = STATE_LOADING;
            refreshUIByState();


            initDatas();
            //异步加载
//            mLoadDataTask = new LoadDataTask();
////            new Thread(mLoadDataTask).start();
//            ThreadPoolProxyFactory.getNormalThreadPoolProxy().submit(mLoadDataTask);
        }else {
            LogUtils.e(TAG,"没有成功才去加载");
        }
    }


    public void initDatas(){
        //得到数据
        LogUtils.e(TAG,"//得到数据 " + Thread.currentThread().getName());
        LoadedResult loadedResult = initData();//同步方法
        //处理数据
        mCurState = loadedResult.getState();
        LogUtils.e(TAG,"视图更新的通知 " + mCurState);

        refreshUIByState();

        //刷新ui-->决定到底提供什么视图给外界(4种视图)
//        MyApplication.getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });

//        //任务执行完成,置空任务
//        mLoadDataTask = null;
    }


//    class LoadDataTask implements Runnable {
//        @Override
//        public void run() {
//
//
//        }
//    }


    /**
     * @des 在子线程中请求数据
     * @call 外界调用LoadingPager的triggerLoadData()方法的时候
     * @des 在LoaingPager中不知道initData具体实现, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法方法, 交给子类具体实现
     */
    public abstract LoadedResult initData();


    /**
     * @return
     * @des 返回成功视图, 该视图经过了数据绑定
     * @call 外界触发加载了数据, 数据加载完成, 而且数据加载成功
     * @des 在LoadingPager, 不知道initSuccessView方法的具体, 交给子类
     * @des 子类必须实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract View initSuccessView();


    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR), LOADING(STATE_LOADING);

        public int state;

        public int getState() {
            return state;
        }

        LoadedResult(int state) {
            this.state = state;
        }
    }



}
