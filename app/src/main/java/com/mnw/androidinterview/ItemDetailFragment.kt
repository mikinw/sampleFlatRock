package com.mnw.androidinterview

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mnw.androidinterview.databinding.FragmentItemDetailBinding
import com.mnw.androidinterview.model.Book
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    lateinit var itemTitleTextView: TextView
    lateinit var itemAuthorTextView: TextView
    lateinit var itemPublisherTextView: TextView
    lateinit var itemRatingTextView: TextView
    lateinit var itemYearTextView: TextView
    lateinit var itemDesctiptionTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        itemDesctiptionTextView = binding.itemDescription!!

        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.item.observe(viewLifecycleOwner) {
            updateContent(it)
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
            itemTitleTextView.text = it.description

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