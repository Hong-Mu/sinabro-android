package com.witchapps.sinabro.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.databinding.FragmentExploreBinding
import com.witchapps.sinabro.ui.home.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExploreViewModel by viewModels()
    @Inject lateinit var adapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRV()

        binding.btnSearch.setOnClickListener {
            viewModel.searchBook(binding.inputKeyword.text.toString())
        }

        viewModel.bookList.observe(viewLifecycleOwner) {
            adapter.originalList = it
        }
    }

    private fun initRV() {
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, poisition ->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailFragment(item as Book, null)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}