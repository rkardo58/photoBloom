package com.example.photobloom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.photobloom.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        if (navHostFragment != null && navHostFragment.childFragmentManager.fragments[0] is MainFragment)
            finish()
    }
}
