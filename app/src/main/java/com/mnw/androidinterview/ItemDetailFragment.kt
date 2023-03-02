package com.mnw.androidinterview

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mnw.androidinterview.databinding.FragmentItemDetailBinding
import com.mnw.androidinterview.model.Book
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var itemTitleTextView: TextView
    private lateinit var itemAuthorTextView: TextView
    private lateinit var itemPublisherTextView: TextView
    private lateinit var itemRatingTextView: TextView
    private lateinit var itemYearTextView: TextView
    private lateinit var itemDescriptionTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            viewModel.setItemId(dragData.toString())

        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val id = it.getString(ARG_ITEM_ID)
                if (!id.isNullOrEmpty()) {
                    viewModel.setItemId(id)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemTitleTextView = binding.itemTitle!!
        itemAuthorTextView = binding.itemAuthors!!
        itemPublisherTextView = binding.itemPublisher!!
        itemRatingTextView = binding.itemRating!!
        itemYearTextView = binding.itemYear!!
        itemDescriptionTextView = binding.itemDescription!!

        binding.saveButton!!.setOnClickListener {
            viewModel.save()
        }
        binding.unsaveButton!!.setOnClickListener {
            viewModel.unsave()
        }

        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.item.observe(viewLifecycleOwner) {
            updateContent(it)
            updateButtons(it, viewModel.savedList.value ?: emptyList())
        }

        viewModel.savedList.observe(viewLifecycleOwner) {
            val book = viewModel.item.value
            updateButtons(book, it)
        }
    }

    private fun updateButtons(currentBook: Book?, it: List<Book>) {
        if (currentBook != null && it.contains(currentBook)) {
            binding.saveButton?.visibility = View.GONE
            binding.unsaveButton?.visibility = View.VISIBLE
        } else {
            binding.saveButton?.visibility = View.VISIBLE
            binding.unsaveButton?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun updateContent(book: Book?) {

        toolbarLayout?.title = book?.title

        // Show the placeholder content as text in a TextView.
        book?.let {
            itemTitleTextView.text = it.title

            itemAuthorTextView.text = it.authors ?: "n/a"
            itemPublisherTextView.text = it.publisher ?: "n/a"
            itemRatingTextView.text = it.rating ?: "n/a"
            itemYearTextView.text = it.year?.toString() ?: "n/a"
            itemDescriptionTextView.text = it.description ?: "n/a"

            binding.imageDetails?.let { imageView ->
                Glide
                    .with(requireContext())
                    .load(it.thumbnail)
                    .centerCrop()
                    .into(imageView)

            }
        }
    }


    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}