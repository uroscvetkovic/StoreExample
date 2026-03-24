package com.example.storeexample.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storeexample.R
import com.example.storeexample.data.remote.model.Product
import com.bumptech.glide.Glide
import com.example.storeexample.databinding.ItemProductBinding

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
            if ((id in old) != (id in ids)) notifyItemChanged(i, PAYLOAD_FAVORITE)
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewTitle.text = product.title
            binding.textViewCategory.text = product.category
            binding.textViewPrice.text = "$" + "%.2f".format(product.price)
            binding.textViewRating.text = "★ %.1f".format(product.rating)
            Glide.with(binding.imageViewThumbnail)
                .load(product.thumbnail)
                .into(binding.imageViewThumbnail)
            binding.root.setOnClickListener { onProductClick(product.id) }
            binding.buttonFavorite.setOnClickListener { onFavoriteClick?.invoke(product) }
            updateFavoriteIcon(product.id in favoriteIds)
        }

        fun updateFavoriteIcon(isFav: Boolean) {
            binding.buttonFavorite.setImageResource(
                if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_FAVORITE) {
            holder.updateFavoriteIcon(getItem(position).id in favoriteIds)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }

    companion object {
        private const val PAYLOAD_FAVORITE = "favorite"
    }
}
