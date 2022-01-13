package com.example.hotspot20.repositories

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.hotspot20.R
import com.example.hotspot20.model.User
import com.example.hotspot20.views.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AuthRepository(application: Application) {
    private var application: Application = application
    var firebaseUserMutableData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val database = Firebase.database
    var loginSuccess = false
    var registerSuccess = false
    var resetSuccess = false
    var saveSuccess = false

    init {

        if (auth.currentUser != null) {
            firebaseUserMutableData.postValue((auth.currentUser))
        }
    }


    public fun saveUser(user: User) {
        val ref = database.getReference("users")
        val userId = auth.currentUser!!.uid
        ref.child(userId).setValue(user).addOnCompleteListener {
            Toast.makeText(application, "Successfull", Toast.LENGTH_SHORT).show()
            saveSuccess = true
        }

    }


    public fun register(email: String, password: String) {
        email.trim()
        password.trim()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserMutableData.postValue(auth.currentUser)
                    Toast.makeText(
                        application,
                        "You are now registered",
                        Toast.LENGTH_SHORT
                    ).show()
                    registerSuccess = true
                } else {
                    Toast.makeText(
                        application,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    public fun logOut() {
        auth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }

    public fun logIn(email: String, password: String) {
        email.trim()
        password.trim()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserMutableData.postValue(auth.currentUser)
                    Toast.makeText(
                        application,
                        "Welcome back",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(
                        application,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun resetPassword(email: String) {
        email.trim()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    application,
                    "Email to reset your password has been sent",
                    Toast.LENGTH_SHORT
                ).show()
                registerSuccess = true
            } else {
                Toast.makeText(
                    application,
                    it.exception!!.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun uploadImage(pickedImage: Uri) {
        var myStorage = FirebaseStorage.getInstance().reference.child("user_photos")
        var imagePath = myStorage.child(pickedImage.lastPathSegment!!)
        var userID = auth.currentUser!!.uid
        imagePath.child(userID).putFile(pickedImage).addOnSuccessListener {
            Toast.makeText(application, "Picture Uploaded", Toast.LENGTH_SHORT).show()
        }

    }
}




