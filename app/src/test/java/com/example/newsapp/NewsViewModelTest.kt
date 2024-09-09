package com.example.newsapp

import com.example.newsapp.data.api.NewsResponse
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.cancel
import org.junit.Assert.assertEquals


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.newsapp.data.api.Article
import com.example.newsapp.data.db.Bookmark
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NewsViewModel

    @Mock
    private lateinit var repository: NewsRepository

    @Mock
    private lateinit var observer: Observer<List<Article>>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = NewsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun testGetHeadlines() = runTest {
        val mockArticles = listOf(
            Article("Title 1", "description1", "newsurl1", "url1"),
            Article("Title 2", "description2", "newsurl2", "url2")
        )
        val mockResponse = mock(NewsResponse::class.java)
        `when`(mockResponse.articles).thenReturn(mockArticles)
        `when`(repository.getTopHeadlines("us", "testApiKey")).thenReturn(mockResponse)

        viewModel.articles.observeForever(observer)

        viewModel.getHeadlines("us", "testApiKey")
        advanceUntilIdle()

        verify(observer).onChanged(mockArticles)
        assertEquals(mockArticles, viewModel.articles.value)
    }

    @Test
    fun testSearchNews() = runTest {
        val mockArticles = listOf(
            Article("Search Title 1", "description1", "newsurl1", "url1"),
            Article("Search Title 2", "description2", "newsurl2", "url2")
        )
        val mockResponse = mock(NewsResponse::class.java)
        `when`(mockResponse.articles).thenReturn(mockArticles)
        `when`(repository.searchNews("query", "testApiKey")).thenReturn(mockResponse)

        viewModel.articles.observeForever(observer)

        viewModel.searchNews("query", "testApiKey")
        advanceUntilIdle()

        verify(observer).onChanged(mockArticles)
        assertEquals(mockArticles, viewModel.articles.value)
    }

    @Test
    fun testaddBookmark() = runTest {
        val bookmark = Bookmark("url", "Title", "description", "image_url")

        viewModel.addBookmark(bookmark)
        advanceUntilIdle()

        verify(repository).insertBookmark(bookmark)
    }

    @Test
    fun testremoveBookmark() = runTest {
        val bookmark = Bookmark("url", "Title", "description", "image_url")

        viewModel.removeBookmark(bookmark)
        advanceUntilIdle()

        verify(repository).deleteBookmark(bookmark)
    }

    @Test
    fun testgetAllBookmarks() {
        viewModel.getAllBookmarks()

        verify(repository).getAllBookmarks()
    }
}
