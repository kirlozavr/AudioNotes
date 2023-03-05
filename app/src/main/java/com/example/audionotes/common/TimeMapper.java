package com.example.audionotes.common;

public class TimeMapper {

    private long time;
    private int flag;
    public static final int MILLISECOND = 0;
    public static final int SECOND = 1;

    public TimeMapper(long time, int flag){
        this.flag = flag;
        this.time = time;
    }

    public int getMinute(){
        int minute;
        if(flag == MILLISECOND) {
            minute = (int) (((time / 1000) / 60) % 60);
        } else {
             minute = (int) ((time / 60) % 60);
        }
        return minute;
    }

    public int getSecond(){
        int second;
        if(flag == MILLISECOND){
            second = (int) ((time / 1000) % 60);
        } else {
            second = (int) (time % 60);
        }
        return second;
    }

}
