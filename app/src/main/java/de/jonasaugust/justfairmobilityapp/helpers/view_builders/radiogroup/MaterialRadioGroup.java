package de.jonasaugust.justfairmobilityapp.helpers.view_builders.radiogroup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons.ButtonDesigner;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

@SuppressWarnings("unused")
public class MaterialRadioGroup {

    private int selected;

    private final boolean onSurface;
    private boolean enabled = true;
    private String errorMessage;
    private final Context context;
    private final MaterialButton[] buttons;
    private final boolean icons;
    private final boolean invert;

    private final View.OnClickListener[] listenersChildren;
    private MaterialRadioGroupOnStateChangeListener listener;

    public MaterialRadioGroup(final int selectedButton, final boolean onSurface, final boolean invert, boolean icons, final View.OnClickListener[] listenersChildren, final Context context, final MaterialButton ... buttons){
        this.context = context;
        this.invert = invert;
        this.onSurface = onSurface;
        this.buttons = buttons;
        this.icons = icons;
        this.listenersChildren = listenersChildren;
        for (int i = 0; i< buttons.length; i++){
            final MaterialButton button = buttons[i];

            if (onSurface) ButtonDesigner.designButtonUnselectedOnSurface(button,invert,context);
            else ButtonDesigner.designButtonUnselectedOnPrimary(button,context);

            if (icons) for (MaterialButton b:buttons) {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_baseline_radio_button_unchecked_24);
                if (drawable != null) {
                    drawable.setAlpha(ButtonDesigner.ALPHA);
                }
                b.setIcon(drawable);
            }

            final int finalI = i;
            button.setOnClickListener(view -> {
                if (enabled||finalI == selected){
                    if (onSurface)ButtonDesigner.designButtonSelectedOnSurface(button,invert,context);
                    else ButtonDesigner.designButtonSelectedOnPrimary(button,context);
                    if (getSelected()!=finalI){
                        try {
                            if (onSurface)ButtonDesigner.designButtonUnselectedOnSurface(buttons[getSelected()],invert,context);
                            else ButtonDesigner.designButtonUnselectedOnPrimary(buttons[getSelected()],context);
                        }catch (Exception ignored){}
                    }
                    setSelected(finalI);
                    if (getListener()!=null)getListener().onChange(finalI);
                }else {
                    if (errorMessage!=null) ToastBuilder.show(context,errorMessage,false,true);
                }
                if (listenersChildren != null)try {
                    listenersChildren[finalI].onClick(view);
                }catch (ArrayIndexOutOfBoundsException ignore){}
            });
        }

        if (selectedButton>=0) {
            setSelectedButton(selectedButton);
            if (listenersChildren != null) listenersChildren[selectedButton].onClick(buttons[selectedButton]);
        }

        this.selected = selectedButton;
    }

    public void setSelectedButtonAndNotify(int position){
        setSelectedButton(position);
        if (listener!=null)listener.onChange(position);
        if (listenersChildren != null) listenersChildren[position].onClick(buttons[position]);
    }

    public void setSelectedButton(int position){
        if (onSurface)ButtonDesigner.designButtonSelectedOnSurface(buttons[position],invert,context);
        else ButtonDesigner.designButtonSelectedOnPrimary(buttons[position],context);
        if (getSelected()!=position){
            try {
                if (onSurface)ButtonDesigner.designButtonUnselectedOnSurface(buttons[getSelected()],invert,context);
                else ButtonDesigner.designButtonUnselectedOnPrimary(buttons[getSelected()],context);
            }catch (Exception ignored){}
        }
        setSelected(position);
    }

    private void setSelected(int i){
        if (icons) {
            try {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_baseline_radio_button_unchecked_24);
                if(drawable != null) drawable.setAlpha(ButtonDesigner.ALPHA);
                buttons[selected].setIcon(drawable);
            } catch (ArrayIndexOutOfBoundsException ignore) {}
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_baseline_radio_button_checked_24);
            if (drawable != null) drawable.setAlpha(ButtonDesigner.ALPHA);
            buttons[i].setIcon(drawable);
        }
        selected = i;
    }

    public int getSelected(){
        return selected;
    }

    public void setListener(MaterialRadioGroupOnStateChangeListener listener) {
        this.listener = listener;
    }

    public void setEnabled(boolean enabled, String errorMassage){
        this.enabled = enabled;
        this.errorMessage = errorMassage;
    }

    private MaterialRadioGroupOnStateChangeListener getListener(){
        return listener;
    }

    public void setVisibility(int visibility) {
        for (MaterialButton button : buttons) {
            button.setVisibility(visibility);
        }
    }
}