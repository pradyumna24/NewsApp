package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.db.Bookmark
import com.example.newsapp.data.api.Article
import com.example.newsapp.data.db.AppDatabase
import com.example.newsapp.databinding.FragmentBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    // View binding object
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView using View Binding
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Load bookmarks
        loadBookmarks()
    }

    private fun loadBookmarks() {
        val bookmarkDao = AppDatabase.getDatabase(requireContext()).bookmarkDao()

        lifecycleScope.launch {
            bookmarkDao.getAllBookmarks().observe(viewLifecycleOwner) { bookmarks ->
                if (bookmarks != null && bookmarks.isNotEmpty()) {
                    binding.recyclerView.adapter =
                        NewsAdapter(bookmarks.map { bookmarkToArticle(it) }) {}
                }
            }
        }
    }

    private fun bookmarkToArticle(bookmark: Bookmark): Article {
        return Article(
            title = bookmark.title,
            description = bookmark.description,
            urlToImage = bookmark.urlToImage,
            url = bookmark.url
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks
    }
}
