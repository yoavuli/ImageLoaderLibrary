package com.yoavuli.imageloader

/**
 *  A data class to hold all the request settings for image transformation.
 *  currently only height and width are supported.
 *  additional settings (Rounded corners,Saturation etc) can be added in the future.
 *
 */
data class RequestSettings(var height : Int? = null, var width : Int? = null){

    fun resize(width : Int, height : Int) : RequestSettings{
        this.width = width
        this.height = height
        return this
    }
}
