package com.ahmadrd.dicodingevent.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrd.dicodingevent.data.remote.response.Event
import com.ahmadrd.dicodingevent.databinding.ItemRowFavBinding
import com.ahmadrd.dicodingevent.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class ListFavoriteAdapter: ListAdapter<Event, ListFavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ItemRowFavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.tvTitle.text = item.name
            Log.d("ListFavoriteAdapter", "Judul: ${item.name}")
            binding.tvDesc.text = item.summary
            Log.d("ListFavoriteAdapter", "Deskripsi-Summary: ${item.summary}")
            Log.d("ListFavoriteAdapter", "Deskripsi-Desc: ${item.description}")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}