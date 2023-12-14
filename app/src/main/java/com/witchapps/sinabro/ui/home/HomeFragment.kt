package com.witchapps.sinabro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.witchapps.sinabro.R
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var adapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        initRecyclerView()
    }

    private fun initSpinner() {
        binding.spinnerSort.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            viewModel.selectedSortIndex = newIndex
            when(newIndex) {
                0 -> adapter.clearFilter()
                1 -> adapter.sortByTitle()
                2 -> adapter.sortByPubDate()
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { item, position ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item as Book, null)
            findNavController().navigate(action)
        }

        viewModel.bookList.observe(viewLifecycleOwner) {
            adapter.originalList = it
            binding.spinnerSort.selectItemByIndex(viewModel.selectedSortIndex)
        }
    }

    override fun onStop() {
        super.onStop()
        binding.spinnerSort.dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}