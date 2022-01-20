package com.example.hotspot20.views

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser

class RegisterFragment : Fragment() {

    var emailEdit: EditText? = null
    var passwordEdit: EditText? = null
    var registerBtn: Button? = null
    var viewModel: AuthViewModel? = null
    var logInText: TextView? = null
    var succes = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[AuthViewModel::class.java]
        viewModel!!.userData.observe(this, object : Observer<FirebaseUser> {
            override fun onChanged(firebaseUser: FirebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_signInFragment)
                }
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEdit = view.findViewById(R.id.registerEmail)
        passwordEdit = view.findViewById(R.id.registerPassword)
        registerBtn = view.findViewById(R.id.registerBtn)
        logInText = view.findViewById(R.id.backFromRegister)

        logInText!!.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registerFragment_to_signInFragment)
        }

        registerBtn!!.setOnClickListener {
            var email = emailEdit!!.text.toString().trim()
            var password = passwordEdit!!.text.toString().trim()

            if (email.isEmpty()) {
                emailEdit!!.error = "Please enter email"
            } else if (password.isEmpty()) {
                passwordEdit!!.error = "Please enter a password"
            } else if (email.isEmpty() && password.isEmpty()) {
                passwordEdit!!.error = "Please enter a password"
                emailEdit!!.error = "Please enter email"
            } else {
                viewModel!!.register(email, password)

                viewModel!!.userData.observe(
                    requireActivity(),
                    { firebaseUser ->
                        if (firebaseUser != null) {
                            if(Navigation.findNavController(requireView()).currentDestination?.id == R.id.registerFragment) {
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_registerFragment_to_createProfileFragment)
                            }
                        }
                    })
            }


        }
    }
}