package com.example.photobloom.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.photobloom.R
import com.example.photobloom.databinding.LoginFragmentBinding
import com.example.photobloom.utils.AuthenticationState
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

const val TAG = "LoginFragment"
const val SIGN_IN_REQUEST_CODE = 3
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding : LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { if (it == AuthenticationState.Authenticated) goToMain() })
        binding.gmail.setOnClickListener { launchSignInFlow(AuthUI.IdpConfig.GoogleBuilder().build()) }
        binding.email.setOnClickListener { launchSignInFlow(AuthUI.IdpConfig.EmailBuilder().build()) }
    }

    private fun goToMain() {
        Navigation.findNavController(view!!).navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun launchSignInFlow(authMethod : AuthUI.IdpConfig) {
        val providers = arrayListOf(authMethod)
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(providers).build(), SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED){
            Log.i(TAG, "${IdpResponse.fromResultIntent(data)?.error?.errorCode}")
            Toast.makeText(requireContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show()
        }
    }

}
