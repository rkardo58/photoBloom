package com.example.photobloom.ui.main

import android.Manifest
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photobloom.ui.dialogs.FileNameDialog
import com.example.photobloom.utils.Utils
import com.example.photobloom.utils.Utils.Companion.getFilesFromStorage
import java.io.File


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val path = application.getExternalFilesDir(null)?.absolutePath + File.separator + "images/"
    private val _filesList = MutableLiveData<ArrayList<File>>()
    private val root = File(path)
    val filesList : LiveData<ArrayList<File>> get() = _filesList
    val cameraPermissions : Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    val filesPermissions : Array<String> = cameraPermissions.copyOfRange(0,2)
    var currentPhotoUri: Uri? = null
    var dialog : FileNameDialog? = null

    init {
        _filesList.value = getFilesFromStorage(root)
    }

    fun saveFile(file: Bitmap, name: String){
        if (Utils.saveFile(file, name, path))
            _filesList.value = getFilesFromStorage(root)
    }

}
