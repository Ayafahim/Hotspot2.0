package com.example.hotspot20.repositories

import android.app.Application
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.hotspot20.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AuthRepository(application: Application) {
    private var application: Application = application
    var firebaseUserMutableData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var userInfoMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val database = Firebase.database
    val ref = database.getReference("users")




    init {
        if (auth.currentUser != null) {
            firebaseUserMutableData.postValue((auth.currentUser))
        }
    }

    fun saveUser(user: User) {
        val userId = auth.currentUser!!.uid
        ref.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(application, "Your info has now been added", Toast.LENGTH_SHORT)
                    .show()
                userInfoMutableLiveData.postValue(true)
            } else {
                Toast.makeText(
                    application,
                    task.exception!!.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateUser(user: User) {
        val userId = auth.currentUser!!.uid
        val nUser = mapOf<String, String>(
            "name" to user.name,
            "dateOfBirth" to user.dateOfBirth,
            "bio" to user.bio
        )
        ref.child(userId).updateChildren(nUser).addOnSuccessListener {
            Toast.makeText(application, "Your info has been updated", Toast.LENGTH_SHORT).show()
        }
    }


    fun register(email: String, password: String) {
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
                } else {
                    Toast.makeText(
                        application,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun logOut() {
        auth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }

    fun logIn(email: String, password: String) {
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
        var userID = auth.currentUser!!.uid
        var myStorage = FirebaseStorage.getInstance().reference.child("profile_pics")
        var imagePath = myStorage.child("$userID.jpeg")

        imagePath.putFile(pickedImage).addOnSuccessListener {
            getDownloadUrl(imagePath)
        }
    }


    private fun getDownloadUrl(storageReference: StorageReference) {
        storageReference.downloadUrl.addOnSuccessListener(OnSuccessListener {
            Log.d(TAG, "onSuccess:$it")
            setUserProfileUrl(it)
        })
    }

    private fun setUserProfileUrl(it: Uri?) {
        val user = auth.currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(it).build()
        user!!.updateProfile(request).addOnSuccessListener {
            Toast.makeText(application, "Picture Uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}









