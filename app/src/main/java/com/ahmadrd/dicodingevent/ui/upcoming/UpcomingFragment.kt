package com.ahmadrd.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadrd.dicodingevent.R
import com.ahmadrd.dicodingevent.databinding.FragmentUpcomingBinding
import com.ahmadrd.dicodingevent.ui.ListEventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private lateinit var adapter: ListEventAdapter
    private val upcomingViewModel: UpcomingViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ListEventAdapter()
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@UpcomingFragment.adapter
        }
    }

    private fun observeViewModel() {
        upcomingViewModel.listEvent.observe(viewLifecycleOwner) { eventList ->
            adapter.submitList(eventList)
            binding.tvNoData.visibility = if (eventList.isEmpty()) View.VISIBLE else View.GONE
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        upcomingViewModel.error.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                Toast.makeText(requireContext(), R.string.failed, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}