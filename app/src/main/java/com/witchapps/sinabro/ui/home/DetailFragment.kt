package com.witchapps.sinabro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.api.response.SimpleBook
import com.witchapps.sinabro.databinding.FragmentDetailBinding
import com.witchapps.sinabro.ui.BookCardAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    @Inject lateinit var seriesAdapter: BookCardAdapter

    private var book: Book? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSeriesRecyclerView()

        book = args.book
        if(book == null) {
            viewModel.getBook(args.itemId!!, type = "ItemId")
        } else {
            updateUI()
        }

        viewModel.getSimpleBookResult.observe(viewLifecycleOwner) { result ->
            if(result.isSuccessful) {
                seriesAdapter.list = result.data!!
                binding.textSeries.visibility = View.VISIBLE
            }
            binding.seriesProgressBar.visibility = View.GONE

        }

        viewModel.getBookResult.observe(viewLifecycleOwner) { result ->
            if(result.isSuccessful) {
                book = result.data?.get(0)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        book?.let { item ->
            binding.textTitle.text = item.title
            binding.textAuthor.text = item.author
            binding.textPublisher.text = item.publisher
            Glide.with(binding.root)
                .load(item.cover)
                .into(binding.imageCover)

            // item.seriesInfo?.let { series -> binding.textSeriesTitle.text = series.seriesName }

            viewModel.getSimpleBook(item.itemId.toString())
        }
    }

    private fun initSeriesRecyclerView() {
        binding.seriesRecyclerView.adapter = seriesAdapter
        seriesAdapter.setOnItemClickListener { item, position ->
            val itemId = (item as SimpleBook).itemId
            val action = DetailFragmentDirections.actionDetailFragmentSelf(null, itemId)
            findNavController().navigate(action)
        }
        binding.seriesRecyclerView.layoutManager = object: GridLayoutManager(context, 3) {
            override fun canScrollVertically() = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}