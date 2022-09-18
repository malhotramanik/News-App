package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(value = "everything?domains=livemint.com&pageSize=10")
    suspend fun getLiveMintNews(@Query("page") pageNum: Int): NewsResponseDto

    @GET(value = "everything?domains=livemint.com&pageSize=10")
    suspend fun getSearchNews(@Query("q")searchQuery: String, @Query("page") pageNum: Int): NewsResponseDto

}