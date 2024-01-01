package de.jonasaugust.justfairmobilityapp.helpers.view_builders.texts;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.Converter;

public class TextErrorWatcher implements android.text.TextWatcher {

    private final EditText editText;
    private final boolean isDouble;
    private final String missing;
    private final String doubleString;
    private final boolean needed;

    public TextErrorWatcher(boolean needed, boolean isDouble, EditText editText, Context context) {
        this.isDouble = isDouble;
        this.editText = editText;
        this.needed = needed;
        missing = context.getString(R.string.error_missingText);
        doubleString = context.getString(R.string.error_wrongValue);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (needed && editable.toString().isEmpty()) {
            editText.setError(missing);
        } else if (isDouble && Converter.stringToDouble(editable.toString()) == null && (needed || !editable.toString().isEmpty())) {
            editText.setError(doubleString);
        } else {
            editText.setError(null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //nothing
    }

    public static boolean isNeededError(EditText editText, Context context){
        if (editText.getText() == null) editText.setText("");
        if (editText.getText().toString().trim().isEmpty()) {
            setError(editText, R.string.error_missingText, context);
            return true;
        }
        return false;
    }

    public static boolean isDoubleError(EditText editText, Context context){
        if (Converter.stringToDouble(editText.getText().toString()) == null) {
            setError(editText, R.string.error_missingText, context);
            return true;
        }
        return false;
    }

    /**
     * @return false
     */
    @SuppressWarnings("SameReturnValue")
    public static boolean setError(EditText editText, int msgResource, Context context){
        return setError(editText, context.getString(msgResource), context);
    }

    /**
     * @return false
     */
    @SuppressWarnings("SameReturnValue")
    public static boolean setError(EditText editText, String msg, Context context){
        editText.setError(msg);
        editText.requestFocus();
        editText.selectAll();
        context.getApplicationContext();
        /* Do not show Keyboard on error
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.RESULT_HIDDEN);
        imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
        */
        return false;
    }

    public static void attach(boolean needed, boolean isDouble, EditText editText, Context context) {
        TextErrorWatcher textErrorWatcher = new TextErrorWatcher(needed, isDouble, editText, context);
        editText.addTextChangedListener(textErrorWatcher);
    }
}