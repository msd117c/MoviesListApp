package com.vp.list

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener,
        ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listViewModel: ListViewModel
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    // Task 4: Add SwipeRefreshLayout variable
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currentQuery: String? = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        // Task 4: Set the swipe variable view
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout)

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY)
        }

        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(this, Observer { searchResult ->
            if (searchResult != null) {
                handleResult(listAdapter!!, searchResult)
            }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView?.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            val layoutManager = GridLayoutManager(context,
                    if (resources.configuration.orientation ==
                            Configuration.ORIENTATION_PORTRAIT) 2 else 3)
            this.layoutManager = layoutManager

            // Pagination
            gridPagingScrollListener = GridPagingScrollListener(layoutManager)
            gridPagingScrollListener?.setLoadMoreItemsListener(this@ListFragment)
            gridPagingScrollListener?.let { nonNullGridPagingScrollListener ->
                addOnScrollListener(nonNullGridPagingScrollListener)
            }
        }

        // Task 4: Add swipe refresh listener and set color accordingly to app theme
        swipeRefreshLayout?.setOnRefreshListener {
            listViewModel.searchMoviesByTitle(currentQuery, 1)
        }
        swipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent)
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(progressBar) ?: 0
    }

    private fun showList() {
        // Task 4: Set refreshing to false to hide swipeRefreshLayout's progressBar
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
        val tempList = mutableListOf<ListItem?>()
        tempList.addAll(searchResult.items)
        listAdapter.setItems(tempList)

        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener?.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter?.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        // Task 2: Open movie detail
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
        val TAG = "ListFragment"
        // Task 3: Open current query to be accessed from Activity
        internal val CURRENT_QUERY = "current_query"
    }
}