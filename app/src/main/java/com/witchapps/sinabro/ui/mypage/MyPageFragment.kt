package com.witchapps.sinabro.ui.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.witchapps.sinabro.R
import com.witchapps.sinabro.databinding.FragmentMyPageBinding
import com.witchapps.sinabro.ui.MainActivity
import com.witchapps.sinabro.ui.util.FirebaseUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private var snapShotListener: ListenerRegistration? = null

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val oneTapClientLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    signInWithCredential(credential)
                } catch (exception: ApiException) {
                    Snackbar.make(binding.root, exception.localizedMessage, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAuth()
        updateUI()

        val user = Firebase.auth.currentUser
        if(user != null) {
            initSnapShotListener(user)
        }
    }

    private fun initSnapShotListener(user: FirebaseUser){
        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)

        snapShotListener = ref.addSnapshotListener() { value, error ->
            if(error != null) return@addSnapshotListener

            if(value != null && value.exists()) {
                val name = value.getString(FirebaseUtil.FIELD_NAME)
                val email = value.getString(FirebaseUtil.FIELD_EMAIL)
                val introduction = value.getString(FirebaseUtil.FIELD_INTRODUCTION)

                binding.textName.text = name
                binding.textIntroduction.text = introduction?:"자기소개가 비어있습니다"

                Glide.with(binding.root)
                    .load(user.photoUrl)
                    .circleCrop()
                    .into(binding.imageProfile)
            }
        }
    }

    private fun initAuth() {
        initGoogleAuth()
        binding.btnAuth.setOnClickListener {
            oneTapClient
                .beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        oneTapClientLauncher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
        }

        binding.textEditProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        binding.textSignOut.setOnClickListener {
            Firebase.auth.signOut()
            updateUI()
        }
    }

    private fun signInWithCredential(credential: SignInCredential) {
        val idToken = credential.googleIdToken
        if (idToken != null) {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

            Firebase.auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener {
                    // 로그인 성공
                    updateFirebase(it.user!!)
                    binding.textName.text = it.user!!.displayName

                    Glide.with(binding.root)
                        .load(it.user!!.photoUrl)
                        .circleCrop()
                        .into(binding.imageProfile)
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateFirebase(user: FirebaseUser) {

        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)

        ref.get().addOnSuccessListener { snapshot ->
            if(!snapshot.exists()) {
                ref.set(hashMapOf(
                    FirebaseUtil.FIELD_NAME to user.displayName,
                    FirebaseUtil.FIELD_EMAIL to user.email
                ))
            }
            updateUI()
        }.addOnFailureListener {
            Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun initGoogleAuth() {

        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.google_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun updateUI() {
        val user = Firebase.auth.currentUser
        if (user == null) { // 비로그인
            binding.layoutOption.visibility = View.GONE
            binding.layoutProfile.visibility = View.GONE
            binding.imageIllustration.visibility = View.VISIBLE

            binding.btnAuth.visibility = View.VISIBLE
        } else { // 로그인
            binding.layoutOption.visibility = View.VISIBLE
            binding.layoutProfile.visibility = View.VISIBLE
            binding.imageIllustration.visibility = View.GONE

            binding.btnAuth.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapShotListener?.remove()
        _binding = null
    }
}