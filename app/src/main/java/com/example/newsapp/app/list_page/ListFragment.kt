package com.example.newsapp.app.list_page

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lloydsassigment.presentation.product_list.adapters.ArticleAdapter
import com.example.newsapp.R
import com.example.newsapp.app.MainActivity
import com.example.newsapp.app.MainViewModel
import com.example.newsapp.databinding.FragmentListBinding
import com.example.newsapp.domain.models.Article
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.hide
import com.example.newsapp.utils.log
import com.example.newsapp.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var activity: MainActivity

    private lateinit var articleAdapter: ArticleAdapter

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = requireActivity() as MainActivity

        initialiseRecycleView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.isSearchEnabled = true
                viewModel.performSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String) = false

        })

        binding.searchView.setOnCloseListener {
            viewModel.isSearchEnabled = false
            viewModel.closeSearch()
            false
        }


        lifecycleScope.launchWhenStarted {
            viewModel.listStateFlow.collectLatest { result ->
                handleResult(result)
            }
            viewModel.sListStateFlow.collectLatest { result ->
                handleResult(result)
            }
        }

    }

    private fun handleResult(result: Resource<List<Article>>) {
        when (result) {
            is Resource.Error -> {
                Toast.makeText(
                    requireContext(),
                    result.message,
                    Toast.LENGTH_LONG
                ).show()

                activity.binding.progressBar.hide()
            }

            is Resource.Loading -> {
                activity.binding.progressBar.show()
            }

            is Resource.Success -> {
                val listOfProducts = result.data
                listOfProducts?.let { list ->
                    articleAdapter.updateArticleList(list)
                }
                activity.binding.progressBar.hide()
            }
        }
    }


    private fun isLastVisible(rv: RecyclerView): Boolean {
        val layoutManager = rv.layoutManager as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems: Int = rv.adapter?.itemCount ?: 0
        return pos >= numItems - 1
    }

    private fun initialiseRecycleView() {
        articleAdapter = ArticleAdapter()
        binding.rvArticles.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.rvArticles.setOnScrollChangeListener { _, _, _, _, _ ->
                if (viewModel.isSearchEnabled) {
                    if (isLastVisible(binding.rvArticles)) viewModel.nextSearchPage()
                } else {
                    if (isLastVisible(binding.rvArticles)) viewModel.nextPage()
                }
            }
        }

        articleAdapter.setItemClickListener { article ->
            val articleUrl = article.url
            log("Clicked: $articleUrl")

            val action = ListFragmentDirections
                .actionListFragmentToWebViewFragment(url = articleUrl)

            findNavController().navigate(action)
        }

    }

}