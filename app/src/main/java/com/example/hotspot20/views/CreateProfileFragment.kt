package com.example.hotspot20.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.model.User
import com.example.hotspot20.viewmodel.AuthViewModel
import com.google.firebase.auth.UserProfileChangeRequest


class CreateProfileFragment : Fragment() {

    private var nameEdit: EditText? = null
    private var dateOfBirthEdit: EditText? = null
    private var bioEdit: EditText? = null
    private var createBtn: Button? = null
    private var uploadBtn: Button? = null
    private var profilePic: ImageView? = null
    private var viewModel: AuthViewModel? = null
    private val SELECT_PICTURE = 1

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
        uploadBtn = view.findViewById(R.id.uploadPicBtn)
        bioEdit = view.findViewById(R.id.createBio)
        profilePic = view.findViewById(R.id.profilePic)


        createBtn!!.setOnClickListener {
            var birthday = dateOfBirthEdit!!.text.toString()
            var name = nameEdit!!.text.toString()
            var bio = bioEdit!!.text.toString()

            if (birthday.isEmpty()) {
                dateOfBirthEdit!!.error = "Please enter your birthday"
            } else if (name.isEmpty()) {
                nameEdit!!.error = "Please enter name"
            } else if (bio.isEmpty()) {
                bioEdit!!.error = "Please enter a bio"
            } else if (birthday.isEmpty() && name.isEmpty() && bio.isEmpty()) {
                dateOfBirthEdit!!.error = "Please enter your birthday"
                nameEdit!!.error = "Please enter name"
                bioEdit!!.error = "Please enter a bio"
            } else {
                viewModel!!.saveUser(User(name, birthday, bio))
                viewModel!!.userInfo.observe(
                    requireActivity(),
                    { boolean ->
                        if (boolean) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_createProfileFragment_to_hotspotFragment)
                        }
                    }
                )
            }
        }

        uploadBtn!!.setOnClickListener {
            imageChooser()
        }
    }

    private fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        val pickIntent =
            Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(i, "Pick Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickIntent)
        startActivityForResult(chooserIntent, SELECT_PICTURE)

    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode === SELECT_PICTURE) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (resultCode == RESULT_OK) {
                // Get the url of the image from data
                var uri = data!!.data

                profilePic!!.setImageURI(uri)
                viewModel!!.uploadProfilePic(uri!!)
            }
        }
    }
}

