package com.witchapps.sinabro.ui.home

import android.animation.Animator
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.witchapps.sinabro.R
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.api.response.SimpleBook
import com.witchapps.sinabro.databinding.FragmentDetailBinding
import com.witchapps.sinabro.databinding.LayoutAddCollectionBinding
import com.witchapps.sinabro.ui.BookCardAdapter
import com.witchapps.sinabro.ui.util.FirebaseUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val contentBinding by lazy {LayoutAddCollectionBinding.inflate(layoutInflater)}

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var seriesAdapter: BookCardAdapter
    private val collectionBottomSheet by lazy { BottomSheetDialog(requireContext()) }


    private var book: Book? = null
    private var bookmarkChecked = false
    private var bookMarkListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        book = args.book
        if (book == null) {
            viewModel.getBook(args.itemId!!, type = "ItemId")
        } else {
            val user = Firebase.auth.currentUser
            if (user != null) {
                initBookmark(user)
                initCollectionBottomSheet(user)
            }
            updateUI()
        }

        initLottie()
        initSeriesRecyclerView()

        binding.btnAladin.setOnClickListener {
            CustomTabsIntent.Builder().build()
                .launchUrl(requireContext(), Uri.parse("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=${book!!.itemId}"))
        }

        binding.fabAdd.setOnClickListener {
            collectionBottomSheet.show()
        }

        viewModel.getSimpleBookResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccessful) {
                seriesAdapter.list = result.data!!
                binding.textSeries.visibility = View.VISIBLE
            }
            binding.seriesProgressBar.visibility = View.GONE

        }

        viewModel.getBookResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccessful) {
                book = result.data?.get(0)

                val user = Firebase.auth.currentUser
                if (user != null) {
                    initBookmark(user)
                    initCollectionBottomSheet(user)
                }

                updateUI()
            }
        }
    }

    private fun initBookmark(user: FirebaseUser) {
        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)
            .collection(FirebaseUtil.COLLECTION_BOOKMARK)
            .document(book!!.itemId.toString())

        bookMarkListener = ref.addSnapshotListener { value, error ->
            if(error != null) return@addSnapshotListener

            bookmarkChecked = value != null && value.exists()
            updateBookmark(bookmarkChecked)
        }

        binding.btnBookmarks.setOnClickListener {
            if(!bookmarkChecked) {
                ref.set(mapOf(
                    FirebaseUtil.FIELD_TITLE to book!!.title,
                    FirebaseUtil.FIELD_AUTHOR to book!!.author,
                    FirebaseUtil.FIELD_COVER to book!!.cover,
                )).addOnSuccessListener {
                    playLottie()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                ref.delete().addOnSuccessListener {
                    Snackbar.make(binding.root, "북마크가 제거되었습니다.", Snackbar.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initLottie() {
        binding.lottie.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                binding.lottie.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationRepeat(p0: Animator) {}

        })
    }

    private fun playLottie(){
        binding.lottie.visibility = View.VISIBLE
        binding.lottie.playAnimation()
    }

    private fun updateBookmark(checked: Boolean) {
        val baseColor = ColorStateList.valueOf(resources.getColor(R.color.md_theme_light_primaryContainer))
        val checkedColor = ColorStateList.valueOf(resources.getColor(R.color.md_theme_light_primary))
        if(checked) {
            binding.btnBookmarks.imageTintList = checkedColor
            contentBinding.btnSubmit.text = "북마크 제거"

        } else {
            binding.btnBookmarks.imageTintList = baseColor
            contentBinding.btnSubmit.text = "추가하기"

        }
    }

    private fun initCollectionBottomSheet(user: FirebaseUser) {
        collectionBottomSheet.setContentView(contentBinding.root)

        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)
            .collection(FirebaseUtil.COLLECTION_BOOKMARK)
            .document(book!!.itemId.toString())

        contentBinding.btnSubmit.setOnClickListener {
            if(!bookmarkChecked) {
                ref.set(mapOf(
                    FirebaseUtil.FIELD_TITLE to book!!.title,
                    FirebaseUtil.FIELD_AUTHOR to book!!.author,
                    FirebaseUtil.FIELD_COVER to book!!.cover,
                )).addOnSuccessListener {
                    playLottie()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                ref.delete().addOnSuccessListener {

                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
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
        binding.seriesRecyclerView.layoutManager = object : GridLayoutManager(context, 3) {
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
        bookMarkListener?.remove()
        _binding = null
    }
}