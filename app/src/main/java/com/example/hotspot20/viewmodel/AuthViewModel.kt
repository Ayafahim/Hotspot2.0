package com.example.hotspot20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hotspot20.repositories.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var authRepo = AuthRepository(application)
    var userData = authRepo.firebaseUserMutableData
    var loggedStatus = authRepo.userLoggedMutableLiveData


    public fun register(email: String,password : String){
        authRepo.register(email,password)
    }
    public fun logIn(email: String,password: String){
        authRepo.logIn(email,password)
    }
    public fun logOut(){
        authRepo.logOut()
    }


}