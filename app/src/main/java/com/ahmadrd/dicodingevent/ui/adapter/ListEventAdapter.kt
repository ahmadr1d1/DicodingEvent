package com.ahmadrd.dicodingevent.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrd.dicodingevent.data.remote.response.ListEventsItem
import com.ahmadrd.dicodingevent.databinding.ItemRowEventBinding
import com.ahmadrd.dicodingevent.databinding.ItemRowEventHorizontalBinding
import com.ahmadrd.dicodingevent.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class ListEventAdapter : ListAdapter<ListEventsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val VIEW_TYPE_HORIZONTAL = 1
        const val VIEW_TYPE_VERTICAL = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).viewType == VIEW_TYPE_HORIZONTAL) {
            VIEW_TYPE_HORIZONTAL
        } else {
            VIEW_TYPE_VERTICAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HORIZONTAL -> {
                val binding = ItemRowEventHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HorizontalViewHolder(binding)
            }
            VIEW_TYPE_VERTICAL -> {
                val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HorizontalViewHolder -> holder.bind(item)
            is VerticalViewHolder -> holder.bind(item)
        }
    }

    inner class HorizontalViewHolder(private val binding: ItemRowEventHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEventsItem) {
            binding.tvTitle.text = item.name
            binding.tvDesc.text = item.summary
            Log.d("ListEventAdapter", "Deskripsi: ${item.summary}")
            Glide.with(binding.root.context)
                .load(item.mediaCover)
                .into(binding.eventImage)

            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_EVENT_ID, item.id)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    inner class VerticalViewHolder(private val binding: ItemRowEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEventsItem) {
            binding.tvEvent.text = item.name
            Glide.with(binding.root.context)
                .load(item.mediaCover)
                .into(binding.eventPhoto)

            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_EVENT_ID, item.id)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }
}