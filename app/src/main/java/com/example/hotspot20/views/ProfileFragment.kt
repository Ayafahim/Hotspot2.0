package com.example.hotspot20.views

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.hotspot20.R
import com.example.hotspot20.repositories.AuthRepository
import com.example.hotspot20.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileFragment : Fragment() {

    var viewModel: AuthViewModel? = null
    var nameView: TextView? = null
    var birthdayView: TextView? = null
    var bioView: TextView? = null
    var profilePic: ImageView? = null
    var imageView1: ImageView? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = Firebase.database
    val ref = database.getReference("users")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nameView = view.findViewById(R.id.nameView)
        bioView = view.findViewById(R.id.aboutViewText)
        birthdayView = view.findViewById(R.id.birthdayView)
        return view

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[AuthViewModel::class.java]

        viewModel!!.loggedStatus.observe(this, object : Observer<Boolean> {
            override fun onChanged(boolean: Boolean) {
                if (boolean) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_profileFragment_to_signInFragment)
                }
            }
        })

        val userId = auth.currentUser!!.uid
//      Read from the database
        ref.child(userId).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var userName = snapshot.child("name").value.toString()
                var userBio = snapshot.child("dateOfBirth").value.toString()
                var userBirthday = snapshot.child("bio").value.toString()


                nameView!!.text = userName
                birthdayView!!.text = "Born: $userBio"
                bioView!!.text = userBirthday
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsBtn = view.findViewById<ImageView>(R.id.settingsBtn)
        profilePic = view.findViewById(R.id.profilePicp)
        imageView1 = view.findViewById(R.id.imageView1)

        val user = auth.currentUser
        val uri = user!!.photoUrl
        Glide.with(context)
            .load(uri)
            .into(profilePic!!)


        settingsBtn.display
        settingsBtn.setOnClickListener {
            val popupMenu = PopupMenu(activity, settingsBtn)
            popupMenu.inflate(R.menu.settings_menu)

            popupMenu.setOnMenuItemClickListener { MenuItem ->
                val id = MenuItem.itemId
                if (id == R.id.nav_logout) {
                    viewModel!!.logOut()
                    Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show()
                } else {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_profileFragment_to_editProfileFragment)
                }
                true
            }
            popupMenu.show()
        }
    }
}
