package com.suwei.cn.thread;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**参考文章：http://blog.csdn.net/vking_wang/article/details/9952063
 * 共享资源
 */
class syncData{
    private int data;
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    public void set(int data){
        rwl.writeLock().lock(); //取到写锁
        try {
            System.out.println(Thread.currentThread().getName()+"准备写入数据");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.data = data;
            System.out.println(Thread.currentThread().getName() + "写入" + this.data);
        }finally {
            rwl.writeLock().unlock();// 释放写锁
        }

    }


    public void get() {
        rwl.readLock().lock();// 取到读锁
        try {
            System.out.println(Thread.currentThread().getName() + "准备读取数据");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "读取" + this.data);
        } finally {
            rwl.readLock().unlock();// 释放读锁
        }
    }
}

/**
 * Created by Administrator on 2017/8/24/024.
 * 读写锁，可以实现多个线程对同一资源的读操作，但是同时只有一个线程进行写操作
 */
public class MyReadWriteLock {
    public static void main(String[] args) {
//        final Data data = new Data();
        final syncData data = new syncData();
//        final RwLockData data = new RwLockData();

        //写入
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        data.set(new Random().nextInt(30));
                    }
                }
            });
            t.setName("Thread-W" + i);
            t.start();
        }
        //读取
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        data.get();
                    }
                }
            });
            t.setName("Thread-R" + i);
            t.start();
        }
    }
}
