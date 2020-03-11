package com.example.photobloom.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.example.photobloom.R
import com.example.photobloom.databinding.NameDialogLayoutBinding
import com.example.photobloom.ui.main.FileNameDialogListener

class FileNameDialog(context: Context, private val bitmap: Bitmap?, private val listener: FileNameDialogListener)  : Dialog(context), TextWatcher {

   lateinit var binding : NameDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        if (bitmap == null) dismiss()

        binding = DataBindingUtil.inflate<NameDialogLayoutBinding>(LayoutInflater.from(context), R.layout.name_dialog_layout, null,false)
        setContentView(binding.root)

        setView()
    }

    private fun setView() {
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setCanceledOnTouchOutside(false)
        binding.previewImage.setImageBitmap(bitmap)
        binding.submitBt.isEnabled = false
        setListeners()
    }

    private fun setListeners() {
        binding.inputName.addTextChangedListener(this)

        binding.submitBt.setOnClickListener {
            listener.nameEntered(bitmap!!, binding.inputName.text.toString())
            dismiss()
        }

        binding.cancelBt.setOnClickListener {
            dismiss()
        }
    }

    override fun afterTextChanged(s: Editable?) {
        binding.submitBt.isEnabled = !s.isNullOrEmpty()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}