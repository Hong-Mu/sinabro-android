package com.witchapps.sinabro.ui.library.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.witchapps.sinabro.databinding.ItemCollectionBinding

class CollectionAdapter: RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {
    var list: List<Collection> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemCollectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            list[position].apply {
                binding.textTitle.text = title
                binding.textUserName.text = userName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}