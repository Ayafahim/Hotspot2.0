package com.example.hotspot20.views

import android.R.attr
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.text.TextUtils
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
import android.content.Intent
import android.net.Uri

import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.attr.data
import android.graphics.Bitmap
import android.provider.MediaStore


class CreateProfileFragment : Fragment() {

    private var nameEdit: EditText? = null
    private var dateOfBirthEdit: EditText? = null
    private var bioEdit: EditText? = null
    private var createBtn: Button? = null
    private var uploadBtn: Button? = null
    private var profilePic: ImageView? = null
    private var viewModel: AuthViewModel? = null
    private val SELECT_PICTURE = 200

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
        profilePic = view.findViewById(R.id.profilepic)

        createBtn!!.setOnClickListener {
            var birthday = dateOfBirthEdit!!.text.toString()
            var name = nameEdit!!.text.toString()
            var bio = bioEdit!!.text.toString()

            if (birthday.isEmpty()) {
                dateOfBirthEdit!!.error = "Please enter your birthday"
            }
            if (name.isEmpty()) {
                nameEdit!!.error = "Please enter name"
            }
            if (bio.isEmpty()) {
                bioEdit!!.error = "Please enter a bio"
            }
            if (bio.isEmpty() && name.isEmpty() && birthday.isEmpty()) {
                dateOfBirthEdit!!.error = "Please enter your birthday"
                nameEdit!!.error = "Please enter name"
                bioEdit!!.error = "Please enter a bio"
            } else {
                viewModel!!.saveUser(User(name, birthday, bio))
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_createProfileFragment_to_hotspotFragment)

            }
        }

        uploadBtn!!.setOnClickListener {
            imageChooser()
        }
    }

    private fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (resultCode === RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode === SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = intent!!.data
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, Uri.parse(
                        selectedImageUri.toString()
                    )
                )
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    viewModel!!.uploadProfilePic(selectedImageUri)

                    //profilePic!!.setImageURI(selectedImageUri)
                }
            }
        }
    }
}