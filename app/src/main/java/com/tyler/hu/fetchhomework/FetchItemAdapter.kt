package com.tyler.hu.fetchhomework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FetchItemAdapter : ListAdapter<OnlineDataRepository.FetchItem, FetchItemAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val listIdTextView: TextView = itemView.findViewById(R.id.listIdTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fetch, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.idTextView.text = currentItem.id.toString()
        holder.listIdTextView.text = currentItem.listId.toString()
        holder.nameTextView.text = currentItem.name
    }

    class DiffCallback : DiffUtil.ItemCallback<OnlineDataRepository.FetchItem>() {
        override fun areItemsTheSame(oldItem: OnlineDataRepository.FetchItem, newItem: OnlineDataRepository.FetchItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OnlineDataRepository.FetchItem, newItem: OnlineDataRepository.FetchItem): Boolean {
            return oldItem == newItem
        }
    }
}