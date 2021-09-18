package com.imooc.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：      演示每个任务前后都可以放钩子函数
 */
public class PauseableThreadPool extends ThreadPoolExecutor {
    private  final ReentrantLock lock = new ReentrantLock();
    private Condition unPause = lock.newCondition();
    private boolean isPaused;

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    //线程池提供的钩子函数，会在所有的线程任务执行前执行
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            //如果pause执行，此处就暂停
            while (isPaused) {
                unPause.await();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void pause(){
        lock.lock();
        try{
            isPaused = true;
        }finally {
            lock.unlock();
        }
    }

    private void resume(){
        lock.lock();
        try{
            Thread.sleep(2000);
            isPaused = false;
            unPause.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PauseableThreadPool ptp = new PauseableThreadPool(10, 20, 10l, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println("被执行了！！");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 10000; i++) {
            ptp.execute(run);
        }
            Thread.sleep(1500);
            ptp.pause();
            System.out.println("线程被暂停了！");
            Thread.sleep(1500);
            ptp.resume();
            System.out.println("线程被重启了！");
            System.exit(5);
    }
}

