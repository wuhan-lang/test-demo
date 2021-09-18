package com.imooc.thread;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaterPoolThread extends ThreadPoolExecutor {
    private  final ReentrantLock lock = new ReentrantLock();
    private Condition unPause = lock.newCondition();
    private boolean isPaused;

    public WaterPoolThread(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public WaterPoolThread(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public WaterPoolThread(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public WaterPoolThread(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

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
            isPaused = false;
            System.out.println("下午，开始放水！！");
            unPause.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaterPoolThread wpt = new WaterPoolThread(5, 5, 10l, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println("水池正在放水");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        };

        Runnable run1 = new Runnable() {
            @Override
            public void run() {
                while(wpt.isPaused) {
                    System.out.println("水池暂停放水");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        };
        int i = 1000;
        System.out.println("上班啦，水池放入1000吨水！");
        for (; i > 0; i--) {
            wpt.execute(run);
        }
        //主线程中修改状态，线程池就会暂停，因为钩子函数会暂停
        Thread.sleep(200);
        wpt.pause();

        System.out.println("回家吃午饭，水池剩下"+wpt.getQueue().size()+"吨水");
        Thread thread = new Thread(run1);
        thread.start();
        Thread.sleep(200);
        wpt.resume();

        Thread.sleep(200);
        wpt.shutdown();
        if (wpt.isShutdown()){
            System.out.println("下午5点，开始停水啦！10秒后强制停水");
        }
        if (wpt.awaitTermination(100l, TimeUnit.MILLISECONDS)){
            System.out.println("水池已放完");
        }else {
            List list = wpt.shutdownNow();
            System.out.println("启动强制停水！水池剩下"+list.size()+"吨水");
        }
    }
}


