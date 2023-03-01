package com.mnw.androidinterview.app

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mnw.androidinterview.ItemDetailFragment
import com.mnw.androidinterview.R
import com.mnw.androidinterview.databinding.ItemListContentBinding
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.placeholder.PlaceholderContent


class RecyclerViewAdapter (
    private val itemDetailFragmentContainer: View?
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var values: MutableList<Book> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.title
        holder.thumbnail.setImageBitmap(item.thumbnail)

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

    override fun getItemCount() = values.size

    fun setItems(it: List<Book>) {
        values = ArrayList(it)
        notifyDataSetChanged()

    }

    inner class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.textTitle
        val thumbnail: ImageView = binding.imageThumbnail
    }

}