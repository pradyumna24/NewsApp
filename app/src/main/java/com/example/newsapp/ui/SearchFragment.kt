package com.example.newsapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.api.Article
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.newsdetail.NewsDetailActivity
import com.example.newsapp.ui.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()

    // View Binding object
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the articles LiveData from ViewModel
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            binding.recyclerView.adapter = NewsAdapter(articles) { article ->
                openArticleDetail(article)
            }
        }

        // Trigger the search when needed (for example, on button click)
        binding.searchButton.setOnClickListener {
            val query = binding.searchInput.text.toString()
            if (query.isNotEmpty()) {
                searchNews(query)
            }
        }
    }

    private fun openArticleDetail(article: Article) {
        val intent = Intent(context, NewsDetailActivity::class.java).apply {
            putExtra("title", article.title)
            putExtra("description", article.description)
            putExtra("imageUrl", article.urlToImage)
            putExtra("articleUrl", article.url)
        }
        startActivity(intent)
    }


    // Trigger the search and let ViewModel handle the result
    private fun searchNews(query: String) {
        viewModel.searchNews(query, "55f78cc110354e2b8bd55965962c6eee")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
