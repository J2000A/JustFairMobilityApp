package de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts;

/** @noinspection FieldCanBeLocal*/
public class ToastMessage extends Exception {

    private final String message;

    public ToastMessage(String message) {
        super(message);
        this.message = message;
    }

}
