package com.imooc.threadLocal;

public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int FinalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TokenContextHolder.holder.set("token"+FinalI);
                    new TokenUtil().getTokenStr();
                }
            }).start();
        }
    }

}

class TokenUtil{
    public void getTokenStr(){
        String s = TokenContextHolder.holder.get();
        System.out.println("GetToken1类拿到："+s);
        new GetToken().getToken();
    }
}

class GetToken{
    public void getToken(){
        String s = TokenContextHolder.holder.get();
        System.out.println("GetToken2类拿到："+s);
    }
}

class TokenContextHolder {
    public static ThreadLocal<String> holder = new ThreadLocal<>();
}

