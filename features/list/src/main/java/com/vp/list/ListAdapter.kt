package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listItems: MutableList<ListItem?> = mutableListOf()
    private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
        override fun onItemClick(imdbID: String) {}
    }
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
        } else {
            LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_item_list, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListViewHolder) {
            listItems[position]?.let {
                val poster = it.poster
                if (poster != null && NO_IMAGE != poster) {
                    val density = holder.image.resources.displayMetrics.density
                    GlideApp
                            .with(holder.image)
                            .load(poster)
                            .override((300 * density).toInt(), (600 * density).toInt())
                            .into(holder.image)
                } else {
                    holder.image.setImageResource(R.drawable.placeholder)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listItems[position] == null) LOADING else ITEM
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: MutableList<ListItem?>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        onItemClickListener?.let { nonNullItemClickListener ->
            this.onItemClickListener = nonNullItemClickListener
            return
        }
        this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
    }

    internal inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition]?.imdbID ?: "")
        }
    }

    internal inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        private val NO_IMAGE = "N/A"
        private val ITEM = 1
        private val LOADING = 2
    }
}
