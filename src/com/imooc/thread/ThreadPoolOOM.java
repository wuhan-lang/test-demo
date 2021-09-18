package com.imooc.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolOOM {
    public static void main(String[] args) {
        //创建一个线程池，里面核心线程数为4
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            //四个线程轮流随机执行1000个任务，原本期望用1000个线程，但是线程池只有4个线程，就用4个线程轮流执行1000个线程任务
            executorService.execute(new Task1());
        }
    }

}

class Task1 implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
