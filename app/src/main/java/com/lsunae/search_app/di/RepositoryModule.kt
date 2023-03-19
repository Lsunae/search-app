package com.lsunae.search_app.di

import com.lsunae.search_app.api.ImageSearchService
import com.lsunae.search_app.data.repository.image.ImageSearchRepository
import com.lsunae.search_app.data.repository.image.ImageSearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideImageSearchRepository(
        imageSearchService: ImageSearchService
    ): ImageSearchRepository {
        return ImageSearchRepositoryImpl(imageSearchService)
    }
}