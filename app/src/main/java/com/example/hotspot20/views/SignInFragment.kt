package com.example.hotspot20.views

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser


class SignInFragment : Fragment() {

    var emailEdit: EditText? = null
    var passwordEdit: EditText? = null
    var loginBtn: Button? = null
    var registerText: TextView? = null
    var resetPassword: TextView? = null
    var viewModel: AuthViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[AuthViewModel::class.java]
        viewModel!!.userData.observe(this,
            { firebaseUser ->
                if (firebaseUser != null) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_signInFragment_to_hotspotFragment)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEdit = view.findViewById(R.id.loginEmail)
        passwordEdit = view.findViewById(R.id.loginPassword)
        loginBtn = view.findViewById(R.id.loginBtn)
        registerText = view.findViewById(R.id.registerAcc)
        resetPassword = view.findViewById(R.id.forgotPassword)

        resetPassword!!.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        }

        registerText!!.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_signInFragment_to_registerFragment)
        }

        loginBtn!!.setOnClickListener {
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
                viewModel!!.logIn(email, password)
                viewModel!!.userData.observe(
                    requireActivity(),
                    { firebaseUser ->
                        if (firebaseUser != null) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_signInFragment_to_hotspotFragment)
                        }
                    }
                )
            }
        }
    }
}