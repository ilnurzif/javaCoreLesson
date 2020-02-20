package com.lessons.less5;

import java.util.concurrent.CyclicBarrier;

public abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}