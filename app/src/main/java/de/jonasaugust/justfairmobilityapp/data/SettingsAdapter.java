package de.jonasaugust.justfairmobilityapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Pair;

import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.compatability.DateCompat;

public class SettingsAdapter {

    private static final String EINSTELLUNGEN = "DatabaseSettings";

    public enum Sprachen {
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
    private static final String DATE_FORMAT_MIT_JAHR = "dateFormatMitJahr";
    private static final String DATE_FORMAT_OHNE_JAHR = "dateFormatOhneJahr";
    private static final String DATE_FORMAT_SELECTED = "dateFormatSelected";
    private static final String VERSION_INFO_COUNTER = "lastVersionInfoCount";
    private static final String LAST_CONVERSION_ORDINAL = "lastConversionOrdinal";
    private static final String NIGHT_MODE = "nightMode";

    private final Context context;
    private final SharedPreferences sp;

    public SettingsAdapter(Context context) {
        this.context = context;
        this.sp = getSP(context);
    }

    void logout() {
        sp.edit().clear().apply();
    }

    public void setAmPmFormat(boolean amPm){
        sp.edit().putBoolean(AM_PM_FORMAT,amPm).apply();
    }

    public boolean isAmPmFormat(){
        SharedPreferences sp = SettingsAdapter.this.sp;
        if (sp.contains(AM_PM_FORMAT)) return sp.getBoolean(AM_PM_FORMAT,false);
        else return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getDateFormat(boolean mitJahr, Context context){
        SharedPreferences sp = getSP(context);
            if (sp.contains(DATE_FORMAT_MIT_JAHR) && mitJahr) return new SimpleDateFormat(sp.getString(DATE_FORMAT_MIT_JAHR,context.getResources().getString(R.string.defaultDateFormatWithYear)));
            else if (sp.contains(DATE_FORMAT_OHNE_JAHR) && !mitJahr) return new SimpleDateFormat(sp.getString(DATE_FORMAT_OHNE_JAHR,context.getResources().getString(R.string.defaultDateFormatWithoutYear)));
            else if (!mitJahr) return new SimpleDateFormat(context.getResources().getString(R.string.defaultDateFormatWithoutYear));
            else return new SimpleDateFormat(context.getResources().getString(R.string.defaultDateFormatWithYear));
    }

    public void setDateFormat(int selected) {
        final int YMD = 0;
        final int MDY = 1;
        final int DMY = 2;

        String dateFormatMitJahr;
        String dateFormatOhneJahr;
        switch (selected){
            case YMD:
                dateFormatMitJahr = "yyyy.MM.dd";
                dateFormatOhneJahr = "MMM d";
                break;
            case MDY:
                dateFormatMitJahr = "M.dd.yyyy";
                dateFormatOhneJahr = "MMM d";
                break;
            case DMY:
                dateFormatMitJahr = "d.MM.yyyy";
                dateFormatOhneJahr = "d. MMM";
                break;
            default:
                dateFormatMitJahr = context.getString(R.string.defaultDateFormatWithYear);
                dateFormatOhneJahr = context.getString(R.string.defaultDateFormatWithoutYear);
                break;

        }
        sp.edit().putString(DATE_FORMAT_MIT_JAHR,dateFormatMitJahr)
                .putString(DATE_FORMAT_OHNE_JAHR,dateFormatOhneJahr)
                .putInt(DATE_FORMAT_SELECTED,selected)
                .apply();
    }

    public static String getFormattedDateOHNEHeuteGestern(DateCompat date, boolean mitJahr, boolean hideYearWhenIsThisYear, Context context){
        if (date == null) return context.getString(R.string.selectDate);
        if (hideYearWhenIsThisYear&&mitJahr&&date.getYear()!=new DateCompat().getYear()) hideYearWhenIsThisYear = false;
        if (hideYearWhenIsThisYear)mitJahr = false;
        SimpleDateFormat simpleDateFormat = getDateFormat(mitJahr, context);
        return simpleDateFormat.format(new Date(date.getTime()));
    }

    public static Context setSprache(Sprachen sprache, boolean updateSP, Context context){
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

    public static Sprachen getSprache(Context context){
        final String NULL = "null";
        String s;
        SharedPreferences sp = getSP(context);
        if (sp.contains(SPRACHE)) s = sp.getString(SPRACHE,NULL);
        else s = NULL;
        if (Sprachen.EN.getLocaleString().equals(s)) {
            return Sprachen.EN;
        }
        return Sprachen.DE;
    }

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(EINSTELLUNGEN, Context.MODE_PRIVATE);
    }

    public boolean isNewVersion() {
        int lastVersionCode = sp.getInt(VERSION_INFO_COUNTER, -1);
        if (lastVersionCode == -1) {
            versionInfoRead();
            return false;
        }
        return lastVersionCode < context.getResources().getInteger(R.integer.version_info_counter);
    }

    public void versionInfoRead() {
        sp.edit().putInt(VERSION_INFO_COUNTER, context.getResources().getInteger(R.integer.version_info_counter)).apply();
    }

    public int getLastConversionOrdinal(int defaultOrdinal) {
        return sp.getInt(LAST_CONVERSION_ORDINAL, defaultOrdinal);
    }

    public void setLastConversionOrdinal(int ordinal) {
        sp.edit().putInt(LAST_CONVERSION_ORDINAL, ordinal).apply();
    }

    /**
     * @return 0 = auto; 1 = day; 2 = night
     */
    public static int getNightMode(Context context) {
        //TODO Change default to 0 when implementing night mode
        return getSP(context).getInt(NIGHT_MODE, 1);
    }

    /**
     * @param mode 0 = auto; 1 = day; 2 = night
     */
    public void setNightMode(int mode) {
        if (mode < 0 || mode > 2) throw new IllegalArgumentException();
        sp.edit().putInt(NIGHT_MODE, mode).apply();
        updateNightMode(mode);
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