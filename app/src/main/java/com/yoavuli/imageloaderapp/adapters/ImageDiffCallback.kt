package com.yoavuli.imageloaderapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.yoavuli.imageloaderapp.data.ImageModel

class ImageDiffCallback(private val oldList : List<ImageModel>,
                        private val newList : List<ImageModel>) : DiffUtil.Callback()  {
    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}