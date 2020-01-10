package com.qy.ccm.other.thread;

import com.qy.ccm.bean.other.entity.WalletToken;
import com.qy.ccm.bean.other.rxbus.TokenWalletEvent;
import com.qy.ccm.utils.rxbus.RxBusHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Data：2019/1/14-4:16 PM
 */
public class GetCurrencyBalance extends ThreadPoolExecutor {


    public GetCurrencyBalance(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public GetCurrencyBalance(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public GetCurrencyBalance(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public GetCurrencyBalance(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return super.awaitTermination(timeout, unit);

    }

    @Override
    protected void terminated() {
        super.terminated();

    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
// TODO Auto-generated method stub
        super.afterExecute(r, t);
        synchronized (this) {
            //已执行完任务之后的最后一个线程
            if (this.getActiveCount() == 1) {
                TokenWalletEvent walletEvent = new TokenWalletEvent();
                RxBusHelper.post(walletEvent);
                this.notify();
            }
        }
    }


}
