package com.example.newsapp.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.models.Article
import com.example.newsapp.domain.usecases.UseCaseGetMintLiveNews
import com.example.newsapp.domain.usecases.UseCaseSearchNews
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetMintLiveNews: UseCaseGetMintLiveNews,
    private val useCaseSearchNews: UseCaseSearchNews
) : ViewModel() {

    var isSearchEnabled: Boolean = false
    private val _listStateFlow = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val listStateFlow = _listStateFlow.asStateFlow()

    private val _pageNum = MutableStateFlow<Int>(1)
    private var maxPage = 10

    private val _listArticles: MutableList<Article> = mutableListOf()

    private val _sListStateFlow = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val sListStateFlow = _sListStateFlow.asStateFlow()

    private val _sPageNum = MutableStateFlow<Int>(1)
    private var sMaxPage = 10

    private val _sListArticles: MutableList<Article> = mutableListOf()

    init {
        getLiveMintNews()
    }

    private fun getLiveMintNews() {
        viewModelScope.launch {
            _pageNum.collectLatest { pageNum ->
                _listStateFlow.emit(Resource.Loading())
                useCaseGetMintLiveNews(pageNum).onEach { result ->
                    _listStateFlow.emit(
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { _listArticles.addAll(it) }
                                Resource.Success(_listArticles.toList())
                            }
                            else -> result
                        }
                    )
                }.launchIn(viewModelScope)
            }
        }
    }

    fun nextPage() {
        val currentPage = _pageNum.value
        if (currentPage < maxPage)
            viewModelScope.launch {
                _pageNum.emit(currentPage + 1)
            }
    }

    fun nextSearchPage() {
        val currentPage = _sPageNum.value
        if (currentPage < sMaxPage)
            viewModelScope.launch {
                _sPageNum.emit(currentPage + 1)
            }
    }

    fun performSearch(searchQuery: String) {
        _sPageNum.value = 1
        viewModelScope.launch {
            _sPageNum.collectLatest { pageNum ->
                _listStateFlow.emit(Resource.Loading())
                useCaseSearchNews(searchQuery, pageNum).onEach { result ->
                    _listStateFlow.emit(
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { _sListArticles.addAll(it) }
                                Resource.Success(_sListArticles.toList())
                            }
                            else -> result
                        }
                    )
                }.launchIn(viewModelScope)
            }
        }

    }

    fun closeSearch() {
        _sListArticles.clear()
        viewModelScope.launch {
            _listStateFlow.emit(Resource.Success(_listArticles.toList()))
        }
    }

}