package com.witchapps.sinabro.ui.library.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.databinding.ItemBookBinding
import com.witchapps.sinabro.ui.home.HomeAdapter
import com.witchapps.sinabro.ui.util.OnItemClickListener
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BookmarkAdapter @Inject constructor(

) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    var originalList = listOf<Bookmark>()
        set(value) {
            field = value
            list = value
        }

    var list: List<Bookmark> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clearFilter() {
        list = originalList
    }

    fun sortByTitle() {
        list = originalList.sortedBy { it.title }
    }

    inner class ViewHolder(private val binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            list[position].let {  item ->
                binding.textTitle.text = item.title
                binding.textAuthor.text = item.author
                Glide.with(binding.root)
                    .load(item.cover)
                    .into(binding.imageCover)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(list[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}