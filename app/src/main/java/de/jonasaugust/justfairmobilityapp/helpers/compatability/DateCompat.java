package de.jonasaugust.justfairmobilityapp.helpers.compatability;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@SuppressWarnings("unused")
public class DateCompat implements Comparable<DateCompat> {

    private final Calendar c = Calendar.getInstance();

    public DateCompat() {
        c.setTime(new Date());
    }

    public DateCompat(Calendar calendar) {
        c.setTimeInMillis(calendar.getTimeInMillis());
    }

    public DateCompat(Date date) {
        c.setTime(date);
    }

    public DateCompat(long date) {
        c.setTimeInMillis(date);
    }

    public DateCompat(int year, int month, int date) {
        c.set(year + 1900, month, date);
    }

    public DateCompat(int year, int month, int date, int hrs, int min) {
        c.set(year + 1900, month, date, hrs, min);
    }

    public DateCompat(int year, int month, int date, int hrs, int min, int sec) {
        c.set(year + 1900, month, date, hrs, min, sec);
    }

    public DateCompat(String s) throws ParseException {
        DateFormat df = DateFormat.getDateInstance();
        Date date = df.parse(s);
        if (date == null) throw new ParseException("Created Date was null", 0);
        c.setTimeInMillis(date.getTime());
    }

    /**
     * @return DAY_OF_MONTH
     */
    public int getDate() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return DAY_OF_WEEK
     */
    public int getDay() {
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int getHours() {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        return c.get(Calendar.MINUTE);
    }

    public int getMonth() {
        return c.get(Calendar.MONTH);
    }

    public int getSeconds() {
        return c.get(Calendar.SECOND);
    }

    public int getTimezoneOffset() {
        return c.get(Calendar.DST_OFFSET);
    }

    public int getYear() {
        return c.get(Calendar.YEAR) - 1900;
    }

    public static long parse(String s) throws ParseException {
        DateFormat df = DateFormat.getInstance();
        Date date = df.parse(s);
        if (date == null) throw new ParseException("Created Date was null", 0);
        return date.getTime();
    }

    /**
     * @param date DAY_OF_MONTH
     */
    public void setDate(int date) {
        c.set(Calendar.DAY_OF_MONTH, date);
    }

    public void setHours(int hours) {
        c.set(Calendar.HOUR_OF_DAY, hours);
    }

    public void setMinutes(int minutes) {
        c.set(Calendar.MINUTE, minutes);
    }

    public void setMonth(int month) {
        c.set(Calendar.MONTH, month);
    }

    public void setSeconds(int seconds) {
        c.set(Calendar.SECOND, seconds);
    }

    public void setYear(int year) {
        c.set(Calendar.YEAR, year + 1900);
    }

    public String toGMTString() {
        Date date = new Date(c.getTime().getTime());
        DateFormat df = DateFormat.getInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    public String toLocaleString() {
        Date date = new Date(c.getTime().getTime());
        DateFormat df = DateFormat.getInstance();
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static long UTC(int year, int month, int date, int hrs, int min, int sec) {
        Calendar c = Calendar.getInstance();
        c.set(year + 1900, month, date, hrs, min, sec);
        return c.getTime().getTime();
    }

    @NonNull
    public Object clone() {
        return new DateCompat(c.getTimeInMillis());
    }

    public long getTime() {
        return c.getTimeInMillis();
    }

    public void setTime(long time) {
        c.setTimeInMillis(time);
    }

    public boolean before(Date when) {
        Calendar whenCalendar = Calendar.getInstance();
        whenCalendar.setTime(when);
        return c.before(whenCalendar);
    }

    public boolean before(DateCompat when) {
        return c.before(when.getCalendar());
    }

    public boolean after(Date when) {
        Calendar whenCalendar = Calendar.getInstance();
        whenCalendar.setTime(when);
        return c.after(whenCalendar);
    }

    public boolean after(DateCompat when) {
        return c.after(when.getCalendar());
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        long compareMillis;
        if (obj instanceof Date) {
            compareMillis = ((Date) obj).getTime();
        } else if (obj instanceof DateCompat) {
            compareMillis = ((DateCompat) obj).getTime();
        } else if (obj instanceof Calendar) {
            compareMillis = getTime();
        } else {
            return false;
        }
        return c.getTimeInMillis() == compareMillis;
    }

    public int compareTo(Date anotherDate) {
        Calendar anotherCalendar = Calendar.getInstance();
        anotherCalendar.setTime(anotherDate);
        return c.compareTo(anotherCalendar);
    }

    public int compareTo(DateCompat anotherDateCompat) {
        return c.compareTo(anotherDateCompat.getCalendar());
    }

    public int compareTo(Calendar anotherCalendar) {
        return c.compareTo(anotherCalendar);
    }

    @Override
    public int hashCode() {
        return c.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return c.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Instant toInstant() {
        return c.toInstant();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DateCompat from(Instant instant) {
        Date date = Date.from(instant);
        return new DateCompat(date);
    }

    public Calendar getCalendar() {
        return (Calendar) c.clone();
    }

    public DateCompat toUtcDate() {
        return new DateCompat(getTime() - new GregorianCalendar().getTimeZone().getOffset(System.currentTimeMillis()));
    }
}
