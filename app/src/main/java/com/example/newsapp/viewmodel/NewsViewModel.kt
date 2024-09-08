package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.db.Bookmark
import com.example.newsapp.data.api.Article
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    fun getHeadlines(country: String, apiKey: String) {
        viewModelScope.launch {
            val response = repository.getTopHeadlines(country, apiKey)
            _articles.postValue(response.articles)
        }
    }

    fun searchNews(query: String, apiKey: String) {
        viewModelScope.launch {
            val response = repository.searchNews(query, apiKey)
            _articles.postValue(response.articles)
        }
    }

    fun addBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            repository.insertBookmark(bookmark)
        }
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            repository.deleteBookmark(bookmark)
        }
    }

    fun getAllBookmarks() = repository.getAllBookmarks()
}