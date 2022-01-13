package com.example.hotspot20.views

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.model.User
import com.example.hotspot20.viewmodel.AuthViewModel


class CreateProfileFragment : Fragment() {

    var nameEdit: EditText? = null
    var dateOfBirthEdit: EditText? = null
    var createBtn: Button? = null
    var viewModel: AuthViewModel? = null

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
        return inflater.inflate(R.layout.fragment_create_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEdit = view.findViewById(R.id.Name)
        dateOfBirthEdit = view.findViewById(R.id.birthday)
        createBtn = view.findViewById(R.id.createProfileBtn)

        createBtn!!.setOnClickListener {
            var birthday = dateOfBirthEdit!!.text.toString()
            var name = nameEdit!!.text.toString()

            if (!TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(name)) {
                viewModel!!.saveUser(User(name, birthday))
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_createProfileFragment_to_hotspotFragment)
            }
        }
    }
}