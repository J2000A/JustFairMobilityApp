package de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;

import de.jonasaugust.justfairmobilityapp.R;

@SuppressWarnings("unused")
public class ButtonDesigner {

    public static final int ALPHA = 180;

    public static void designButtonSelectedOnSurface(MaterialButton button, boolean invert, Context context) {
        designButtonSelectedOnSurface(button, invert, true, context);
    }

    public static void designButtonSelectedOnSurface(MaterialButton button, boolean invert, boolean bold, Context context){
        if (invert) {
            designButtonUnselectedOnSurface(button, bold, context);
            if (bold) button.setTypeface(null, Typeface.BOLD);
            return;
        }
        ViewCompat.setBackgroundTintList(button, AppCompatResources.getColorStateList(context, R.color.colorSecondaryVariant));
        button.setTextColor(ContextCompat.getColor(context, R.color.colorOnPrimary));
        button.setIconTintResource(R.color.colorOnPrimary);
        if (bold) button.setTypeface(null, Typeface.BOLD);
    }

    public static void designButtonUnselectedOnSurface(MaterialButton button, boolean invert, Context context){
        if (invert) {
            designButtonSelectedOnSurface(button,false, true, context);
            button.setTypeface(null, Typeface.NORMAL);
            return;
        }
        ViewCompat.setBackgroundTintList(button,ContextCompat.getColorStateList(context, R.color.colorButtonOnSurface));
        button.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryVariant));
        button.setIconTintResource(R.color.colorSecondaryVariant);
        button.setTypeface(null, Typeface.NORMAL);
    }

    public static void designButtonSelectedOnPrimary(MaterialButton button, Context context){
        ViewCompat.setBackgroundTintList(button,ContextCompat.getColorStateList(context,R.color.colorSecondary));
        button.setTypeface(null, Typeface.BOLD);
    }

    public static void designButtonUnselectedOnPrimary(MaterialButton button, Context context){
        ViewCompat.setBackgroundTintList(button,ContextCompat.getColorStateList(context,R.color.colorSecondaryVariant));
        button.setTypeface(null, Typeface.NORMAL);
    }

    public static void designButtonCheckedOnSurface(MaterialButton button, int customDrawableId, boolean applyAlpha, boolean invert, Context context){
        designButtonSelectedOnSurface(button, invert, context);
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), customDrawableId!=-1?customDrawableId:R.drawable.icon_baseline_check_box_24, null);
        if (applyAlpha && drawable != null) drawable.setAlpha(ALPHA);
        button.setIcon(drawable);
        button.setTypeface(null, Typeface.NORMAL);
        button.setIconTintResource(R.color.colorOnPrimary);
    }

    public static void designButtonUncheckedOnSurface(MaterialButton button, int customDrawableId, boolean applyAlpha, boolean invert, Context context){
        designButtonUnselectedOnSurface(button, invert, context);
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), customDrawableId!=-1?customDrawableId:R.drawable.icon_baseline_check_box_outline_blank_24, null);
        if (applyAlpha && drawable != null)drawable.setAlpha(ALPHA);
        button.setIcon(drawable);
        button.setIconTintResource(R.color.colorSecondaryVariant);
    }

    public static void designButtonCheckedOnPrimary(MaterialButton button, Context context){
        designButtonSelectedOnPrimary(button,context);
        button.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_baseline_check_box_24, null));
        button.setTypeface(null, Typeface.NORMAL);
        button.setIconTintResource(R.color.colorOnPrimary);
    }

    public static void designButtonUncheckedOnPrimary(MaterialButton button, Context context){
        designButtonUnselectedOnPrimary(button,context);
        button.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_baseline_check_box_outline_blank_24, null));
        button.setIconTintResource(R.color.colorOnPrimary);
    }

    public static void designSpeichernButton(MaterialButton button, boolean readyToSave, Context context) {
            if (readyToSave) {
                ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(context, R.color.colorSecondary));
                button.setTextColor(ContextCompat.getColor(context, R.color.colorOnSecondary));
            } else {
                ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(context, R.color.colorSurface));
                button.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary));
            }
    }

    public static void designErrorButton(MaterialButton button, Context context) {
            ViewCompat.setBackgroundTintList(button,ContextCompat.getColorStateList(context,R.color.colorError));
            button.setTextColor(ContextCompat.getColor(context, R.color.colorOnError));
            button.setIconTintResource(R.color.colorOnError);
    }
}