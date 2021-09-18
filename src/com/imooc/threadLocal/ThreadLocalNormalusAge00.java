package com.imooc.threadLocal;



import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalNormalusAge00 {

    public static void main(String[] args) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                String date = new ThreadLocalNormalusAge00().date(10);
                System.out.println(date);
            }
        }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                String date = new ThreadLocalNormalusAge00().date(1007);
                System.out.println(date);
            }
        }).start();
    }

    /**
     * 把秒数格式化为距离1970/1/1 00：00：00的天数
     * @param seconds
     * @return
     */
    public String date(int seconds){
        //根据毫秒数获取日期
        Date date = new Date(1000 * seconds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //格式化日期为字符串
        return sdf.format(date);
    }
}
