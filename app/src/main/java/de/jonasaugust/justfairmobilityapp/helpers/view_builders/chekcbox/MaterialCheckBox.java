package de.jonasaugust.justfairmobilityapp.helpers.view_builders.chekcbox;

import android.content.Context;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons.ButtonDesigner;


@SuppressWarnings("unused")
public class MaterialCheckBox {

    private static final int DEFAULT_ICON = -465;

    private boolean checked;
    private final MaterialButton button;
    private final boolean onSurface;
    private final Context context;
    private MaterialCheckBoxOnStateChangeListener listener;
    private MaterialCheckBox parent;
    private final MaterialCheckBox[] children;
    private int customDrawableIDChecked = DEFAULT_ICON;
    private int customDrawableIDUnchecked = DEFAULT_ICON;
    private boolean applyAlpha = false;
    private final boolean invert;

    public MaterialCheckBox(MaterialButton button, boolean checked, boolean onSurface, boolean invert, final MaterialCheckBox[] children, Context context){
        this.button = button;
        this.onSurface = onSurface;
        this.context = context;
        this.children = children;
        this.invert = invert;
        setChecked(checked,false);
        button.setOnClickListener(view -> {
            setChecked(!isChecked(),true);
            if (children!=null){
                for (MaterialCheckBox child : children) {
                    child.setChecked(isChecked(),true);
                }
            }
            if (parent!=null) parent.onChildChanged();
        });
        if (children!=null){
            for (MaterialCheckBox child : children){
                child.setParent(this);
            }
            onChildChanged();
        }
    }

    public MaterialCheckBox(MaterialButton button, int customDrawableIDChecked, int customDrawableIDUnchecked, boolean applyAlpha, boolean checked, boolean onSurface, boolean invert, final MaterialCheckBox[] children, Context context){
        this.button = button;
        this.invert = invert;
        this.onSurface = onSurface;
        this.context = context;
        this.children = children;
        this.customDrawableIDChecked = customDrawableIDChecked;
        this.customDrawableIDUnchecked = customDrawableIDUnchecked;
        this.applyAlpha = applyAlpha;
        setChecked(checked,false);
        button.setOnClickListener(view -> {
            setChecked(!isChecked(),true);
            if (children!=null){
                for (MaterialCheckBox child : children) {
                    child.setChecked(isChecked(),true);
                }
            }
            if (parent!=null) parent.onChildChanged();
        });
        if (children!=null){
            for (MaterialCheckBox child : children){
                child.setParent(this);
            }
            onChildChanged();
        }
    }

    private void onChildChanged() {
        if (children !=null){
            int amountChecked = 0;
            for (MaterialCheckBox child : children){
                if (child.isChecked())amountChecked++;
            }
            setChecked(amountChecked>0,true);
        }
    }

    private void setParent(MaterialCheckBox parent) {
        this.parent = parent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked, boolean notify) {
        this.checked = checked;
        if (onSurface){
            if (checked) {
                if (customDrawableIDChecked >= 0 || customDrawableIDChecked == DEFAULT_ICON) {
                    ButtonDesigner.designButtonCheckedOnSurface(button,customDrawableIDChecked,applyAlpha,invert,context);
                } else {
                    ButtonDesigner.designButtonSelectedOnSurface(button, false, context);
                }
            } else {
                if (customDrawableIDChecked >= 0 || customDrawableIDChecked == DEFAULT_ICON) {
                    ButtonDesigner.designButtonUncheckedOnSurface(button,customDrawableIDUnchecked,applyAlpha,invert,context);
                } else {
                    ButtonDesigner.designButtonUnselectedOnSurface(button, false, context);
                }
            }
        } else {
            if (checked) ButtonDesigner.designButtonCheckedOnPrimary(button,context);
            else ButtonDesigner.designButtonUncheckedOnPrimary(button,context);
        }
        if (notify&&listener!=null)listener.onChange(checked);
    }

    public void setListener(MaterialCheckBoxOnStateChangeListener listener) {
        this.listener = listener;
    }

    public boolean isChildrenChecked() {
        if (children==null||children.length==0)return false;
        for (MaterialCheckBox m:children) {
            if (m.isChecked())return true;
        }
        return false;
    }

    public boolean allUnchecked() {
        if (children==null)return !isChecked();
        for (MaterialCheckBox c : children) {
            if (c.isChecked()) return true;
        }
        return false;
    }

    public View getButton() {
        return button;
    }
}
