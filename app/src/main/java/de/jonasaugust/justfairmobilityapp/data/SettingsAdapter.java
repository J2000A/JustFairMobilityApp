package de.jonasaugust.justfairmobilityapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.jonasaugust.justfairmobilityapp.R;

public class SettingsAdapter {

    private static final String SETTINGS = "DatabaseSettings";

    public enum Languages {
        EN, DE;

        @SuppressWarnings("SwitchStatementWithTooFewBranches")
        public String getLocaleString() {
            switch (this) {
                case EN:
                    return "en";
                default:
                    return "de";
            }
        }
    }

    private static final String SPRACHE = "sp";
    private static final String AM_PM_FORMAT = "amPm";
    private static final String DATE_FORMAT_WITH_YEAR = "dateFormatWithYear";
    private static final String DATE_FORMAT_WITHOUT_YEAR = "dateFormatWithoutYear";
    private static final String VERSION_INFO_COUNTER = "lastVersionInfoCount";
    private static final String NIGHT_MODE = "nightMode";

    private final Context context;
    private final SharedPreferences sp;

    public SettingsAdapter(Context context) {
        this.context = context;
        this.sp = getSP(context);
    }

    public boolean isAmPmFormat(){
        SharedPreferences sp = SettingsAdapter.this.sp;
        if (sp.contains(AM_PM_FORMAT)) return sp.getBoolean(AM_PM_FORMAT,false);
        else return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getDateFormat(boolean withYear, Context context){
        SharedPreferences sp = getSP(context);
            if (sp.contains(DATE_FORMAT_WITH_YEAR) && withYear) return new SimpleDateFormat(sp.getString(DATE_FORMAT_WITH_YEAR,context.getResources().getString(R.string.defaultDateFormatWithYear)));
            else if (sp.contains(DATE_FORMAT_WITHOUT_YEAR) && !withYear) return new SimpleDateFormat(sp.getString(DATE_FORMAT_WITHOUT_YEAR,context.getResources().getString(R.string.defaultDateFormatWithoutYear)));
            else if (!withYear) return new SimpleDateFormat(context.getResources().getString(R.string.defaultDateFormatWithoutYear));
            else return new SimpleDateFormat(context.getResources().getString(R.string.defaultDateFormatWithYear));
    }

    public static Context setSprache(Languages sprache, boolean updateSP, Context context){
        Locale locale = new Locale(sprache.getLocaleString());
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);

        if (updateSP){
            getSP(context).edit().putString(SPRACHE, sprache.getLocaleString()).apply();
            throw new RuntimeException("Update Notification Channel Language (See Meet and Eat)");
        }

        return context;
    }

    public static Languages getSprache(Context context){
        final String NULL = "null";
        String s;
        SharedPreferences sp = getSP(context);
        if (sp.contains(SPRACHE)) s = sp.getString(SPRACHE,NULL);
        else s = NULL;
        if (Languages.EN.getLocaleString().equals(s)) {
            return Languages.EN;
        }
        return Languages.DE;
    }

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    /**
     * @return 0 = auto; 1 = day; 2 = night
     */
    public static int getNightMode(Context context) {
        //TODO Change default to 0 when implementing night mode
        return getSP(context).getInt(NIGHT_MODE, 1);
    }

    public static void updateNightMode(int mode) {
        switch (mode) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

}