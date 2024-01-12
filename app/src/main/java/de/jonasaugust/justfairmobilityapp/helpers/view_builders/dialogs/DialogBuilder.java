package de.jonasaugust.justfairmobilityapp.helpers.view_builders.dialogs;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.Converter;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons.ButtonDesigner;

@SuppressWarnings({"UnusedReturnValue"})
public class DialogBuilder {

    private final Context context;
    private final Dialog dialog;
    private final Button buttonRed;
    private final Button buttonLong;
    private final Button buttonShort;
    private final View buttonShortSpace;

    private boolean dismissible = true;
    private boolean confirmBack = false;

    public DialogBuilder(int titleId, int iconId, final Context context) {
        this(context.getString(titleId), iconId, context);
    }

    public DialogBuilder(String title, int iconID, final Context context) {
        this(title, false, false, iconID, context);
    }

    public DialogBuilder(int titleId, int iconId, boolean big, final Context context) {
        this(context.getString(titleId), iconId, big, context);
    }

    public DialogBuilder(String title, int iconId, boolean big, final Context context) {
        this(title, big, false, iconId, context);
    }

    private DialogBuilder(String title, boolean big, boolean cancelable, int iconID, final Context context){
        this.context = context;
        dialog = new Dialog(context){

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }

            @Override
            public void onBackPressed() {
                dismiss();
                super.onBackPressed();
            }

            @Override
            public void dismiss() {
                if (!dismissible) return;
                super.dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(cancelable);
        //noinspection DataFlowIssue
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (big) {
            dialog.setContentView(R.layout.dialog_big);
            dialog.getWindow().setLayout((int) (context.getResources().getDisplayMetrics().widthPixels * 0.99), (int) (context.getResources().getDisplayMetrics().heightPixels * 0.99));
        } else {
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setLayout((int) Math.min(Converter.dpToPx(350), context.getResources().getDisplayMetrics().widthPixels * 0.85), context.getResources().getDisplayMetrics().heightPixels);
        }

        if (!big) {
            ImageView imageView = dialog.findViewById(R.id.icon);
            if (iconID > 0) imageView.setImageResource(iconID);
            else imageView.setVisibility(View.GONE);
        } else if (iconID > 0) {
            throw new IllegalArgumentException("Icon ID is not available for big dialog");
        }

        buttonRed = dialog.findViewById(R.id.buttonRed);
        buttonRed.setVisibility(View.GONE);
        buttonLong = dialog.findViewById(R.id.buttonLong);
        buttonLong.setVisibility(View.GONE);
        buttonShort = dialog.findViewById(R.id.buttonShort);
        buttonShort.setVisibility(View.GONE);
        buttonShortSpace = dialog.findViewById(R.id.spaceButtons);
        buttonShortSpace.setVisibility(View.GONE);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBarTop);
        progressBar.setVisibility(View.GONE);

        ImageView button;
        if (big) {
            confirmBack = true;
            dialog.findViewById(R.id.save).setVisibility(View.GONE);
            button = dialog.findViewById(R.id.back);
        } else {
            button = dialog.findViewById(R.id.close);
        }
        button.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view1 = dialog.getCurrentFocus();
            if (view1 == null) {
                view1 = new View(context);
            }
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            dismissible = true;
            this.dismiss(false);
        });

        setTitle(title);
    }

    public static void showBackConfirmationDialog(Context context, View.OnClickListener onConfirmation) {
        DialogBuilder builder = new DialogBuilder(R.string.back_confirmation, -1, context);
        builder.addText(R.string.back_confirmation_text);
        builder.setButtonShort(R.string.cancel, true, null);
        builder.setButtonRed(R.string.back_confirmation_action, true, onConfirmation);
        builder.show();
    }

    public void dismiss() {
        dismiss(true);
    }

    @SuppressWarnings("SameParameterValue")
    private void dismiss(boolean forced) {
        if (forced) dismissible = true;
        if (confirmBack && !forced) {
            showBackConfirmationDialog(context, view -> dialog.dismiss());
            return;
        }
        dialog.dismiss();
    }

    public DialogBuilder setTitle(String s) {
        TextView textView = dialog.findViewById(R.id.title);
        textView.setText(s);
        return this;
    }

    public DialogBuilder setButtonShort(int titleRes, final boolean dismiss, final View.OnClickListener listener) {
        return setButtonShort(context.getString(titleRes), dismiss, listener);
    }

    public DialogBuilder setButtonShort(String title, final boolean dismiss, final View.OnClickListener listener) {
        buttonShort.setVisibility(View.VISIBLE);
        buttonShortSpace.setVisibility(View.VISIBLE);
        if (title != null && !title.equals("")) buttonShort.setText(title);
        buttonShort.setOnClickListener(view -> {
            if (listener!=null)listener.onClick(buttonShort);
            if (dismiss) {
                dismissible = true;
                this.dismiss();
            }
        });
        return this;
    }

    public DialogBuilder setButtonRed(int titleRes, final boolean dismiss, final View.OnClickListener listener) {
        return setButtonRed(context.getString(titleRes), dismiss, listener);
    }

    public DialogBuilder setButtonRed(String title, final boolean dismiss, final View.OnClickListener listener){
        buttonRed.setVisibility(View.VISIBLE);
        if (title!=null&&!title.equals("")) buttonRed.setText(title);
        buttonRed.setOnClickListener(view -> {
            if (listener!=null)listener.onClick(buttonRed);
            if (dismiss) {
                dismissible = true;
                this.dismiss();
            }
        });
        ViewCompat.setBackgroundTintList(buttonShort, ContextCompat.getColorStateList(context, R.color.midGrey));
        return this;
    }

    public DialogBuilder setContent(View view) {
        FrameLayout linearLayout = dialog.findViewById(R.id.content);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        //noinspection DataFlowIssue
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WRAP_CONTENT;
        linearLayout.addView(view, lp);
        return this;
    }

    public DialogBuilder show(){
        try {
            dialog.show();
            if (buttonShort.getVisibility() == View.GONE && buttonLong.getVisibility() == View.GONE && buttonRed.getVisibility() == View.GONE) {
                View buttons = dialog.findViewById(R.id.buttons);
                View content = dialog.findViewById(R.id.content);
                ScrollView scrollView = dialog.findViewById(R.id.scrollView);

                buttons.setBackgroundResource(R.color.colorSurface);
                buttons.setPadding(buttons.getPaddingLeft(), Converter.dpToPx(4), buttons.getPaddingRight(), buttons.getPaddingBottom());
                content.setPadding(content.getPaddingLeft(), content.getPaddingTop(), content.getPaddingRight(), 0);
                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    if (buttonShort.getVisibility() == View.GONE && buttonLong.getVisibility() == View.GONE && buttonRed.getVisibility() == View.GONE) {
                        boolean isScrollable = scrollView.getHeight() < content.getHeight() + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
                        if (isScrollable) {
                            buttons.setBackgroundResource(R.color.colorSurfaceVariant);
                        }
                    }
                });
            }
            return this;
        } catch (WindowManager.BadTokenException ignore) {
            // Activity was closed before Dialog could be shown
            return null;
        }
    }

    public static MaterialButton generateButton(String text, int iconRes, Context context, View.OnClickListener listener) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        @SuppressLint("InflateParams") MaterialButton button = (MaterialButton) LayoutInflater.from(context).inflate(R.layout.dialog_buttonselection,null,false);
        if (iconRes > 0) {
            Drawable drawable = ContextCompat.getDrawable(context, iconRes);
            if (drawable != null) {
                drawable.setAlpha(ButtonDesigner.ALPHA);
            }
            button.setIcon(drawable);
        }
        button.setText(text);
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(listener);
        return button;
    }

    public static TextView generateText(String text, boolean paddingTop, boolean paddingBottom, Context context) {
        TextView textView = new TextView(context);
        textView.setText(text);

        if (paddingBottom && paddingTop) {
            textView.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.small), 0,
                    context.getResources().getDimensionPixelSize(R.dimen.small));
        } else if (paddingBottom) {
            textView.setPadding(0, 0, 0, context.getResources().getDimensionPixelSize(R.dimen.small));
        } else if (paddingTop) {
            textView.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.small), 0, 0);
        }
        return textView;
    }

    public DialogBuilder addViewList(View ... views) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0,0,0,(int)context.getResources().getDimension(R.dimen.small));
        layout.setClipToPadding(false);
        layout.setClipChildren(false);
        for (View view : views) {
            if (view == null) continue;
            layout.addView(view);
        }
        setContent(layout);
        return this;
    }

    public DialogBuilder addText(int textRes) {
        addText(context.getString(textRes));
        return this;
    }

    public DialogBuilder addText(String text) {
        TextView textView = generateText(text, false, true, context);
        setContent(textView);
        return this;
    }

    public DialogBuilder setConfirmBack(boolean confirmBack) {
        this.confirmBack = confirmBack;
        return this;
    }

}
