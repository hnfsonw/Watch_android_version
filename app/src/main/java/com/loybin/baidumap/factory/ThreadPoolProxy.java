package com.loybin.baidumap.factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类    名:  ThreadPoolProxy
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/10 09:44
 * 描    述： 线程池的代理,替ThreadPool做一些操作,对相关方法增强(提交任务,执行任务,移除任务)
 */
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;//核心池大小
    private int mMaximumPoolSize;//线程池的最大线程数

    public ThreadPoolProxy(int corePoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = corePoolSize;
    }
    /*
     使用线程代理之后,线程池如何创建就不用关心
     new ThreadPoolProxy(3).submit(task)
     new ThreadPoolProxy(3).execute(task)
     new ThreadPoolProxy(3).remove(task)
     new ThreadPoolProxy(5).submit(task)
     new ThreadPoolProxy(5).execute(task)
     new ThreadPoolProxy(5).remove(task)


     */

    /**
     * 初始化ThreadPoolExecutor对象
     */
    private void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 3000;//保持时间
                    TimeUnit unit = TimeUnit.MILLISECONDS;//保持时间的单位
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();//任务队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();//异常捕获器
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize,
                            keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
    }

    /*
     提交任务和执行任务的区别?
        submit-->有返回值
        execute-->没有返回值
     submit返回回来的Future对象是干嘛的?
        Future:描述异步执行的结果的
            get方法:任务执行完成会接收结果,阻塞等待任务执行完成,得知任务执行过程中可能抛出的异常
            cancle:取消任务的执行
     */

    /**
     * 提交任务
     */
    public Future submit(Runnable task) {
        //额外的操作,增强的操作
        initThreadPoolExecutor();
        Future<?> future = mExecutor.submit(task);
        return future;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        boolean remove = mExecutor.remove(task);
    }
}
