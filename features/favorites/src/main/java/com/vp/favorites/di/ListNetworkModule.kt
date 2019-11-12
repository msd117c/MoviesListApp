package com.vp.favorites.di

import com.vp.favorites.service.SearchService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ListNetworkModule {

    @Provides
    internal fun providesSearchService(retrofit: Retrofit): SearchService = retrofit.create(SearchService::class.java)
}
