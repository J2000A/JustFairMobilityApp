package de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.dialogs.DialogBuilder;


@SuppressWarnings("unused")
public class SelectorButton {

    private int selectedItem;
    private String[] items;
    private final MaterialButton button;
    private final Context context;
    private final OnSelectionListener listener;

    public SelectorButton(final int[] itemsRes, final int selectedItem, final MaterialButton button, final Context context, final OnSelectionListener listener) {
        this(Arrays.stream(itemsRes).mapToObj(context::getString).toArray(String[]::new), selectedItem, button, context, listener);
    }

    public SelectorButton(final String[] items, final int selectedItem, final MaterialButton button, final Context context, final OnSelectionListener listener) {
        this.selectedItem = selectedItem;
        this.button = button;
        this.context = context;
        this.listener = listener;

        setItems(items);
    }

    public String getItems(int selectedItem) {
        return items[selectedItem];
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private final String[] strings;
        private final OnSelectionListener listener;

        static class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View layout, ViewGroup parent) {
                super(layout);
            }
        }

        MyAdapter(String[] strings, OnSelectionListener listener) {
            this.strings = strings;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_buttonselection, parent, false);
            return new MyViewHolder(itemView, parent);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            MaterialButton button = (MaterialButton) holder.itemView;
            button.setText(strings[position]);
            button.setOnClickListener(view -> listener.onItemSelected(position));
        }

        @Override
        public int getItemCount() {
            return strings.length;
        }

    }

    public void setItems(final String[] items) {
        this.items = items;
        button.setText(items[Math.max(selectedItem, 0)]);
        button.setOnClickListener(view -> {
            final DialogBuilder builder = new DialogBuilder(null,-1,context);

            RecyclerView recyclerView = new RecyclerView(context);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemViewCacheSize(3);
            recyclerView.setAdapter(new MyAdapter(items, new OnSelectionListener() {
                @Override
                public void onItemSelected(int position) {
                    builder.dismiss();
                    button.setText(items[position]);
                    SelectorButton.this.selectedItem = position;
                    if (listener != null) listener.onItemSelected(position);
                }
            }));

            builder.setContent(recyclerView);

            builder.findViewById(R.id.top).setVisibility(View.GONE);
            builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            builder.findViewById(R.id.scrollView).setPadding(0, (int) (40 * Resources.getSystem().getDisplayMetrics().density),0,0);

            builder.show();
        });
    }

    public abstract static class OnSelectionListener{
        public abstract void onItemSelected(int position);
    }

    public int getSelectedItem() {
        return selectedItem;
    }
}
