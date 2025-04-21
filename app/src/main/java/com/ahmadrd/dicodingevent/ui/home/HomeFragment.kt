package com.ahmadrd.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadrd.dicodingevent.R
import com.ahmadrd.dicodingevent.data.remote.response.ListEventsItem
import com.ahmadrd.dicodingevent.databinding.FragmentHomeBinding
import com.ahmadrd.dicodingevent.ui.adapter.ListEventAdapter
import com.ahmadrd.dicodingevent.ui.adapter.ListEventAdapter.Companion.VIEW_TYPE_HORIZONTAL
import com.ahmadrd.dicodingevent.ui.adapter.ListEventAdapter.Companion.VIEW_TYPE_VERTICAL

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.listEventUpcoming.observe(viewLifecycleOwner) { listEvent ->
            if (listEvent != null) {
                setListUpcomingEvents(listEvent)
            }
            binding.tvHomeNoData.visibility = if (listEvent.isEmpty()) View.VISIBLE else View.GONE
        }
        viewModel.listEventFinished.observe(viewLifecycleOwner) { listEvent ->
            if (listEvent != null) {
                setListFinishedEvents(listEvent)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun setListUpcomingEvents(listEvent: List<ListEventsItem>) {
        val listEventWithViewType = listEvent.map { it.copy(viewType = VIEW_TYPE_HORIZONTAL) }
        val listEventAdapter = ListEventAdapter()
        listEventAdapter.submitList(listEventWithViewType)
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listEventAdapter
        }
    }

    private fun setListFinishedEvents(listEvent: List<ListEventsItem>) {
        val listEventWithViewType = listEvent.map { it.copy(viewType = VIEW_TYPE_VERTICAL) }
        val listEventAdapter = ListEventAdapter()
        listEventAdapter.submitList(listEventWithViewType)
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listEventAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}