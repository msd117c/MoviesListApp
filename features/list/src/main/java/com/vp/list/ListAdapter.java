package com.vp.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vp.list.model.ListItem;

import java.util.Collections;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private static final int ITEM = 1;
    private static final int LOADING = 2;
    private List<ListItem> listItems = Collections.emptyList();
    private OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = imdbID -> {
        //empty listener
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        } else {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            ListItem listItem = listItems.get(position);

            if (listItem.getPoster() != null && !NO_IMAGE.equals(listItem.getPoster())) {
                final float density = listViewHolder.image.getResources().getDisplayMetrics().density;
                GlideApp
                        .with(listViewHolder.image)
                        .load(listItem.getPoster())
                        .override((int) (300 * density), (int) (600 * density))
                        .into(listViewHolder.image);
            } else {
                listViewHolder.image.setImageResource(R.drawable.placeholder);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position) == null ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setItems(List<ListItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    public void clearItems() {
        listItems.clear();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.poster);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }

    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
