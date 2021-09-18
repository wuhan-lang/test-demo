package com.imooc.threadLocal;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalNormalusAge03 {
    private static ExecutorService es = Executors.newFixedThreadPool(10);
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int a = i;
            es.submit(new Runnable(){
                @Override
                public void run() {
                    String date = new ThreadLocalNormalusAge03().date(a*2);
                    System.out.println(date);
                }
            });
        }
        es.shutdown();

    }

    /**
     * 把秒数格式化为距离1970/1/1 00：00：00的天数
     * @param seconds
     * @return
     */
    public String date(int seconds){
        //根据毫秒数获取日期
        Date date = new Date(1000 * seconds);

        String s = null;
        //目的是给sdf上锁，防止sdf对象在多线程中出现导致的线程安全问题
        synchronized (ThreadLocalNormalusAge03.class) {
            s = sdf.format(date);
        }
        //格式化日期为字符串
        return s;
    }
}
