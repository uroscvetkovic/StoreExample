package com.example.storeexample.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storeexample.R
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.databinding.ItemProductBinding
import com.example.storeexample.util.ImageLoader

class ProductAdapter(
    private val onProductClick: (productId: Int) -> Unit,
    private val onFavoriteClick: ((product: Product) -> Unit)? = null
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    private var favoriteIds: Set<Int> = emptySet()

    fun setFavorites(ids: Set<Int>) {
        val old = favoriteIds
        favoriteIds = ids
        for (i in 0 until currentList.size) {
            val id = getItem(i).id
            if ((id in old) != (id in ids)) notifyItemChanged(i)
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewTitle.text = product.title
            binding.textViewCategory.text = product.category
            binding.textViewPrice.text = "$" + "%.2f".format(product.price)
            binding.textViewRating.text = "★ %.1f".format(product.rating)
            ImageLoader.load(product.thumbnail, binding.imageViewThumbnail)

            val isFav = product.id in favoriteIds
            binding.buttonFavorite.setImageResource(
                if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.buttonFavorite.setOnClickListener { onFavoriteClick?.invoke(product) }

            binding.root.setOnClickListener { onProductClick(product.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }
}
