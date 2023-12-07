package de.jonasaugust.justfairmobilityapp.helpers;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.jonasaugust.justfairmobilityapp.helpers.compatability.DateCompat;

/*
This class was created by Jonas august outside working hours
*/

@SuppressWarnings("unused")
public class Zeit {

    public static final String TRENNER = ":";

    public static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000L;
    public static final int STUNDEN_AM_TAG = 24;

    private int minute;
    private int stunde;

    public static final int DAUER_TAG = 1;
    public static final int DAUER_WOCHE = 7;
    public static final int DAUER_MONAT = 30;
    public static final int DAUER_DREI_MONATE = 3 * DAUER_MONAT;
    public static final int DAUER_JAHR = 365;

    public static final int ANZAHL_WOCHEN_PRO_MONAT = 4;
    public static final int ANZAHL_MONATE_PRO_JAHR = 12;

    public Zeit(){
        DateCompat date = new DateCompat();
        this.minute = date.getMinutes();
        this.stunde = date.getHours();
    }

    public Zeit(int stunde, int minute) {
        this.minute = minute;
        this.stunde = stunde;
    }

    public Zeit(DateCompat date) {
        this.minute = date.getMinutes();
        this.stunde = date.getHours();
    }

    public Zeit(Zeit zeit) {
        this.minute = zeit.getMinute();
        this.stunde = zeit.getStunde();
    }

    public Zeit(Long timestamp) {
        this(new DateCompat(timestamp));
    }

    /**
     * @param s Format 00:00 oder 0000
     */
    public Zeit(String s) {
        if (s.contains(TRENNER)) {
            String[] strings = s.split(TRENNER);
            stunde = Integer.parseInt(strings[0]);
            minute = Integer.parseInt(strings[1]);
        } else {
            stunde = Integer.parseInt(s.substring(0, 2));
            minute = Integer.parseInt(s.substring(2));
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getStunde() {
        return stunde;
    }

    public void setStunde(int stunde) {
        this.stunde = stunde;
    }

    public DateCompat attachToDate(DateCompat date){
        date.setMinutes(minute);
        date.setHours(stunde);
        date.setSeconds(0);
        return date;
    }

    public boolean kleinerGleich(Zeit zeit){
        DateCompat date1 = attachToDate(new DateCompat());
        DateCompat date2 = zeit.attachToDate(new DateCompat());
        return date1.before(date2) || istGleich(zeit);
    }

    public boolean istGleich(Zeit zeit){
        return getMinute() == zeit.getMinute() && getStunde() == zeit.getStunde();
    }

    public void setZeit(Zeit zeit){
        minute = zeit.getMinute();
        stunde = zeit.getStunde();
    }

    public void setZeit(int stunde,int minute){
        this.minute = minute;
        this.stunde = stunde;
    }

    public void setZeit(DateCompat date){
        minute = date.getMinutes();
        stunde = date.getHours();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Zeit zeit = (Zeit) obj;
            return zeit.getStunde() == getStunde() && zeit.getMinute() == getMinute();
        }catch (Exception e){
            return super.equals(obj);
        }
    }

    public double zuDouble() {
        return getStunde() + (((double)getMinute())/60d);
    }

    @NonNull
    public String toString(){
        return toString(false);
    }

    public String toString(boolean leadingZeroHour) {
        return (leadingZeroHour && stunde <= 9 ? "0" : "") + stunde + TRENNER + (minute <= 9 ? "0" : "") + minute;
    }

    public int minutenDelta(Zeit zeit) {
        if (istGleich(zeit)) {
            return 0;
        } else if (kleinerGleich(zeit)) {
            return 60 - minute + zeit.minute + (zeit.stunde - stunde - 1) * 60;
        } else {
            return zeit.minutenDelta(this);
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
