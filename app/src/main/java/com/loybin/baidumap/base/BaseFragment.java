package com.loybin.baidumap.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.loybin.baidumap.factory.LoadingPager;
import com.loybin.baidumap.model.LoadedResultModel;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UIUtils;
import com.loybin.baidumap.util.UserUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;


/**
 * 类    名:  BaseFragment
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/7 17:39
 * 描    述： 是听故事里面7个Fragment的基类
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "RecommendedFramgent";
    private LoadingPager mLoadingPager;
    protected LoadedResultModel mMProgressModel;

    public LoadingPager getLoadingPager() {
        return mLoadingPager;
    }
    /*
       分析7个Framgent视图的特点
            1.视图情况是固定,一共有4种情况,加载中视图,成功视图,空视图,错误视图
            2.针对4种视图情况做分析,分析特点
                加载中视图,空视图,错误视图属于静态视图,成功视图成功视图可以看做动态视图(变化比较多)
            3.显示的时候,只能显示4种视图中的一种
                    多种-->2种以上
                    coni 加载中视图--->进入页面的时候
                    coni 成功视图
                    coni 空视图
                    coni 错误视图
                    curState-->时刻记录最新状态-->状态一旦改变-->修改ui

        分析7个Framgent数据的加载流程
            触发加载(重要)
                点击重试,下拉刷新,上拉加载,一进入页面加载
            异步加载-->显示加载中视图
            得到数据
            处理数据
            更新ui
                数据加载失败-->显示错误视图
                数据加载成功,但是数据为空-->显示空视图
                数据加载成功,数据不为空-->显示成功视图

     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "BaseFragment onCreate");
        if (mMProgressModel == null){
            mMProgressModel = new LoadedResultModel();
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG,"BaseFragment  onCreateView");
//        return super.onCreateView(inflater, container, savedInstanceState);//返回一个视图
        //调用BaseFragment里面定义的同名的方法
        if (mLoadingPager == null) {
            LogUtils.e(TAG,"创建LoadingPager");
            mLoadingPager = new LoadingPager(UIUtils.getContext(),mLoadingPager) {
                /**
                 * @des 在子线程中请求数据
                 * @call 外界调用LoadingPager的triggerLoadData()方法的时候
                 */
                @Override
                public LoadedResult initData() {
                    //调用BaseFragment里面定义的同名的方法
                    LogUtils.e(TAG,"调用BaseFragment里面定义的同名的方法");
                   return BaseFragment.this.initData(mLoadingPager);
                }

                /**
                 * @return
                 * @des 返回成功视图, 该视图经过了数据绑定
                 * @call 外界触发加载了数据, 数据加载完成, 而且数据加载成功
                 */
                @Override
                public View initSuccessView() {
                    //调用BaseFragment里面定义的同名的方法
                    return BaseFragment.this.initSuccessView();
                }
            };
        } else {
            ViewParent parent = mLoadingPager.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mLoadingPager);
            }
        }

        return mLoadingPager;
    }


    /**
     * @des 在子线程中请求数据
     * @call 外界调用LoadingPager的triggerLoadData()方法的时候
     * @des 在BaseFramgent中, 不知道initData具体实现, 交给子类
     * @des BaseFramgent子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract LoadingPager.LoadedResult initData(LoadingPager loadingPager);

    /**
     * @return
     * @des 返回成功视图, 该视图经过了数据绑定
     * @call 外界触发加载了数据, 数据加载完成, 而且数据加载成功
     * @des 在BaseFramgent中不知道initSuccessView的具体实现, 交给子类
     * @des BaseFramgent子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract View initSuccessView();


    public LoadingPager.LoadedResult checkResData(Object resObj) {
        return UserUtil.checkResData(resObj);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG,"BaseFragment  onDestroy");
    }

    public abstract void onSuccess(List<Album> alba,int totalCount);


    public abstract void onError();
}
