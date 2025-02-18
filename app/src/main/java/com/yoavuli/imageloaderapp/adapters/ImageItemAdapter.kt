package com.yoavuli.imageloaderapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yoavuli.imageloader.ImageLoaderFactory
import com.yoavuli.imageloaderapp.R
import com.yoavuli.imageloaderapp.data.ImageModel

class ImageItemAdapter : RecyclerView.Adapter<ImageItemAdapter.ImageViewHolder>() {

    private val items : MutableList <ImageModel> = mutableListOf()
    private var height : Int = 600
    private var width : Int = 1020
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = items[position]
        holder.idTitle.text = holder.itemView.context.getString(R.string.image_item_id_title, item.id)

        ImageLoaderFactory.init(holder.itemView.context)

        ImageLoaderFactory.load(item.imageUrl)
            .placeholder(R.drawable.loading)
            .errorPlaceholder(R.drawable.error)
            .resize(width, height)
            .setView(holder.imageView)
        holder.imageView.doOnLayout {
             height = it.height
             width = it.width
        }

    }


    fun updateList(images : List<ImageModel>){
        val diffCallback = ImageDiffCallback(items, images)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(images)
        diffResult.dispatchUpdatesTo(this)
    }

    class   ImageViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val idTitle: TextView = view.findViewById(R.id.idTitle)
    }

}

