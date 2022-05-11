package com.yayatotaxi.listener

import com.yayatotaxi.models.ModelCarsRent


interface CarOnRentListener {
    fun onClick(poolDetails: ModelCarsRent.Result, status: String, position: Int)
}