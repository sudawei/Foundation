package com.suwei.cn.thread;

/**
 * 生产者和消费者的实现：1,使用wait()和notify();2,使用线程间通信Condition
 * Created by Administrator on 2017/8/25/025.
 * 1,使用wait()和notify();
 */
class AppleTree{
    //标记：flag = true表示可以生产，无法取走；flag = false 表示可以取走，但是无法生产
    boolean flag = true;

   //生产苹果
    public synchronized void setApple(int i) throws InterruptedException {
       if(this.flag == false)//表示此时消费者还未取走，无法生产
        super.wait();
        System.out.println("生产了一个苹果"+i);
        this.flag = false;
        super.notifyAll();
    }

    //消费苹果
    public synchronized void getApple(int i) throws InterruptedException{
        if(this.flag == true)//表示此时生产者还未生产，无法消费
        super.wait();
        System.out.println("取得了一个苹果"+i);
        this.flag = true;
        super.notifyAll();
    }
}

//生产者
class Producer implements Runnable{
    AppleTree source = null;

    public Producer(AppleTree source) {
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
class Customer implements Runnable{
    AppleTree source = null;

    public Customer(AppleTree source) {
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
public class ProducerAndCustomer1 {
    public static void main(String[] args) {
        AppleTree appleTree = new AppleTree();
        Producer producer = new Producer(appleTree);
        Customer customer = new Customer(appleTree);
        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(customer);
        thread1.start();
        thread2.start();
    }
}
