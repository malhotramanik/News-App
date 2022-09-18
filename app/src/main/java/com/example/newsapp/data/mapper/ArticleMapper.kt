package com.example.newsapp.data.mapper

import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.domain.models.Article
import javax.inject.Inject

class ArticleMapper @Inject constructor() {

    fun dtoToArticle(articleDto: ArticleDto) = Article(
        author = articleDto.author,
        content = articleDto.content,
        description = articleDto.description,
        publishedAt = articleDto.publishedAt,
        source = articleDto.source.name,
        title = articleDto.title,
        url = articleDto.url,
        urlToImage = articleDto.urlToImage
    )
}