package com.yoavuli.imageloader

import android.content.Context
import android.graphics.Bitmap
import com.yoavuli.imageloader.cache.DiscCache
import com.yoavuli.imageloader.cache.MemoryCache
import com.yoavuli.imageloader.network.ImageDownloader
import com.yoavuli.imageloader.transitions.ImageResizeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.io.File

object ImageLoaderFactory {


    private val imageDownloader : ImageDownloader = ImageDownloader()

    private var memoryCache : MemoryCache? = null
    private var discCache : DiscCache? = null
    private val semaphore = Semaphore(5)
    private var isInit = false


    fun init(context: Context, expirationCache : Long = 4 * 60 * 60 * 1000){
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = MemoryCache(cacheSize)
        if (isInit) return
        discCache = DiscCache(File(context.cacheDir,"image_cache"),expirationCache ,100)
        isInit = true

    }


    fun load(url: String) : ImageRequest {
        return ImageRequest(url,this)
    }




    internal suspend fun execute(request: ImageRequest) : Bitmap? {
        return semaphore.withPermit {
            withContext(Dispatchers.IO) {

                memoryCache?.get(request.url)?.let {
                    return@withContext it
                }


                discCache?.get(request.url)?.let {
                    memoryCache?.put(request.url, it)
                    return@withContext it
                }

                val bitmap = downloadImage(request.url)
                bitmap?.let {
                    val finalBitmap =
                        ImageResizeHelper.resizeBitmapIfNeeded(bitmap, request.settings)
                    memoryCache?.put(request.url, finalBitmap)
                    discCache?.put(request.url, finalBitmap)
                    return@withContext finalBitmap
                }

                return@withContext null

            }
        }

    }


    private suspend fun downloadImage(url: String) : Bitmap? {
        return withContext(Dispatchers.IO) {
            val bitmap = imageDownloader.download(url)
            bitmap?.let {
                memoryCache?.put(url, it)
            }
        bitmap
        }
    }

    fun invalidateCache() {
        memoryCache?.clear()
        discCache?.clear()
    }
}