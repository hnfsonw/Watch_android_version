package com.loybin.baidumap.factory;


/**
 * 类    名:  ThreadPoolProxyFactory
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/8 10:08
 * 描    述： ThreadPoolProxy工厂,封装ThreadPoolProxy对象的创建过程
 */
public class ThreadPoolProxyFactory {
    //普通的线程池-->5个任务-->ThreadPoolProxy(5)-->mNormalThreadPoolProxy
    //下载的线程池-->3个任务-->ThreadPoolProxy(3)-->mDownloadThreadPoolProxy
    static ThreadPoolProxy mNormalThreadPoolProxy;
    static ThreadPoolProxy mDownloadThreadPoolProxy;
    /**
     * 得到普通的线程池代理对象
     *
     * @return
     */
    public static ThreadPoolProxy getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    /**
     * 得到下载的线程池代理对象
     *
     * @return
     */
    public static ThreadPoolProxy getDownloadThreadPoolProxy() {
        if (mDownloadThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mDownloadThreadPoolProxy == null) {
                    mDownloadThreadPoolProxy = new ThreadPoolProxy(3);
                }
            }
        }
        return mDownloadThreadPoolProxy;
    }
}
