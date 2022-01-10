package com.example.hotspot20.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    var viewModel: AuthViewModel? = null
    var logOutBtn: Button? = null

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
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsBtn = view.findViewById<ImageView>(R.id.settingsBtn)

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
                    Toast.makeText(activity, "Edit profile", Toast.LENGTH_SHORT).show()
                }
                true
            }
            popupMenu.show()
        }

    }
}