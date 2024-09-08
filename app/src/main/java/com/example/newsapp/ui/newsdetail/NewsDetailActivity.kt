package com.example.newsapp.ui.newsdetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.data.db.Bookmark
import com.example.newsapp.data.db.AppDatabase
import com.example.newsapp.databinding.ActivityNewsDetailBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding
    private var isBookmarked = false
    private lateinit var articleUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get article details from intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")
        articleUrl = intent.getStringExtra("articleUrl") ?: ""

        // Set the article details to views
        binding.newsTitle.text = title
        binding.newsDescription.text = description
        Picasso.get().load(imageUrl).into(binding.newsImage)

        // Check if the article is already bookmarked
        checkIfBookmarked()

        // Set up the bookmark button
        binding.bookmarkButton.setOnClickListener {
            if (isBookmarked) {
                removeBookmark(title, description, imageUrl, articleUrl)
            } else {
                addBookmark(title, description, imageUrl, articleUrl)
            }
        }
    }

    private fun checkIfBookmarked() {
        val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookmarkDao()
        lifecycleScope.launch {
            val bookmark = bookmarkDao.getBookmarkByUrl(articleUrl)
            isBookmarked = bookmark != null
            updateBookmarkButton()
        }
    }

    private fun addBookmark(title: String?, description: String?, imageUrl: String?, url: String?) {
        val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookmarkDao()

        lifecycleScope.launch {
            val bookmark = Bookmark(url!!, title!!, description, imageUrl)
            bookmarkDao.insertBookmark(bookmark)
            isBookmarked = true
            updateBookmarkButton()
            Toast.makeText(this@NewsDetailActivity, "Article bookmarked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeBookmark(title: String?, description: String?, imageUrl: String?, url: String?) {
        val bookmarkDao = AppDatabase.getDatabase(applicationContext).bookmarkDao()

        lifecycleScope.launch {
            val bookmark = Bookmark(url!!, title!!, description, imageUrl)
            bookmarkDao.deleteBookmark(bookmark)
            isBookmarked = false
            updateBookmarkButton()
            Toast.makeText(this@NewsDetailActivity, "Bookmark removed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBookmarkButton() {
        if (isBookmarked) {
            binding.bookmarkButton.text = "Remove Bookmark"
        } else {
            binding.bookmarkButton.text = "Bookmark"
        }
    }
}