package com.yayatotaxi.listener

import com.yayatopartnerapp.models.ModelTaxiRequest


interface PoolCarListener {
    fun onClick(position:Int,model: ModelTaxiRequest.Result)
}