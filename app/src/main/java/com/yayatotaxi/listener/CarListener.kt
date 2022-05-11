package com.yayatotaxi.listener

import com.yayatotaxi.models.ModelCarsType


interface CarListener {
    fun onClick(position:Int,model: ModelCarsType.Result)
}