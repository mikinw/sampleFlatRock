package com.mnw.androidinterview



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mnw.androidinterview.app.RecyclerViewAdapter
import com.mnw.androidinterview.databinding.FragmentItemListBinding
import com.mnw.androidinterview.model.NetworkState
import com.mnw.androidinterview.model.NetworkStateModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SavedListFragment : Fragment() {

    private val viewModel: SavedListViewModel by viewModels()

    private var _binding: FragmentItemListBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var networkStateModel: NetworkStateModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.visibility = View.GONE

        val recyclerView: RecyclerView = binding.itemList

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        setupRecyclerView(recyclerView, itemDetailFragmentContainer)

        viewModel.bookList.observe(viewLifecycleOwner) {
            (binding.itemList.adapter as RecyclerViewAdapter).submitList(it)
        }

        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
        }



    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        itemDetailFragmentContainer: View?
    ) {

        recyclerView.adapter = RecyclerViewAdapter(
            itemDetailFragmentContainer
        )
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}