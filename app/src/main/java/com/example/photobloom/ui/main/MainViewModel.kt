package com.example.photobloom.ui.main

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photobloom.utils.Utils
import com.example.photobloom.utils.Utils.Companion.getFilesFromStorage
import java.io.File


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val path = application.getExternalFilesDir(null)?.absolutePath + File.separator + "images/"
    private val _filesList = MutableLiveData<ArrayList<File>>()
    private val root = File(path)
    val filesList : LiveData<ArrayList<File>> get() = _filesList

    init {
        _filesList.value = getFilesFromStorage(root)
    }

    fun saveFile(file: Bitmap, name: String){
        if (Utils.saveFile(file, name, path))
            _filesList.value = getFilesFromStorage(root)
    }
}
