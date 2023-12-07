package de.jonasaugust.justfairmobilityapp.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.SettingsAdapter;
import de.jonasaugust.justfairmobilityapp.helpers.compatability.DateCompat;

@SuppressWarnings("unused")
public class Umrechner {

    public static String datumZuHeuteGesternMorgenDateFormat(Calendar calendar, Context context) {
        return datumZuHeuteGesternMorgenDateFormat(new DateCompat(calendar), context);
    }

    @SuppressLint("SimpleDateFormat")
    public static String datumZuHeuteGesternMorgenDateFormat(DateCompat date, Context context) {
        DateCompat jetzt = new DateCompat();
        String s;

        if (datesOnSameDay(date, jetzt)){
            s = context.getResources().getString(R.string.today);
        } else if (date.getYear()==jetzt.getYear() && date.getMonth()==jetzt.getMonth() && date.getDate() == jetzt.getDate() -1 ){
            s = context.getResources().getString(R.string.yesterday);
        } else if (date.getYear()==jetzt.getYear() && date.getMonth()==jetzt.getMonth() && date.getDate() == jetzt.getDate() + 1) {
            s = context.getString(R.string.tomorrow);
        } else {
            s = SettingsAdapter.getFormattedDateOHNEHeuteGestern(date,true,true, context);
        }
        return s;
    }

    public static String zeitToString(Zeit zeit, boolean allowMidnight, Context context){
        if (zeit == null || (!allowMidnight && zeit.getStunde() == 0 && zeit.getMinute() == 0)) return "--" + context.getResources().getString(R.string.separation_time) + "--";
        String minuteString;
        if (zeit.getMinute()<10){
            minuteString = "0" + zeit.getMinute();
        }else {
            minuteString = zeit.getMinute() + "";
        }
        if (!new SettingsAdapter(context).isAmPmFormat()){
            return zeit.getStunde() + context.getResources().getString(R.string.separation_time) + minuteString;
        }else {
            boolean istAm = zeit.getStunde() <= 12;
            if (istAm){
                return zeit.getStunde() + context.getResources().getString(R.string.separation_time) + minuteString + " " +context.getResources().getString(R.string.am);
            }else {
                zeit.setStunde(zeit.getStunde()-12);
                return zeit.getStunde() + context.getResources().getString(R.string.separation_time) + minuteString + " " + context.getResources().getString(R.string.pm);
            }
        }
    }

    public static Double stringToDouble(String s) {
        if (s.isEmpty()) return null;
        s = s.replace(',','.');
        Double d;
        try {
            d = Double.parseDouble(s);
        }catch (Exception e){
            d = null;
        }
        return d;
    }

    public static String doubleToString(double wert, Context context){
        int[] vorNachKomma = doubleToVorNachKomma(wert);
        if (vorNachKomma[1]!=0)return vorNachKomma[0] + context.getString(R.string.separation_doubles) + vorNachKomma[1];
        return vorNachKomma[0] + "";
    }

    public static int[] doubleToVorNachKomma(double value) {
        int[] ints = new int[2];
        ints[0] = (int) value;
        ints[1] = rundenMitteAufInt((value%1)*10);
        return ints;
    }

    public static int rundenMitteAufInt(double value){return (int) (value + 0.5);}

    public static int pxToDp(int px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static Calendar dateZuCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static int dpToPx(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static String[] sortStringAlphabetically(String[] x){
        int j;
        boolean flag = true;
        String temp;

        while ( flag )
        {
            flag = false;
            for ( j = 0;  j < x.length - 1;  j++ )
            {
                if ( x [ j ].compareToIgnoreCase( x [ j+1 ] ) > 0 )
                {
                    temp = x [ j ];
                    x [ j ] = x [ j+1];
                    x [ j+1] = temp;
                    flag = true;
                }
            }
        }
        return x;
    }

    public static boolean isTextEmpty(String s) {
        if (s == null) return true;
        return s.isEmpty();
    }

    public static String minutenZuStundenMinutenStringOhneSekunden(int minuten, Context context){
        int minutenOhneStunden = minuten%60;
        int stunden = (minuten-minutenOhneStunden)/60;
        if (stunden==0){
            return minuten + " " + context.getResources().getQuantityString(R.plurals.minutes,minuten);
        }
        String extraNull = "";
        if (minutenOhneStunden<10) extraNull = "0";
        return stunden + context.getString(R.string.separation_time) + extraNull + minutenOhneStunden + " " + context.getResources().getQuantityString(R.plurals.hours, minuten == 0 ? stunden: 42);
    }

    public static String minutenZuStundenMinutenStringMitSekunden(int sekunden, Context context){
        if (sekunden<60) return sekunden + " " + context.getResources().getQuantityString(R.plurals.seconds,sekunden);
        int minuten = sekunden/60;
        int minutenOhneStunden = minuten%60;
        int stunden = (minuten-minutenOhneStunden)/60;
        if (stunden==0){
            return minuten + " " + context.getResources().getQuantityString(R.plurals.minutes,minuten);
        }
        String extraNull = "";
        if (minutenOhneStunden<10) extraNull = "0";
        return stunden + context.getString(R.string.separation_time) + extraNull + minutenOhneStunden + " " + context.getResources().getQuantityString(R.plurals.hours,stunden);
    }

    public static String sekundenZuMinutenSekundenString(long sekunden, Context context) {
        long sekundenOhneMinuten = sekunden%60;
        int minuten = (int) (sekunden-sekundenOhneMinuten)/60;
        if (minuten==0)return sekundenOhneMinuten + " " + context.getResources().getQuantityString(R.plurals.seconds,(int) sekundenOhneMinuten);
        String extraNull = "";
        if (sekundenOhneMinuten<10) extraNull = "0";
        if (sekundenOhneMinuten!=0)return minuten + context.getString(R.string.separation_time) + extraNull + sekundenOhneMinuten + " " + context.getResources().getQuantityString(R.plurals.minutes,minuten);
        else return minuten + " " + context.getResources().getQuantityString(R.plurals.minutes,minuten);
    }

    public static double rundenAb(double value) {
        return Math.round(value*1000000d)/1000000d;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int newSize){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth;
        int newHeight;

        if(width > height){
            newWidth = newSize;
            newHeight = (newSize * height)/width;
        } else if(width < height){
            newHeight = newSize;
            newWidth = (newSize * width)/height;
        } else {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static boolean isInvalidEmailAddress(String target) {
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String baseUrlRelativeUrlToAbsoluteUrl(@NonNull String baseUrl, @NonNull String relativeUrl) throws ArrayIndexOutOfBoundsException {
        // Check if relative URl is actually an absolute URL
        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://") || relativeUrl.startsWith("ftp://")) {
            return relativeUrl;
        }

        // Separate the baseUrl
        String protocol = baseUrl.split("://", 2)[0];
        String baseRoot = baseUrl.split("://", 2)[1].split("/", 2)[0];
        String basePath = null;
        try {
            basePath = baseUrl.split("://", 2)[1].split("/", 2)[1];
        } catch (IndexOutOfBoundsException ignore) {}

        // Split base path in Elements
        List<String> basePathElements = new ArrayList<>();
        if (basePath != null) basePathElements = Arrays.stream(basePath.split("/")).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
        // Remove the last Element pointing to the current document
        if (basePathElements.size() > 0) basePathElements.remove(basePathElements.size() - 1);

        // Split relative Url in Elements
        List<String> relativeUrlElements = Arrays.stream(relativeUrl.split("/")).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());

        for (String element : relativeUrlElements) {
            if (element.equals(".."))
                // If .. go back one Element
                basePathElements.remove(basePathElements.size()-1);
            else if (!element.equals("."))
                // If . stay in directory else move to relative Element
                basePathElements.add(element);
        }

        // Recreate Absolute URL
        StringBuilder output = new StringBuilder(protocol + "://" + baseRoot);
        for (String element : basePathElements) {
            output.append("/").append(element);
        }

        return output.toString();
    }

    public static int getAverageBitmapColor(Bitmap bitmap, int skippedPixels) {
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y+=Math.max(1, skippedPixels)) {
            for (int x = 0; x < bitmap.getWidth(); x+=Math.max(1, skippedPixels)) {
                int pixel = bitmap.getPixel(x, y);
                pixelCount++;
                redBucket += Color.red(pixel);
                greenBucket += Color.green(pixel);
                blueBucket += Color.blue(pixel);
            }
        }

        return Color.rgb(Math.toIntExact(redBucket / pixelCount), Math.toIntExact(greenBucket / pixelCount), Math.toIntExact(blueBucket / pixelCount));
    }

    public static String md5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        char[] hexChars = new char[digest.length * 2];
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        for (int j = 0; j < digest.length; j++) {
            int v = digest[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getWeekdayString(Calendar calendar, Context context) {
        return getWeekdayString(new DateCompat(calendar), context);
    }

    public static String getWeekdayString(DateCompat date, Context context) {
        Calendar calendar = date.getCalendar();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: return context.getString(R.string.monday);
            case Calendar.TUESDAY: return context.getString(R.string.tuesday);
            case Calendar.WEDNESDAY: return context.getString(R.string.wednesday);
            case Calendar.THURSDAY: return context.getString(R.string.thursday);
            case Calendar.FRIDAY: return context.getString(R.string.friday);
            case Calendar.SATURDAY: return context.getString(R.string.saturday);
            case Calendar.SUNDAY: return context.getString(R.string.sunday);
            default: return null;
        }
    }

    public static boolean datesOnSameDay(Calendar c1, Calendar c2) {
        return datesOnSameDay(new DateCompat(c1), new DateCompat(c2));
    }

    public static boolean datesOnSameDay(DateCompat d1, Date d2) {
        return datesOnSameDay(d1, new DateCompat(d2));
    }

    public static boolean datesOnSameDay(DateCompat d1, DateCompat d2) {
        return d1.getYear() == d2.getYear()
                && d1.getMonth() == d2.getMonth()
                && d1.getDate() == d2.getDate();
    }

    public static boolean isInvalidICAOPlane(String callsign) {
        if (callsign == null) return true;
        String[] strings = callsign.trim().split("-",2);
        if (strings.length != 2) return true;
        if (strings[0].length() == 0 || strings[0].length() > 2) return true;
        for (char c : strings[0].toLowerCase().toCharArray()) {
            if (c >= 'a' && c<= 'z') continue;
            return true;
        }
        if (strings[1].length() != 4) return true;
        for (char c : strings[1].toLowerCase().toCharArray()) {
            if ((c >= 'a' && c<= 'z') || (c >= '0' && c <= '9')) continue;
            return true;
        }
        return false;
    }

    public static boolean isInvalidIcaoFieldId(String icaoId) {
        if (icaoId == null) return true;
        if (icaoId.length() != 4) return true;
        for (char c : icaoId.toCharArray()) {
            if (c >= 'A' && c<= 'Z') continue;
            return true;
        }
        return false;
    }

    public static boolean isInvalidPhoneNumber(String phone) {
        if (phone == null) return true;
        char[] chars = phone.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= '0' && chars[i]<= '9') continue;
            if (i == 0 && chars[i] == '+') continue;
            return true;
        }
        return false;
    }

    public static int kmhToKt(int kmh) {
        return (int) (kmh / 1.852d + 0.5);
    }

    public static int ktToKmh(int kmh) {
        return (int) (kmh * 1.852d + 0.5);
    }

    public static int ftToM(int ft) {
        return (int) (ft / 3.280839895 + 0.5);
    }

    public static int mToFt(int m) {
        return (int) (m * 3.280839895 + 0.5);
    }

    public static String toSeedString(int seed) {
        seed = Math.abs(seed);
        if (seed < 10)
            return "000" + seed;
        if (seed < 100)
            return "00" + seed;
        if (seed < 1000)
            return "0" + seed;
        return String.valueOf(seed);

    }

    public static boolean isInvalidOTP(String secret) {
        if (secret == null || secret.isEmpty()) return true;
        char[] chars = secret.toCharArray();
        if (chars.length != 6) return true;
        for (char aChar : chars) {
            if (aChar >= '0' && aChar <= '9') continue;
            return true;
        }
        return false;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}