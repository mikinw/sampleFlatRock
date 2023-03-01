package com.mnw.androidinterview.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mnw.androidinterview.ItemDetailFragment
import com.mnw.androidinterview.R
import com.mnw.androidinterview.databinding.ItemListContentBinding
import com.mnw.androidinterview.model.Book


private class DiffCallback : DiffUtil.ItemCallback<Book>() {

    override fun areItemsTheSame(oldItem: Book, newItem: Book) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Book, newItem: Book) =
        oldItem == newItem
}



class RecyclerViewAdapter (
    private val itemDetailFragmentContainer: View?
) : ListAdapter<Book, RecyclerViewAdapter.ViewHolder>(DiffCallback()) {


    private var values: MutableList<Book> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.title.text = item.title

        if (!item.thumbnail.isNullOrBlank()) {
            Glide
                .with(holder.itemView)
                .load(item.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(holder.thumbnail)

        }

        with(holder.itemView) {
            tag = item
            setOnClickListener { itemView ->
                val item = itemView.tag as Book
                val bundle = Bundle()
                bundle.putString(
                    ItemDetailFragment.ARG_ITEM_ID,
                    item.id
                )
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }

        }
    }

    inner class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.textTitle
        val thumbnail: ImageView = binding.imageThumbnail
    }

}