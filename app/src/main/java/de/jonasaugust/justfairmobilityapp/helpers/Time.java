package de.jonasaugust.justfairmobilityapp.helpers;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.jonasaugust.justfairmobilityapp.helpers.compatability.DateCompat;

@SuppressWarnings("unused")
public class Time {

    public static final String SEPARATOR = ":";

    public static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000L;
    public static final int HOURS_OF_DAY = 24;

    private int minute;
    private int hour;

    public static final int DURATION_DAY = 1;
    public static final int DURATION_WEEK = 7;
    public static final int DURATION_MONTH = 30;
    public static final int DURATION_THREE_MONTHS = 3 * DURATION_MONTH;
    public static final int DURATION_YEAR = 365;

    public static final int WEEKS_PER_MONTH = 4;
    public static final int MONTHS_PER_YEAR = 12;

    public Time(){
        DateCompat date = new DateCompat();
        this.minute = date.getMinutes();
        this.hour = date.getHours();
    }

    public Time(int hour, int minute) {
        this.minute = minute;
        this.hour = hour;
    }

    public Time(DateCompat date) {
        this.minute = date.getMinutes();
        this.hour = date.getHours();
    }

    public Time(Time time) {
        this.minute = time.getMinute();
        this.hour = time.getHour();
    }

    public Time(Long timestamp) {
        this(new DateCompat(timestamp));
    }

    /**
     * @param s Format 00:00 oder 0000
     */
    public Time(String s) {
        if (s.contains(SEPARATOR)) {
            String[] strings = s.split(SEPARATOR);
            hour = Integer.parseInt(strings[0]);
            minute = Integer.parseInt(strings[1]);
        } else {
            hour = Integer.parseInt(s.substring(0, 2));
            minute = Integer.parseInt(s.substring(2));
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public DateCompat attachToDate(DateCompat date){
        date.setMinutes(minute);
        date.setHours(hour);
        date.setSeconds(0);
        return date;
    }

    public boolean lessEqual(Time time){
        DateCompat date1 = attachToDate(new DateCompat());
        DateCompat date2 = time.attachToDate(new DateCompat());
        return date1.before(date2) || isEqual(time);
    }

    public boolean isEqual(Time time){
        return getMinute() == time.getMinute() && getHour() == time.getHour();
    }

    public void setZeit(Time time){
        minute = time.getMinute();
        hour = time.getHour();
    }

    public void setZeit(int hour,int minute){
        this.minute = minute;
        this.hour = hour;
    }

    public void setZeit(DateCompat date){
        minute = date.getMinutes();
        hour = date.getHours();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Time time = (Time) obj;
            return time.getHour() == getHour() && time.getMinute() == getMinute();
        }catch (Exception e){
            return super.equals(obj);
        }
    }

    public double zuDouble() {
        return getHour() + (((double)getMinute())/60d);
    }

    @NonNull
    public String toString(){
        return toString(false);
    }

    public String toString(boolean leadingZeroHour) {
        return (leadingZeroHour && hour <= 9 ? "0" : "") + hour + SEPARATOR + (minute <= 9 ? "0" : "") + minute;
    }

    public int minutesDelta(Time time) {
        if (isEqual(time)) {
            return 0;
        } else if (lessEqual(time)) {
            return 60 - minute + time.minute + (time.hour - hour - 1) * 60;
        } else {
            return time.minutesDelta(this);
        }
    }

    // Source: https://www.javatpoint.com/how-to-get-utc-time-in-java
    public static DateCompat getCurrentUtcDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Date d1;
        try {
            d1 = ldf.parse( sdf.format(new Date()) );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new DateCompat(d1);
    }
}
