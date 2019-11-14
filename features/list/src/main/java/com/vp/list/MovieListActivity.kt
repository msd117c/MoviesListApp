package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
    private val CURRENT_QUERY = "current_query"

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingActivityInjector

    private var searchView: SearchView? = null
    private var searchViewExpanded = true
    private var query: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                    .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            // Task 3: Retrieve current query
            query = savedInstanceState.getString(CURRENT_QUERY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu?.findItem(R.id.search)

        searchView = menuItem?.actionView as SearchView
        searchView?.let {
            it.apply {
                imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
                isIconified = searchViewExpanded
                maxWidth = Integer.MAX_VALUE
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        val listFragment = supportFragmentManager
                                .findFragmentByTag(ListFragment.TAG) as ListFragment
                        listFragment.submitSearchQuery(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })
                setOnCloseListener {
                    // Restore the default search if search view is closed without query
                    if (query.isEmpty()) {
                        val listFragment = supportFragmentManager
                                .findFragmentByTag(ListFragment.TAG) as ListFragment
                        listFragment.submitSearchQuery("Interview")
                    }
                    return@setOnCloseListener false
                }

                // Task 3: Set the query if it isn't null or empty
                this@MovieListActivity.query?.let { nonNullQuery ->
                    if (nonNullQuery.isNotEmpty()) {
                        setQuery(nonNullQuery, true)
                    }
                }
            }
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView?.isIconified ?: false)
        // Task 3: Save current query
        outState?.putString(CURRENT_QUERY, searchView?.query.toString())
    }

}