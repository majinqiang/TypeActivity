package com.laoma.baseloadpage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by majinqiang on 3/24/2017.
 */
public class ThreadPoolFactory {

    public static ExecutorService getNormalPool() {
        //创建一个可重用固定线程数的线程池
//        ExecutorService pool = Executors. newSingleThreadExecutor();
        //创建一个可重用固定线程数的线程池
//        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建一个可重用固定线程数的线程池
        return Executors.newCachedThreadPool();
    }
}
