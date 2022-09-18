package com.example.newsapp.data.remote.dto

data class NewsResponseDto(
    val articles: List<ArticleDto>?,
    val status: String?,
    val totalResults: Int?,
    val code:String?,
    val message:String?
)