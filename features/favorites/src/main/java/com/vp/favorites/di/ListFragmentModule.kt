package com.vp.favorites.di

import com.vp.favorites.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ListFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindListFragment(): ListFragment
}