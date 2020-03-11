package com.example.photobloom.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun Uri.toBitmap(context: Context) : Bitmap?{
    return try {
        if(Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                this
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

class Utils {

    companion object{
        fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        @Throws(IOException::class)
        fun createImageFile(context: Context): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        }

        fun saveFile(image: Bitmap, name: String, path: String) : Boolean{
            val file = File(path, "$name.jpg")
            if (file.exists()) {
                file.delete()
            }
            return try {
                val out = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun getFilesFromStorage(root : File) : ArrayList<File>{
            val a : ArrayList<File> = ArrayList()

            if (root.exists()){
                val files = root.listFiles()
                if (files != null && files.isNotEmpty())
                    for (i in files.indices){
                        if (files[i].name.endsWith(".jpg")){
                            a.add(files[i])
                        }
                    }
            } else{
                root.mkdirs()
            }
            return a
        }

        fun getDate(milliSeconds: Long) : String
        {
            return try {
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = milliSeconds
                formatter.format(calendar.time)
            } catch (e : java.lang.Exception){
                ""
            }
        }
    }
}