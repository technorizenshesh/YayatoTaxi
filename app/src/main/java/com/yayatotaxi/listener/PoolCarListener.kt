package com.yayatotaxi.listener

import com.yayatotaxi.models.ModelTaxiRequest


interface PoolCarListener {
    fun onClick(position:Int,model: ModelTaxiRequest.Result)
}