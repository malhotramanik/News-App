package com.example.newsapp.data.repository

import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.remote.dto.NewsResponseDto
import com.example.newsapp.domain.models.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Constants.ERROR_MSG_HTTP_EXCEPTION
import com.example.newsapp.utils.Constants.ERROR_MSG_IO_EXCEPTION
import com.example.newsapp.utils.Resource
import retrofit2.HttpException
import java.io.IOException

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val articleMapper: ArticleMapper
) : NewsRepository {
    override suspend fun getMintLiveNews(pageNum:Int): Resource<List<Article>> {
        val newsResponse = newsApi.getLiveMintNews(pageNum)
        return parseResponse(newsResponse)
    }

    override suspend fun getSearchNews(searchQuery: String, pageNum: Int): Resource<List<Article>> {
        val newsResponse = newsApi.getSearchNews(searchQuery,pageNum)
        return parseResponse(newsResponse)
    }

    private fun parseResponse(newsResponse: NewsResponseDto): Resource<List<Article>> {
        return try {
            when (newsResponse.status) {
                "ok" -> {
                    val articles = newsResponse.articles?.map { articleMapper.dtoToArticle(it) }
                    Resource.Success(articles)
                }
                else -> Resource.Error(message = newsResponse.message)
            }

        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: ERROR_MSG_HTTP_EXCEPTION)
        } catch (e: IOException) {
            Resource.Error(message = ERROR_MSG_IO_EXCEPTION)
        }
        catch (e:Exception){
            Resource.Error(message = ERROR_MSG_HTTP_EXCEPTION)
        }
    }
}