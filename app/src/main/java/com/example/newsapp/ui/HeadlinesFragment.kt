package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.api.Article
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.ui.newsdetail.NewsDetailActivity
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlinesFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()

    // View Binding object
    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView using View Binding
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe articles LiveData from ViewModel
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            binding.recyclerView.adapter = NewsAdapter(articles) { article ->
                openArticleDetail(article)
            }
        }

        // Fetch headlines
        viewModel.getHeadlines("us", "55f78cc110354e2b8bd55965962c6eee")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clean up the binding to avoid memory leaks
    }
}
