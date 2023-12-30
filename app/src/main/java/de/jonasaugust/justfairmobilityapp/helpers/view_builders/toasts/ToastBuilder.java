package de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import de.jonasaugust.justfairmobilityapp.R;

public class ToastBuilder {

    private static Toast create(Context context, int StringID, boolean shortDuration, boolean error){
        if (error && Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            Toast toast = Toast.makeText(context,StringID,shortDuration?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
            toast.getView().getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorError), PorterDuff.Mode.SRC_IN);
            ((TextView)toast.getView().findViewById(android.R.id.message)).setTextColor(ContextCompat.getColor(context, R.color.colorOnError));
            return toast;
        }else try {
            return Toast.makeText(context,StringID,shortDuration?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
        } catch (NullPointerException ignore) {return null;}
    }

    private static Toast create(Context context, String string, boolean shortDuration, boolean error){
        if (error && Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            Toast toast = Toast.makeText(context,string,shortDuration?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
            toast.getView().getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorError), PorterDuff.Mode.SRC_IN);
            ((TextView)toast.getView().findViewById(android.R.id.message)).setTextColor(ContextCompat.getColor(context, R.color.colorOnError));
            return toast;
        } else try {
            return Toast.makeText(context,string,shortDuration?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
        } catch (NullPointerException ignore) {return null;}
    }

    public static void show(Context context, int StringID, boolean shortDuration, Exception exception){
        if (exception == null) {
            Toast toast = create(context, StringID, shortDuration, false);
            if (toast != null) toast.show();
        } else {
            show(context, exception);
        }
    }

    public static void show(Context context, int StringID, boolean shortDuration, boolean error){
        Toast toast = create(context, StringID, shortDuration, error);
        if (toast != null) {
            toast.show();
        } else {
            Log.println(Log.ERROR, "TOAST Error was null", "TOAST Error was null");
        }
    }

    public static void show(Context context, String string, boolean shortDuration, boolean error){
        Toast toast = create(context, string, shortDuration, error);
        if (toast != null) {
            toast.show();
        } else {
            Log.println(Log.ERROR, "TOAST Error was null", "TOAST Error was null");
        }
    }

    public static void show(Context context, int stringId) {
        show(context, stringId, true, false);
    }

    public static void show(Context context, Exception e){
        if (e == null) return;

        String errorString = getErrorString(context, e);

        if (errorString == null) return;

        Toast toast = create(context, errorString,false,true);
        if (toast != null) {
            toast.show();
        }
    }

    private final static String IGNORE_EXCEPTION = "IGNORE_EXCEPTION";

    /**
     * @return null if it should be ignored
     */
    public static String getErrorString(Context context, Exception e) {
        if (e == null) return null;

        if (e instanceof ToastMessage) {
            return e.getMessage();
        }
        if (e instanceof UnknownHostException) {
            return context.getString(R.string.error_unknown_host);
        }
        if (e instanceof ConnectException
                || (e instanceof SocketException && e.getMessage() != null && e.getMessage().equals("Software caused connection abort"))) {
            return context.getString(R.string.error_connection);
        }
        if (e instanceof FileNotFoundException) {
            return context.getString(R.string.error_file_not_found);
        }
        if (e instanceof SocketTimeoutException) {
            return context.getString(R.string.error_timeout);
        }
        if (e instanceof SSLException) {
            return context.getString(R.string.error_ssl);
        }
        if (e instanceof IOException && e.getMessage() != null && e.getMessage().equals("{\"httpstatuscode\":401,\"error\":\"Unauthorized\"}")) {
            return context.getString(R.string.error_http_401);
        }
        if (e instanceof ActivityNotFoundException) {
            return context.getString(R.string.error_activity_not_found);
        }

        if (e.getMessage() != null && e.getMessage().equals(IGNORE_EXCEPTION)) {
            return null;
        } else {
            return e.getMessage();
        }
    }
}

