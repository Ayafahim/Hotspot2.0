package com.example.hotspot20.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.hotspot20.model.User
import com.example.hotspot20.repositories.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var authRepo = AuthRepository(application)
    var userData = authRepo.firebaseUserMutableData
    var userInfo = authRepo.userInfoMutableLiveData
    var loggedStatus = authRepo.userLoggedMutableLiveData


    fun register(email: String, password: String) {
        authRepo.register(email, password)
    }

    fun logIn(email: String, password: String) {
        authRepo.logIn(email, password)
    }

    fun logOut() {
        authRepo.logOut()
    }

    fun saveUser(user: User) {
        authRepo.saveUser(user)
    }

    fun updateUser(user: User) {
        authRepo.updateUser(user)
    }

    fun uploadProfilePic(pickedImage: Uri) {
        authRepo.uploadImage(pickedImage)
    }

    fun resetPassword(email: String) {
        authRepo.resetPassword(email)
    }
}