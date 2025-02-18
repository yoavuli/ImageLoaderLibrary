package com.yoavuli.imageloader.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Responsible for caching images in a local directory.
 * @param cacheDir The directory where the cached images will be stored.
 * @param expirationCache The maximum age of cached images in milliseconds.
 * @param maxSizeMB The maximum size of the cache in megabytes.
 */
class DiscCache(private val cacheDir: File,private val expirationCache: Long = 4 *60 * 60 * 1000 ,
                maxSizeMB: Long = 100 ) {

    private val maxSizeBytes = maxSizeMB * 1024 * 1024
    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    private fun getFile(url: String): File {
        val fileName = url.hashCode().toString()
        return File(cacheDir, fileName)
    }

    fun put(url: String, bitmap: Bitmap) {
        val file = getFile(url)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                file.setLastModified(System.currentTimeMillis())
            }
            checkSizeLimit()
        }

        catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun get(url: String): Bitmap? {
        val file = getFile(url)
        if (!file.exists()) return null

        if (System.currentTimeMillis() - file.lastModified() > expirationCache){
            file.delete()
            return null
        }

        return BitmapFactory.decodeFile(file.absolutePath)
    }

    /**
     * Checks the size of the cache directory and deletes files if the total size exceeds the limit.
     */
    private fun checkSizeLimit() {
        val files = cacheDir.listFiles() ?: return
        var totalSize = files.sumOf { it.length() }

        if (totalSize > maxSizeBytes) {
            files.sortedBy { it.lastModified() }.forEach { file ->
                if (totalSize <= maxSizeBytes) return
                totalSize -= file.length()
                file.delete()
            }
        }
    }

    /**
     * Clears the cache directory.
     */
    fun clear(){
        cacheDir.listFiles()?.forEach { it.delete() }
    }

}