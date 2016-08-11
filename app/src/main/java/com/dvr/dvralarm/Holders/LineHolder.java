package com.dvr.dvralarm.Holders;


public class LineHolder {
    public int id;
    public boolean is_actual;
    public boolean is_active;
    public int hour;
    public int minute;
    public boolean sound;
    public boolean vibration;
    public boolean week_days;
    public boolean day1;
    public boolean day2;
    public boolean day3;
    public boolean day4;
    public boolean day5;
    public boolean day6;
    public boolean day7;
    public boolean [] days;

    public  LineHolder(
                                int id,
                                boolean is_actual,
                                boolean is_active,
                                int hour,
                                int minute,
                                boolean sound,
                                boolean vibration,
                                boolean week_days,
                                boolean day1,
                                boolean day2,
                                boolean day3,
                                boolean day4,
                                boolean day5,
                                boolean day6,
                                boolean day7

                           ){
        this.id = id;
        this.is_actual = is_actual;
        this.is_active = is_active;
        this.hour = hour;
        this.minute = minute;
        this.sound = sound;
        this.vibration = vibration;
        this.week_days = week_days;
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;

        days = new boolean[8];
        days[0] = this.day1;
        days[1] = this.day2;
        days[2] = this.day3;
        days[3] = this.day4;
        days[4] = this.day5;
        days[5] = this.day6;
        days[6] = this.day7;

    }


}
