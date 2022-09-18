package com.example.newsapp.di

import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.remote.RequestInterceptor
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsApi::class.java)
    }

    private fun getOkHttpClient() = OkHttpClient()
        .newBuilder()
        .addNetworkInterceptor(RequestInterceptor())
        .build()

    @Singleton
    @Provides
    fun getNewsRepository(newsApi: NewsApi, articleMapper: ArticleMapper): NewsRepository =
        NewsRepositoryImpl(newsApi, articleMapper)
}