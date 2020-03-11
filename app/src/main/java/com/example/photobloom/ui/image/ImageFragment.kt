package com.example.photobloom.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.photobloom.R
import com.example.photobloom.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentImageBinding>(inflater, R.layout.fragment_image, container, false)
        binding.path = ImageFragmentArgs.fromBundle(
            requireArguments()
        ).path
        binding.name = ImageFragmentArgs.fromBundle(
            requireArguments()
        ).name
        return binding.root
    }

}
