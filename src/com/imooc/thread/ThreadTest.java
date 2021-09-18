package com.imooc.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    public static void main(String[] args) {
        //创建一个线程池，里面核心线程数为4
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 1000; i++) {
            //四个线程轮流随机执行1000个任务，原本期望用1000个线程，但是线程池只有4个线程，就用4个线程轮流执行1000个线程任务
            executorService.execute(new Task());
            System.out.println("第"+i+"次");
        }
    }

}

class Task implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
