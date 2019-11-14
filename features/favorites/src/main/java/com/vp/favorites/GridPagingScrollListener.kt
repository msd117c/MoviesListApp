package com.vp.favorites

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener(private val layoutManager: GridLayoutManager)
    : RecyclerView.OnScrollListener() {
    private var loadMoreItemsListener: LoadMoreItemsListener = EMPTY_LISTENER
    private var isLastPage = false
    private var isLoading = false

    private val isNotFirstPage: Boolean
        get() =
            layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    private val isNotLoadingInProgress: Boolean
        get() = !isLoading

    private val nextPageNumber: Int
        get() = layoutManager.itemCount / PAGE_SIZE + 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (shouldLoadNextPage()) {
            loadMoreItemsListener.loadMoreItems(nextPageNumber)
        }
    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >=
                layoutManager.itemCount
    }

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    fun setLoadMoreItemsListener(loadMoreItemsListener: LoadMoreItemsListener) {
        this.loadMoreItemsListener = loadMoreItemsListener
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    companion object {
        private val PAGE_SIZE = 10
        private val EMPTY_LISTENER = object : LoadMoreItemsListener {
            override fun loadMoreItems(page: Int) {}
        }
    }
}
