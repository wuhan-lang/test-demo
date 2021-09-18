package com.imooc.threadLocal;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalStringBuilder {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            int sb = i;
            es.submit(new Runnable(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String st = new ThreadLocalStringBuilder().printString(sb);
                    System.out.println(st);
                }
            });
        }
        es.shutdown();

    }

    public String printString(int a){
        StringBuilder sb = ThreadSafeStringBuilder.stringBuilderThreadLocal.get();
        sb.delete(0, sb.length());
        String st = sb.append("数字"+a).toString();
        return st;
    }

    /**
     * 把秒数格式化为距离1970/1/1 00：00：00的天数
     * @param seconds
     * @return
     */
    /*public String date(int seconds){
        //根据毫秒数获取日期
        Date date = new Date(1000 * seconds);
        //使用ThreadLocal，保证线程安全，斌且效率高于synchronized，配合线程池使用，线程池中一个线程创建一个simpleDateFormat对象
        StringBuilder sdf =ThreadSafeStringBuilder.dateFormatThreadLocal.get();
        //格式化日期为字符串
        return sdf.format(date);
    }*/
}

class ThreadSafeStringBuilder{

    public static ThreadLocal<StringBuilder>
    stringBuilderThreadLocal = new ThreadLocal<StringBuilder>(){

        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };

    public static ThreadLocal<StringBuilder>
            stringBuilderThreadLocal2 = ThreadLocal
                        .withInitial(()->new StringBuilder());
}
