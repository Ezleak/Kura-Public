package me.windyteam.kura.concurrent.event;

public class Priority {

    public static final int LOWEST = Integer.MIN_VALUE;
    public static final int LOW = 0;
    public static final int Medium = 1000;
    public static final int HIGH = 2000;
    public static final int HIGHEST = Integer.MAX_VALUE - 1;
    public static final int PARALLEL = Integer.MAX_VALUE;

}
