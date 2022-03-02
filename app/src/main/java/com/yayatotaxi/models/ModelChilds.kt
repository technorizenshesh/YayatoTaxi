package com.yayatotaxi.models

import java.io.Serializable
import java.util.ArrayList

class ModelChilds : Serializable{

    private var result: ArrayList<Result>? = null
    private var message: String? = null
    private var status: String? = null

    fun setResult(result: ArrayList<Result>?) {
        this.result = result
    }

    fun getResult(): ArrayList<Result>? {
        return result
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getMessage(): String? {
        return message
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getStatus(): String? {
        return status
    }
    class Result {
        var id: String? = null
        var user_id: String? = null
        var name: String? = null
        var age: String? = null
        var pick_time: String? = null
        var drop_time: String? = null
        var home_address: String? = null
        var home_lat: String? = null
        var home_lon: String? = null
        var school_address: String? = null
        var school_lat: String? = null
        var school_lon: String? = null
        var date_time: String? = null
    }

}