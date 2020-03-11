package com.example.photobloom.utils

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.photobloom.R


@BindingAdapter("imageFromFilePath")
fun setImageFromFile(imageView: ImageView, path: String) {
        try {
            val myBitmap = BitmapFactory.decodeFile(path)
            imageView.setImageBitmap(myBitmap)
        } catch (e: Exception){
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_picture))
        }
}