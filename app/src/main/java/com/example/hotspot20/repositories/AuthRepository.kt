package com.example.hotspot20.repositories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository(application: Application) {
    private var application: Application = application

    var firebaseUserMutableData: MutableLiveData<FirebaseUser> = MutableLiveData()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {

        if (auth.currentUser != null) {
            firebaseUserMutableData.postValue((auth.currentUser))
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
}




