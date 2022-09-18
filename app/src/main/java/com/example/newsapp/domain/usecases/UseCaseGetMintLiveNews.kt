package com.example.newsapp.domain.usecases

import com.example.newsapp.domain.models.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseGetMintLiveNews @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(pageNum:Int): Flow<Resource<List<Article>>> =
        flow { emit(newsRepository.getMintLiveNews(pageNum)) }
}