package com.imooc.thread;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PoolProject {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        aa(a);
        bb();
    }

    public static void bb(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        //延迟8秒运行
        //executorService.schedule(new Project(), 8,TimeUnit.SECONDS);
        //延迟8秒，间隔4秒运行
        executorService.scheduleAtFixedRate(new Project(), 8,4, TimeUnit.SECONDS);
    }

    public static void aa(int a){
        ExecutorService executorService = null;
        if(a<10){
            executorService = Executors.newSingleThreadExecutor();
            for (int i = 0; i < a; i++) {
                executorService.execute(new Project());
            }
            System.out.println("a<10执行完毕！");
        }
        if(a>=10 && a<100){
            executorService = Executors.newFixedThreadPool(10);
            for (int i = 0; i < a; i++) {
                executorService.execute(new Project());
            }
            System.out.println("a<100执行完毕！");
        }
        if (a >= 100){
            executorService = Executors.newCachedThreadPool();
            for (int i = 0; i < a; i++) {
                executorService.execute(new Project());
            }
            System.out.println("a>100执行完毕！");
        }
    }

}

class Project implements Runnable{

    @Override
    public void run() {
        System.out.println("工程队"+Thread.currentThread().getName()+"正在施工");
    }
}
