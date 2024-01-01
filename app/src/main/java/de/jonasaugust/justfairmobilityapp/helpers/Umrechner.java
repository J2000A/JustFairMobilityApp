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