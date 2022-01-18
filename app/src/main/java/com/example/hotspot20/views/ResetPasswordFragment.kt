package com.example.hotspot20.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.viewmodel.AuthViewModel


class ResetPasswordFragment : Fragment() {

    private var viewModel: AuthViewModel? = null
    private var submitBtn: Button? = null
    private var emailEdit: EditText? = null
    private var registerText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEdit = view.findViewById(R.id.resetEmail)
        submitBtn = view.findViewById(R.id.submit)
        registerText = view.findViewById(R.id.registerAcc)


        registerText!!.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_resetPasswordFragment_to_registerFragment)
        }

        submitBtn!!.setOnClickListener {
            var email = emailEdit!!.text.toString().trim()

            if (email.isEmpty()) {
                emailEdit!!.error = "Please enter email"
            } else {
                viewModel!!.resetPassword(email)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_resetPasswordFragment_to_signInFragment)
            }
        }

    }
}