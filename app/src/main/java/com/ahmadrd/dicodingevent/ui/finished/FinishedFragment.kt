package com.ahmadrd.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ahmadrd.dicodingevent.R
import com.ahmadrd.dicodingevent.databinding.FragmentFinishedBinding
import com.ahmadrd.dicodingevent.ui.adapter.ListEventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private lateinit var adapter: ListEventAdapter
    private val finishedViewModel: FinishedViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearchBar()
        refreshFinishedEvents()
    }

    private fun setupRecyclerView() {
        adapter = ListEventAdapter()
        binding.rvFinished.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            this.adapter = this@FinishedFragment.adapter
        }
    }

    private fun observeViewModel() {
        finishedViewModel.listEvent.observe(viewLifecycleOwner) { eventList ->
            adapter.submitList(eventList)
        }

        finishedViewModel.listEventSearch.observe(viewLifecycleOwner){ eventListSearch ->
            adapter.submitList(eventListSearch)
            if (eventListSearch.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_data_finished, binding.searchBar.text.toString()), Toast.LENGTH_SHORT).show()
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        finishedViewModel.error.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                Toast.makeText(requireContext(), R.string.failed, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    finishedViewModel.getEventSearch(searchBar.text.toString())
                    searchView.hide()
                    false
                }
        }
    }

    private fun refreshFinishedEvents() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            finishedViewModel.getEvent()
            binding.searchBar.setText("")
            binding.swipeRefreshLayout.isRefreshing = false
        }
        finishedViewModel.restoreLastState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}