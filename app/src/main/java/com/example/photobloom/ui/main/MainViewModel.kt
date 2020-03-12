package com.example.photobloom.ui.main

import android.Manifest
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photobloom.utils.Utils
import com.example.photobloom.utils.Utils.Companion.getFilesFromStorage
import com.example.photobloom.utils.toByteArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

enum class FileUploadStateEnum{
    NOT_UPLOADING, UPLOADED, UPLOAD_FAIL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val path = application.getExternalFilesDir(null)?.absolutePath + File.separator + "images/"
    private val root = File(path)
    private val _filesList = MutableLiveData<ArrayList<File>>()
    val filesList : LiveData<ArrayList<File>> get() = _filesList
    private val _uploadState = MutableLiveData<FileUploadStateEnum>()
    val uploadState : LiveData<FileUploadStateEnum> get() = _uploadState
    val cameraPermissions : Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    val filesPermissions : Array<String> = cameraPermissions.copyOfRange(0,2)
    var currentPhotoUri: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val viewModelJob = Job()
    private val coroutineScope = IO + viewModelJob

    init {
        _filesList.value = getFilesFromStorage(root)
    }

    fun saveFile(file: Bitmap, name: String){
        if (Utils.saveFile(file, name, path))
            _filesList.value = getFilesFromStorage(root)
        if (FirebaseAuth.getInstance().currentUser != null){
            uploadImage(file, name, FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }

    private fun uploadImage(bitmap: Bitmap, name: String, uid: String){
        CoroutineScope(coroutineScope).launch {
            val imageReference = storageReference.child("$uid/$name.jpg")
            val uploadTask = imageReference.putBytes(bitmap.toByteArray())
            uploadTask.addOnSuccessListener { _uploadState.value = FileUploadStateEnum.UPLOADED }.addOnFailureListener{_uploadState.value = FileUploadStateEnum.UPLOAD_FAIL}
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
