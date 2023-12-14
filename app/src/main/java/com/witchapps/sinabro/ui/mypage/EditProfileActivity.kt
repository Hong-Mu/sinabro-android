package com.witchapps.sinabro.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.witchapps.sinabro.R
import com.witchapps.sinabro.databinding.ActivityEditProfileBinding
import com.witchapps.sinabro.ui.util.FirebaseUtil

class EditProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser
        if(user != null) {
            updateUI(user)
        }
    }

    private fun updateUI(user: FirebaseUser) {
        val ref = Firebase.firestore
            .collection(FirebaseUtil.COLLECTION_USER)
            .document(user.uid)

        ref.get().addOnSuccessListener { snapshot ->
            if(!snapshot.exists()) {
                return@addOnSuccessListener
            }

            snapshot.data?.let {
                binding.inputName.setText(it.get(FirebaseUtil.FIELD_NAME).toString())
                binding.inputEmail.setText(it.get(FirebaseUtil.FIELD_EMAIL).toString())
                binding.inputIntroduction.setText(it.getOrDefault(FirebaseUtil.FIELD_INTRODUCTION, "").toString())
            }
        }.addOnFailureListener {
            Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }

        binding.btnSubmit.setOnClickListener {
            val newName = binding.inputName.text.toString()
            val newIntroduction = binding.inputIntroduction.text.toString()
            ref.update(mapOf(
                FirebaseUtil.FIELD_NAME to newName,
                FirebaseUtil.FIELD_INTRODUCTION to newIntroduction,
            )).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}