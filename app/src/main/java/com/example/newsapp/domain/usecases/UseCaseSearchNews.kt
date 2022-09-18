package com.example.newsapp.domain.usecases

import com.example.newsapp.domain.models.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseSearchNews @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(searchQuery: String, pageNum: Int): Flow<Resource<List<Article>>> =
        flow { emit(newsRepository.getSearchNews(searchQuery, pageNum)) }
}