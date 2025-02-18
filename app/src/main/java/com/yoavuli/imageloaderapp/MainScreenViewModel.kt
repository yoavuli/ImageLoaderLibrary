package com.yoavuli.imageloaderapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoavuli.imageloader.ImageLoaderFactory
import com.yoavuli.imageloaderapp.data.ImageModel
import com.yoavuli.imageloaderapp.data.network.ImageApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel : ViewModel() {

    private val _images = MutableStateFlow<List<ImageModel>>(emptyList())
    val images: StateFlow<List<ImageModel>> = _images


    fun loadImages() {
        viewModelScope.launch {
            try {
                val imageList = withContext(Dispatchers.IO){
                    ImageApiClient.apiService.getImages()
                }
                _images.value = imageList
            }
            catch (e: Exception){
                e.printStackTrace()
            }



        }
    }

    fun invalidateCache() {
       ImageLoaderFactory.invalidateCache()
    }


}
