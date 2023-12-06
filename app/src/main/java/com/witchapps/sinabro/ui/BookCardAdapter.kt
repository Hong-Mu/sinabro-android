package com.witchapps.sinabro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.api.response.SimpleBook
import com.witchapps.sinabro.databinding.ItemBookBinding
import com.witchapps.sinabro.ui.util.OnItemClickListener
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import kotlin.math.min

@FragmentScoped
class BookCardAdapter @Inject constructor() :RecyclerView.Adapter<BookCardAdapter.ViewHolder>()
    {
        var list: List<SimpleBook> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

        inner class ViewHolder(private val binding: ItemBookBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(position: Int) {
                list[position].let { item ->
                    binding.textTitle.text = item.title
                    binding.textAuthor.text = ""
                    binding.textPublisher.text = ""
                    Glide.with(binding.root)
                        .load(item.cover)
                        .into(binding.imageCover)
                }
            }
        }

        override fun getItemCount() = min(list.size, 3)

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(list[position], position)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        private var onItemClickListener: OnItemClickListener? = null

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.onItemClickListener = onItemClickListener
        }
    }