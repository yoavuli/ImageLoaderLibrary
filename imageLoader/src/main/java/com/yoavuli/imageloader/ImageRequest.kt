package com.yoavuli.imageloader

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * holds all the image request parameters
 * @param url image url
 * @param factory image loader factory
 */
class ImageRequest ( val url : String, private val factory : ImageLoaderFactory ){

    private var placeholder : Int? = null
    private var errorPlaceholder : Int? = null

    val settings = RequestSettings()

    fun resize(width: Int, height: Int): ImageRequest {
        settings.resize(width, height)
        return this
    }

    fun placeholder(resId : Int) = apply { this.placeholder = resId }
    fun errorPlaceholder(resId : Int) = apply { this.errorPlaceholder = resId }
    fun setView(view : View){

        CoroutineScope(Dispatchers.Main).launch {
            // set placeholder before fetching image .support both imageView and view
            if (view is ImageView) view.setImageResource(placeholder ?: 0)
            else view.setBackgroundResource(placeholder ?: 0)
            val bitmap = factory.execute(this@ImageRequest)
            if (bitmap != null) {
                if (view is ImageView) view.setImageBitmap(bitmap)
                else view.background = BitmapDrawable(view.resources, bitmap)
                view.alpha = 0f
                view.animate().alpha(1f).setDuration(300).start()

            } else
                // set error placeholder if image not found. support both imageView and view
                errorPlaceholder?.let {
                    if (view is ImageView) view.setImageResource(it)
                    else view.setBackgroundResource(it)
                }

        }


    }
}