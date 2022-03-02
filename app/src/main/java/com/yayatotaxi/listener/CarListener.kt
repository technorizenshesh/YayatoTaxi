package com.yayatotaxi.listener

import com.yayatopartnerapp.models.ModelCarsType


interface CarListener {
    fun onClick(position:Int,model: ModelCarsType.Result)
}