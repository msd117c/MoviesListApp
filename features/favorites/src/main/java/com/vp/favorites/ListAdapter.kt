package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vp.favorites.model.ListItem

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private val NO_IMAGE = "N/A"
    private var listItems: MutableList<ListItem> = mutableListOf()
    private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
        override fun onItemClick(imdbID: String) {}

    }
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]

        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            Glide
                    .with(holder.image)
                    .load(listItem.poster)
                    .apply(RequestOptions().override((300 * density).toInt(),
                            (600 * density).toInt()))
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: MutableList<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        onItemClickListener?.let { nonNullListener ->
            this.onItemClickListener = nonNullListener
            return
        }
        this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }
}
