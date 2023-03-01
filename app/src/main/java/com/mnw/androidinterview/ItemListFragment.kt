package com.mnw.androidinterview

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mnw.androidinterview.app.RecyclerViewAdapter
import com.mnw.androidinterview.databinding.FragmentItemListBinding
import com.mnw.androidinterview.databinding.ItemListContentBinding
import com.mnw.androidinterview.net.BooksApi
import com.mnw.androidinterview.net.EndpointClient
import com.mnw.androidinterview.placeholder.PlaceholderContent
import kotlinx.coroutines.launch

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment() {

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat = ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
        if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
            Toast.makeText(
                v.context,
                "Undo (Ctrl + Z) shortcut triggered",
                Toast.LENGTH_LONG
            ).show()
            true
        } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
            Toast.makeText(
                v.context,
                "Find (Ctrl + F) shortcut triggered",
                Toast.LENGTH_LONG
            ).show()
            true
        }
        false
    }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        setupRecyclerView(recyclerView, itemDetailFragmentContainer)

        getBooksList()
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        itemDetailFragmentContainer: View?
    ) {

        recyclerView.adapter = RecyclerViewAdapter(
            PlaceholderContent.ITEMS, itemDetailFragmentContainer
        )
    }

    fun getBooksList() {
        var retrofit = EndpointClient.getInstance()
        var apiInterface = retrofit.create(BooksApi::class.java)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                try {
                    val response = apiInterface.searchBooks("mongo")
                    if (response.isSuccessful) {

                        PlaceholderContent.clear()
                        response.body()?.bookList?.forEach { book ->
                            PlaceholderContent.addItem(PlaceholderContent.PlaceholderItem(book.thumbnail.substring(0,4), book.title))
                        }

                        (binding.itemList.adapter as RecyclerViewAdapter).notifyDataSetChanged()

                    } else {
                        Toast.makeText(
                            context,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (Ex:Exception){
                    Log.e("ASD Error", Ex.localizedMessage)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}