package de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts;

import android.content.Context;

public class ToastMessage extends Exception {

    public ToastMessage(String message) {
        super(message);
    }

    public ToastMessage(Context context, int stringRes) {
        super(context.getString(stringRes));
    }
}
