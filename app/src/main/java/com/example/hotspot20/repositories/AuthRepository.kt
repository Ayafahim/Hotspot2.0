package com.example.hotspot20.repositories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.hotspot20.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthRepository(application: Application) {
    private var application: Application = application
    lateinit var user: User
    var firebaseUserMutableData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val database = Firebase.database

    init {

        if (auth.currentUser != null) {
            firebaseUserMutableData.postValue((auth.currentUser))
        }
    }

    public fun saveUser(user: User){
        val ref = database.getReference("users")
        val userId = auth.currentUser!!.uid
        ref.child(userId).setValue(user).addOnCompleteListener{
            Toast.makeText(application, "Successfull", Toast.LENGTH_SHORT).show()
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




