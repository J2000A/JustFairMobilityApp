package de.jonasaugust.justfairmobilityapp.helpers.view_builders.texts;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import de.jonasaugust.justfairmobilityapp.helpers.Converter;

/** @noinspection UnusedReturnValue*/
public class TextErrorWatcher implements android.text.TextWatcher {

    private EditText editText;
    private boolean isDouble;
    private String missing;
    private String doubleString;
    private boolean needed;

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

}