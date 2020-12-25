package com.example.math.retrofit.mymodels

interface GetVersionCallback {
    fun callback(isOk : Boolean,data : GetVersionResult?,message : String)
}