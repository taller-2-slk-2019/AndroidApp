package com.taller2.hypechatapp.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";
    private static final String localPattern = "dd/MM/yyyy HH:mm";

    public static Date parseServerDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String dateToLocalString(Date date){
        SimpleDateFormat myFormat = new SimpleDateFormat(localPattern);
        return myFormat.format(date);
    }

    public static String serverToLocalString(String date){
        return dateToLocalString(parseServerDate(date));
    }
}
