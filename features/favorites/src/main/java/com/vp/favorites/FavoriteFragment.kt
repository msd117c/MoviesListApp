package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vp.favorites.model.ListItem
import com.vp.favorites.viewmodel.ListState
import com.vp.favorites.viewmodel.FavoriteViewModel
import com.vp.favorites.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteFragment : Fragment(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    private var currentQuery: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        favoriteViewModel = ViewModelProviders.of(this, factory).get<FavoriteViewModel>(FavoriteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout)

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY)
        }

        initList()
        favoriteViewModel.source.observe(this, Observer { searchResult ->
            searchResult?.let { nonNullSearchResult ->
                listAdapter?.let { nonNullListAdapter ->
                    handleResult(nonNullListAdapter, nonNullSearchResult)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.resumeViewModel()
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.layoutManager = layoutManager

        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        recyclerView?.addOnScrollListener(gridPagingScrollListener!!)

        swipeRefreshLayout?.setOnRefreshListener { favoriteViewModel.refreshList() }
        swipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent)
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(progressBar) ?: 0
    }

    private fun showList() {
        swipeRefreshLayout?.isRefreshing = false
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(recyclerView) ?: 0
    }

    private fun showError() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(errorTextView) ?: 0
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
        gridPagingScrollListener?.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        val tempList = mutableListOf<ListItem>()
        tempList.addAll(searchResult.items)
        listAdapter.setItems(tempList)

        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }

        // Task-5: Add empty check to show feedback to user
        if (tempList.isEmpty()) {
            Toast.makeText(context, "Do you not like any movie? Try adding one to favorites!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter?.clearItems()
        favoriteViewModel.searchMoviesByTitle(query)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        if (imdbID.isEmpty()) {
            Toast.makeText(context, "ID is empty or null, please select other movie",
                    Toast.LENGTH_LONG).show()
            return
        }
        val intent: Intent
        try {
            intent = Intent(activity,
                    Class.forName("com.vp.detail.DetailActivity"))
            val uri = Uri.parse("app://movies/detail?imdbID=$imdbID")
            intent.data = uri
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            Toast.makeText(context, "Activity not found", Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        val TAG = "FavoriteFragment"
        internal val CURRENT_QUERY = "current_query"
    }
}
