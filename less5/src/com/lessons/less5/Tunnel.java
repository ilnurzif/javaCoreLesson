package com.lessons.less5;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private static Semaphore sm;

    public Tunnel(int limitCar) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        if (sm==null)
        {sm=new Semaphore(limitCar);}
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                sm.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                sm.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
