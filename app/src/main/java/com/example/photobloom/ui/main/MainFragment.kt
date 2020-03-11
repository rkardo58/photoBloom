package com.example.photobloom.ui.main

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.photobloom.R
import com.example.photobloom.databinding.MainFragmentBinding
import com.example.photobloom.ui.dialogs.FileNameDialog
import com.example.photobloom.utils.Utils
import com.example.photobloom.utils.Utils.Companion.hasPermissions
import com.example.photobloom.utils.toBitmap
import java.io.File
import java.io.IOException

const val CAMERA_REQUEST_CODE = 101
const val FILE_REQUEST_CODE = 102
const val REQUEST_IMAGE_CAPTURE = 1
const val REQUEST_IMAGE_FROM_GALLERY = 2

interface FileNameDialogListener{
    fun nameEntered(file: Bitmap, name: String)
}

class MainFragment : Fragment(), FileNameDialogListener, OnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private val cameraPermissions : Array<String> = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA)
    private val filesPermissions : Array<String> = cameraPermissions.copyOfRange(0,2)
    lateinit var currentPhotoUri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.cameraButton.setOnClickListener {
            if (hasPermissions(requireContext(), cameraPermissions)){
                openCamera()
            } else{
                checkPermissions(cameraPermissions, CAMERA_REQUEST_CODE)
            }
        }

        binding.fileButton.setOnClickListener {
            if (hasPermissions(requireContext(), filesPermissions)){
                openFiles()
            } else{
                checkPermissions(filesPermissions, FILE_REQUEST_CODE)
            }
        }
    }

    private fun setObservers() {
        viewModel.filesList.observe(viewLifecycleOwner, Observer {
            if (binding.filesList.adapter == null){
                binding.filesList.adapter = FilesAdapter(this)
            }
            (binding.filesList.adapter as FilesAdapter).submitList(it)
        })
    }

    private fun checkPermissions(list: Array<String>, code: Int) {
        requestPermissions(list, code)
    }

    private fun openFiles() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }

    private fun openCamera() {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {

                    val photoFile: File? = try {
                        Utils.createImageFile(requireContext())
                    } catch (ex: IOException) {
                        Toast.makeText(requireContext(), "error creating file", Toast.LENGTH_SHORT).show()
                        null
                    }
                    photoFile?.also { currentPhotoUri = FileProvider.getUriForFile(requireContext(),
                        "com.example.android.fileprovider",
                            it
                        )
                        currentPhotoUri
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
            when (requestCode) {
                CAMERA_REQUEST_CODE -> openCamera()
                FILE_REQUEST_CODE -> openFiles()
                else -> return
            }
        } else {
            Toast.makeText(requireContext(), "permissionMissing", Toast.LENGTH_LONG).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null){
                FileNameDialog(requireContext(), when (requestCode){
                    REQUEST_IMAGE_CAPTURE ->{
                        currentPhotoUri.toBitmap(requireContext())
                    }

                    REQUEST_IMAGE_FROM_GALLERY -> {
                        data.data?.toBitmap(requireContext())
                    }
                    else -> return
                }, this).show()
            }
        }
    }

    override fun nameEntered(file: Bitmap, name: String) {
        viewModel.saveFile(file, name)
    }

    override fun onClick(path: String, name: String) {
        view?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToImageFragment(path, name))
    }
}
