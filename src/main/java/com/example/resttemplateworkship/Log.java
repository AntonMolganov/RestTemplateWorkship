package com.example.resttemplateworkship;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void log(String text) {
        String threadId = "Thread_" + Thread.currentThread().getId();
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        System.out.println(formatter.format(new Date(System.currentTimeMillis())) + " " + threadId + " " + text);
    }
}
