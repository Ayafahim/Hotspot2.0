package com.example.hotspot20.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.model.User
import com.example.hotspot20.viewmodel.AuthViewModel


class EditProfileFragment : Fragment() {
    var editBtn: Button? = null
    var nameEdit: EditText? = null
    var bioEdit: EditText? = null
    var birthdayEdit: EditText? = null
    var viewModel: AuthViewModel? = null
    var uploadBtn: Button? = null
    var profilePic: ImageView? = null
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editBtn = view.findViewById(R.id.editProfileBtn)
        bioEdit = view.findViewById(R.id.EditBio)
        birthdayEdit = view.findViewById(R.id.Editbirthday)
        nameEdit = view.findViewById(R.id.EditName)
        uploadBtn = view.findViewById(R.id.uploadNewPicBtn)
        profilePic = view.findViewById(R.id.profilePicEdit)

        uploadBtn!!.setOnClickListener {
            imageChooser()
        }

        editBtn!!.setOnClickListener {

            var birthday = birthdayEdit!!.text.toString()
            var name = nameEdit!!.text.toString()
            var bio = bioEdit!!.text.toString()
            if (birthday.isEmpty()) {
                birthdayEdit!!.error = "Please enter your birthday"
            } else if (name.isEmpty()) {
                nameEdit!!.error = "Please enter name"
            } else if (bio.isEmpty()) {
                bioEdit!!.error = "Please enter a bio"
            } else if (birthday.isEmpty() && name.isEmpty() && bio.isEmpty()) {
                birthdayEdit!!.error = "Please enter your birthday"
                nameEdit!!.error = "Please enter name"
                bioEdit!!.error = "Please enter a bio"
            } else {
                viewModel!!.updateUser(User(name, birthday, bio))
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_editProfileFragment_to_profileFragment)
            }

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
            if (resultCode == Activity.RESULT_OK) {
                // Get the url of the image from data
                var uri = data!!.data

                profilePic!!.setImageURI(uri)
                viewModel!!.uploadProfilePic(uri!!)
            }
        }
    }
}