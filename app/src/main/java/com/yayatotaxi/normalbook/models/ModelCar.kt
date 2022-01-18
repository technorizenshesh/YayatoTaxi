package com.yayatotaxi.normalbook.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ModelCar : Serializable {

    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("car_name")
    @Expose
    private var carName: String? = null

    @SerializedName("car_image")
    @Expose
    private var carImage: String? = null

    @SerializedName("charge")
    @Expose
    private var charge: String? = null

    @SerializedName("no_of_seats")
    @Expose
    private var noOfSeats: String? = null

    @SerializedName("min_charge")
    @Expose
    private var minCharge: String? = null

    @SerializedName("per_km")
    @Expose
    private var perKm: String? = null

    @SerializedName("hold_charge")
    @Expose
    private var holdCharge: String? = null

    @SerializedName("ride_time_charge_permin")
    @Expose
    private var rideTimeChargePermin: String? = null

    @SerializedName("service_tax")
    @Expose
    private var serviceTax: String? = null

    @SerializedName("free_time_min")
    @Expose
    private var freeTimeMin: String? = null

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("distance")
    @Expose
    private var distance: String? = null

    @SerializedName("miles")
    @Expose
    private var miles: String? = null

    @SerializedName("perMin")
    @Expose
    private var perMin: Int? = null

    @SerializedName("total")
    @Expose
    private var total: String? = null

    @SerializedName("cab_find")
    @Expose
    private var cabFind: String? = null

    private var isSelected = false

    fun isSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getCarName(): String? {
        return carName
    }

    fun setCarName(carName: String?) {
        this.carName = carName
    }

    fun getCarImage(): String? {
        return carImage
    }

    fun setCarImage(carImage: String?) {
        this.carImage = carImage
    }

    fun getCharge(): String? {
        return charge
    }

    fun setCharge(charge: String?) {
        this.charge = charge
    }

    fun getNoOfSeats(): String? {
        return noOfSeats
    }

    fun setNoOfSeats(noOfSeats: String?) {
        this.noOfSeats = noOfSeats
    }

    fun getMinCharge(): String? {
        return minCharge
    }

    fun setMinCharge(minCharge: String?) {
        this.minCharge = minCharge
    }

    fun getPerKm(): String? {
        return perKm
    }

    fun setPerKm(perKm: String?) {
        this.perKm = perKm
    }

    fun getHoldCharge(): String? {
        return holdCharge
    }

    fun setHoldCharge(holdCharge: String?) {
        this.holdCharge = holdCharge
    }

    fun getRideTimeChargePermin(): String? {
        return rideTimeChargePermin
    }

    fun setRideTimeChargePermin(rideTimeChargePermin: String?) {
        this.rideTimeChargePermin = rideTimeChargePermin
    }

    fun getServiceTax(): String? {
        return serviceTax
    }

    fun setServiceTax(serviceTax: String?) {
        this.serviceTax = serviceTax
    }

    fun getFreeTimeMin(): String? {
        return "$freeTimeMin Min"
    }

    fun setFreeTimeMin(freeTimeMin: String?) {
        this.freeTimeMin = freeTimeMin
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getDistance(): String? {
        return distance
    }

    fun setDistance(distance: String?) {
        this.distance = distance
    }

    fun getMiles(): String? {
        return miles
    }

    fun setMiles(miles: String?) {
        this.miles = miles
    }

    fun getPerMin(): Int? {
        return perMin
    }

    fun setPerMin(perMin: Int?) {
        this.perMin = perMin
    }

    fun getTotal(): String? {
        return total
    }

    fun setTotal(total: String?) {
        this.total = total
    }

    fun getCabFind(): String? {
        return if (cabFind.equals("no_cab", ignoreCase = true)) "No Cab Found" else "$cabFind Min"
    }

    fun setCabFind(cabFind: String?) {
        this.cabFind = cabFind
    }

}