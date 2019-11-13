package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private var favoriteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this

        val movieId = intent?.data?.getQueryParameter("imdbID")
                ?: throw IllegalStateException("You must provide movie id to display details")

        detailViewModel.fetchDetails(movieId)
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.favorite().observe(this, Observer { favorite ->
            if (favorite) {
                favoriteItem?.setIcon(R.drawable.ic_star_fill)
            } else {
                favoriteItem?.setIcon(R.drawable.ic_star)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { nonNullItem ->
            when (nonNullItem.itemId) {
                R.id.star -> detailViewModel.toggleFavorite()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        favoriteItem = menu?.findItem(R.id.star)

        return true
    }
}
