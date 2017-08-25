package com.suwei.cn.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者和消费者的实现：1,使用wait()和notify();2,使用线程间通信Condition
 * Created by Administrator on 2017/8/25/025.
 * 2,使用线程间通信Condition
 */
class AppleTree2{
    final Lock lock = new ReentrantLock();          //锁对象
    final Condition notFull  = lock.newCondition(); //写线程锁
    final Condition notEmpty = lock.newCondition(); //读线程锁

    //标记：flag = true表示可以生产，无法取走；flag = false 表示可以取走，但是无法生产
    boolean flag = true;

    //生产苹果
    public  void setApple(int i) throws InterruptedException {
        lock.lock();    //锁定
        try{
            if(this.flag == false)//表示此时消费者还未取走，无法生产
                notFull.await();
            System.out.println("生产了一个苹果"+i);
            this.flag = false;
            notEmpty.signalAll();
        }finally {
            lock.unlock();  //释放锁
        }


    }

    //消费苹果
    public  void getApple(int i) throws InterruptedException{
        lock.lock();
        try{
           if(this.flag == true) //表示此时生产者还未生产，无法消费
                notEmpty.await();
            System.out.println("取得了一个苹果"+i);
            this.flag = true;
            notFull.signalAll();
        }finally {
            lock.unlock();  //释放锁
        }
    }
}

//生产者
class Producer2 implements Runnable{
    AppleTree2 source = null;

    public Producer2(AppleTree2 source) {
        this.source = source;
    }

    @Override
    public void run() {
        for (int i = 0; i <5 ; i++) {
            try {
                Thread.sleep(1000);
                source.setApple(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//消费者
class Customer2 implements Runnable{
    AppleTree2 source = null;

    public Customer2(AppleTree2 source) {
        this.source = source;
    }

    @Override
    public void run() {
        for (int i = 0; i <5 ; i++) {
            try {
                Thread.sleep(1000);
                source.getApple(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class ProducerAndCustomer2 {
    public static void main(String[] args) {
        AppleTree2 appleTree = new AppleTree2();
        Producer2 producer = new Producer2(appleTree);
        Customer2 customer = new Customer2(appleTree);
        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(customer);
        thread1.start();
        thread2.start();
    }
}
