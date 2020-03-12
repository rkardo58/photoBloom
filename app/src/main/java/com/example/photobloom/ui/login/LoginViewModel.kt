package com.example.photobloom.ui.login

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.photobloom.utils.AuthenticationState
import com.example.photobloom.utils.FirebaseUserLiveData
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    val authenticationState = Transformations.map(FirebaseUserLiveData()) { input: FirebaseUser? -> if (input != null) AuthenticationState.Authenticated else AuthenticationState.Unauthenticated}

}
