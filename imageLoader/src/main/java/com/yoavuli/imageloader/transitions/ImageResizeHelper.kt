package com.yoavuli.imageloader.transitions

import android.graphics.Bitmap
import com.yoavuli.imageloader.RequestSettings
import kotlin.math.min


/**
 * An helper class for resizing images
 */
class ImageResizeHelper {


    companion object{

         fun resizeBitmapIfNeeded(bitmap: Bitmap, settings: RequestSettings?): Bitmap {
            val width = settings?.width
            val height = settings?.height

            return if (width != null && height != null) {
                Bitmap.createScaledBitmap(bitmap, width, height, true)
            } else {
                downscaleIfTooLarge(bitmap)
            }
        }

         private fun downscaleIfTooLarge(bitmap: Bitmap, maxWidth: Int = 1080, maxHeight: Int = 1080): Bitmap {
            val width = bitmap.width
            val height = bitmap.height

            if (width > maxWidth || height > maxHeight) {
                val scaleFactor = min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
                return Bitmap.createScaledBitmap(bitmap, (width * scaleFactor).toInt(), (height * scaleFactor).toInt(), true)
            }
            return bitmap
        }
    }

}