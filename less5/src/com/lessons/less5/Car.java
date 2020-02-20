package com.lessons.less5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static String winnerName = "";
    private static int carPosistion = 0;
    private static Lock lock=new ReentrantLock();

    public static CountDownLatch sr = new CountDownLatch(1);
    private CyclicBarrier prepare_cb;
    private CountDownLatch finrace;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier prepare_cb, CountDownLatch finrace) {
        this.race = race;
        this.speed = speed;
        this.prepare_cb = prepare_cb;
        this.finrace = finrace;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            prepare_cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sr.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        try {
            lock.lock();
            if (winnerName.equals("")) {
                winnerName = "name";
                System.out.println("Победитель " + name);
                carPosistion++;
            } else {
                System.out.println("Участник " + name + " (" + (++carPosistion) + " место)");
            }
        } finally {
            lock.unlock();
        }


        finrace.countDown();
    }
}