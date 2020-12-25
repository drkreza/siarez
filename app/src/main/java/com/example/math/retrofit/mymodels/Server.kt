package com.example.math.retrofit.mymodels

import android.content.Context
import android.util.Log
import com.example.math.G
import com.example.math.R
import com.example.math.retrofit.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object Server {
    var apiService = ApiUtils.getAPIService()
    fun getVersion(context: Context, callback: GetVersionCallback){
        apiService.getVersion(G.appVersion).enqueue(object : Callback<GetVersionRespons> {
            override fun onFailure(call: Call<GetVersionRespons>, t: Throwable) {
                Log.i("MyError","onFailure")
                callback.callback(false,null,context.getString(R.string.error))
            }

            override fun onResponse(call: Call<GetVersionRespons>, response: Response<GetVersionRespons>) {
                Log.i("MyError","onResponse "+response.code())
                Log.i("MyError","onResponse "+response.body()?.result?.link+response.body()?.result?.forceUpdate+"  "+response.body()?.result?.showUpdate)
                try {
                    if (response.body()?.status?.isSuccess == true)
                        callback.callback(true,response.body()?.result,"")
                    else
                        callback.callback(false,null,response.body()?.status?.message?:context.getString(R.string.error))
                }catch (e:Exception){
                    callback.callback(false,null,context.getString(R.string.error))
                }
            }
        } )
    }
}