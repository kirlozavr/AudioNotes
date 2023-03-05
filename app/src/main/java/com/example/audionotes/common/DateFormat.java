package com.example.audionotes.common;

import java.time.format.DateTimeFormatter;

public class DateFormat {

    public static DateTimeFormatter getFormatToFileName(){
        return DateTimeFormatter
                .ofPattern("_yyyy_MM_dd_HH_mm_ss");
    }

    public static DateTimeFormatter getFormatToData(){
        return DateTimeFormatter
                .ofPattern("yyyy.MM.dd");
    }

    public static DateTimeFormatter getFormatToTime(){
        return DateTimeFormatter
                .ofPattern("HH:mm");
    }
}
