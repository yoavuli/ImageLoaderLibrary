package com.yoavuli.imageloader

data class RequestSettings(var height : Int? = null, var width : Int? = null){

    fun resize(width : Int, height : Int) : RequestSettings{
        this.width = width
        this.height = height
        return this
    }
}
