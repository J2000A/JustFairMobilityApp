package de.jonasaugust.justfairmobilityapp.helpers;

import android.content.res.Resources;
import android.util.DisplayMetrics;

@SuppressWarnings("unused")
public class Converter {

    public static int pxToDp(int px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int dpToPx(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
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

}