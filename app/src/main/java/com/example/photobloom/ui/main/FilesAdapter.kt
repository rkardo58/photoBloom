package com.example.photobloom.ui.main

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photobloom.databinding.FileItemLayoutBinding
import com.example.photobloom.utils.Utils.Companion.getDate
import java.io.File

interface OnClickListener{
    fun onClick(path: String, name: String)
}
class FilesAdapter(private val listener: OnClickListener) :  ListAdapter<File, FilesAdapter.ViewHolder>(FilesDiffCallback()) {

    class ViewHolder private constructor(private val binding: FileItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File, holder: ViewHolder) {
            holder.binding.file = file
            val lastModified = getDate(file.lastModified())
            holder.binding.date = if (lastModified.isEmpty()) "" else " - $lastModified"
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FileItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file, holder)
        holder.itemView.setOnClickListener {
            listener.onClick(file.absolutePath, file.name)
        }
    }
}

class FilesDiffCallback: DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.name == newItem.name &&  BitmapFactory.decodeFile(oldItem.absolutePath).equals( BitmapFactory.decodeFile(newItem.absolutePath))
    }

}
