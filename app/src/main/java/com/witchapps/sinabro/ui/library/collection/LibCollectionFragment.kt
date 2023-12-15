package com.witchapps.sinabro.ui.library.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.witchapps.sinabro.R
import com.witchapps.sinabro.databinding.FragmentLibBookmarkBinding
import com.witchapps.sinabro.databinding.FragmentLibCollectionBinding

class LibCollectionFragment : Fragment() {

    private var _binding: FragmentLibCollectionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}