package com.imooc.threadLocal;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalNormalusAge04 {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int a = i;
            es.submit(new Runnable(){
                @Override
                public void run() {
                    String date = new ThreadLocalNormalusAge04().date(a*2);
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
        //使用ThreadLocal，保证线程安全，斌且效率高于synchronized，配合线程池使用，线程池中一个线程创建一个simpleDateFormat对象
        SimpleDateFormat sdf =ThreadSafeFormatter.dateFormatThreadLocal2.get();
        //格式化日期为字符串
        return sdf.format(date);
    }
}

class ThreadSafeFormatter{

    public static ThreadLocal<SimpleDateFormat>
    dateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>(){

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
    };

    public static ThreadLocal<SimpleDateFormat>
            dateFormatThreadLocal2 = ThreadLocal
                        .withInitial(()->new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));;
}
