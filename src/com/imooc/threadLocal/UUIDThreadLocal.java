package com.imooc.threadLocal;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UUIDThreadLocal {
    private static ExecutorService es = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        UpdateUUID uu = new UpdateUUID();
        for (int i = 0; i < 10; i++) {
            int FinalI = i;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    new PutUUID().put(FinalI);
                    uu.update(FinalI);
                   // System.out.println(Thread.currentThread().getName()+"当前线程");
                }
            });
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    new PutUUID().put(FinalI);
                    new UpdateUUID().update(FinalI);
                }
            }).start();*/
        }
        es.shutdown();
    }

}

class PutUUID{
    public void put(int num){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        UUIDContextHolder.holder.set(uuid+"-----"+num);
        //System.out.println("已放入UUID"+uuid);
    }
}

class UpdateUUID{
    public synchronized void update(int num){
        String ss = UUIDContextHolder.holder.get();
        System.out.println("updateUUID前拿到:"+ss);
        UUIDContextHolder.holder.remove();
        new PutUUID().put(num);
        ss = UUIDContextHolder.holder.get();
        System.out.println("GetUUID拿到:"+ss+"最新");
    }
}

class GetUUID{
    public void getUuid(){
        String ss = UUIDContextHolder.holder.get();
        //System.out.println("GetUUID:"+ss);
    }
}

class UUIDContextHolder{
    public static ThreadLocal<String> holder = new ThreadLocal<>();
}
