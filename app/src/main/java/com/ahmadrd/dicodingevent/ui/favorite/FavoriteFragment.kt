package com.ahmadrd.dicodingevent.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadrd.dicodingevent.data.remote.response.ListEventsItem
import com.ahmadrd.dicodingevent.databinding.FragmentFavoriteBinding
import com.ahmadrd.dicodingevent.ui.adapter.ListFavoriteAdapter
import com.ahmadrd.dicodingevent.ui.utils.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListFavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        favoriteViewModel = viewModels<FavoriteViewModel> { factory }.value
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ListFavoriteAdapter()
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeViewModel() {
        favoriteViewModel.getFavEvents().observe(viewLifecycleOwner) { events ->
            binding.tvFavNoData.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            binding.tvFavorite.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
            val items = arrayListOf<ListEventsItem>()
            events.map {
                val item = ListEventsItem(
                    id = it.eventId,
                    mediaCover = it.mediaCover,
                    name = it.title,
                    summary = it.description,
                )
                items.add(item)
            }
            adapter.submitList(items)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }

}