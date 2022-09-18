package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.Article
import com.example.newsapp.utils.Resource

interface NewsRepository {

    suspend fun getMintLiveNews(pageNum: Int): Resource<List<Article>>
    suspend fun getSearchNews(searchQuery: String, pageNum: Int): Resource<List<Article>>
}