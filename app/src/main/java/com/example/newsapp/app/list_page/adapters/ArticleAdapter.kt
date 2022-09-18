package com.example.lloydsassigment.presentation.product_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.domain.models.Article

class ArticleAdapter :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var articles: List<Article> = emptyList()

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        val binding = holder.binding
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(binding.imageView)
            binding.titleTv.text = article.title
            binding.descriptionTv.text = article.description

            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount() = articles.size

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun updateArticleList(newList: List<Article>) {

        val diffCallback = ArticleListDiff(this.articles, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.articles = newList

    }
}