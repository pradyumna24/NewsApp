package com.example.newsapp.data.repository

import com.example.newsapp.data.db.Bookmark
import com.example.newsapp.data.api.NewsApiService
import com.example.newsapp.data.db.BookmarkDao
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val bookmarkDao: BookmarkDao
) {
     suspend fun getTopHeadlines(country: String, apiKey: String) =
        newsApiService.getTopHeadlines(country, apiKey)

     suspend fun searchNews(query: String, apiKey: String) =
        newsApiService.searchNews(query, apiKey)

    suspend fun insertBookmark(bookmark: Bookmark) = bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteBookmark(bookmark: Bookmark) = bookmarkDao.deleteBookmark(bookmark)

    suspend fun getBookmarkByUrl(url: String) = bookmarkDao.getBookmarkByUrl(url)

    fun getAllBookmarks() = bookmarkDao.getAllBookmarks()
}