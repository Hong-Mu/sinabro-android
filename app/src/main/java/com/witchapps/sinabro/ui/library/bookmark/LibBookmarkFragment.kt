package com.witchapps.sinabro.ui.library.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.databinding.FragmentLibBookmarkBinding
import com.witchapps.sinabro.ui.library.LibraryFragmentDirections
import com.witchapps.sinabro.ui.util.FirebaseUtil
import com.witchapps.sinabro.ui.util.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LibBookmarkFragment : Fragment() {

    private var _binding: FragmentLibBookmarkBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var adapter: BookmarkAdapter
    private var bookMarkListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        val user = Firebase.auth.currentUser
        if(user != null) {
            load(user)
        }
    }

    private fun load(user: FirebaseUser) {
        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)
            .collection(FirebaseUtil.COLLECTION_BOOKMARK)

        bookMarkListener = ref.addSnapshotListener { value, error ->
            if(error != null) return@addSnapshotListener

            if(value != null && !value.isEmpty) {
                val temp = value.map {
                    it.toObject(Bookmark::class.java).apply {
                        itemId = it.id
                    }
                }
                adapter.originalList = temp
            }
         }
    }

    private fun initRecyclerView(){
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { item, position ->
            val action = LibraryFragmentDirections.actionLibraryFragmentToDetailFragment(null, (item as Bookmark).itemId.toString())
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}