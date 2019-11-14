package com.vp.favorites

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

class FavoriteActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        const val CURRENT_QUERY = "current_query"
    }

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var searchView: SearchView
    private var searchViewExpanded = true
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, FavoriteFragment(), FavoriteFragment.TAG)
                    .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            query = savedInstanceState.getString(CURRENT_QUERY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu?.findItem(R.id.search)

        searchView = menuItem?.actionView as SearchView
        searchView.apply {
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            isIconified = searchViewExpanded
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { nonNullQuery ->
                        val listFragment = supportFragmentManager
                                .findFragmentByTag(FavoriteFragment.TAG) as FavoriteFragment
                        listFragment.submitSearchQuery(nonNullQuery)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
            this@FavoriteActivity.query?.let { nonNullQuery ->
                setQuery(nonNullQuery, true)
            }
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
        outState.putString(CURRENT_QUERY, searchView.query.toString())
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector;
    }
}
