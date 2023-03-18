package com.lsunae.search_app.di

import com.lsunae.search_app.api.ImageSearchService
import com.lsunae.search_app.data.repository.ImageSearchRepository
import com.lsunae.search_app.data.repository.ImageSearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideImageSearchRepository(
        imageSearchService: ImageSearchService
    ): ImageSearchRepository {
        return ImageSearchRepositoryImpl(imageSearchService)
    }
}