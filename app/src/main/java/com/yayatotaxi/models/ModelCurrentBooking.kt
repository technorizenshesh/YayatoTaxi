package com.yayatotaxi.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ModelCurrentBooking : Serializable {

    @SerializedName("estimated_time")
    @Expose
    private var estimatedTime: String? = null

    @SerializedName("estimated_dis")
    @Expose
    private var estimatedDis: Double? = null

    @SerializedName("fare")
    @Expose
    private var fare: Int? = null

    @SerializedName("time_taken")
    @Expose
    private var timeTaken: String? = null

    @SerializedName("wait_time")
    @Expose
    private var waitTime: String? = null

    @SerializedName("base_fare")
    @Expose
    private var baseFare: String? = null

    @SerializedName("trip_fare")
    @Expose
    private var tripFare: Double? = null

    @SerializedName("tax_amt")
    @Expose
    private var taxAmt: Int? = null

    @SerializedName("service_tax")
    @Expose
    private var serviceTax: String? = null

    @SerializedName("service_amt")
    @Expose
    private var serviceAmt: Int? = null

    @SerializedName("sub_total")
    @Expose
    private var subTotal: Int? = null

    @SerializedName("paid_amount")
    @Expose
    private var paidAmount: Int? = null

    @SerializedName("estimate_time")
    @Expose
    private var estimate_time: Int? = null

    @SerializedName("car_name")
    @Expose
    private var car_name: Int? = null

    @SerializedName("discount_percent")
    @Expose
    private var discountPercent: Int? = null

    @SerializedName("discount")
    @Expose
    private var discount: Int? = null

    @SerializedName("route_img")
    @Expose
    private var routeImg: String? = null

    @SerializedName("fav_status")
    @Expose
    private var favStatus: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<ModelCurrentBookingResult?>? = null

    @SerializedName("map")
    @Expose
    private var map: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("distance")
    @Expose
    private var distance: String? = null

    @SerializedName("diff_second")
    @Expose
    private var diffSecond: String? = null

    @SerializedName("booking_dropoff")
    @Expose
    private var bookingDropoff: List<Any?>? = null

    @SerializedName("status")
    @Expose
    private var status: Int? = null


    fun getEstimate_time(): Int? {
        return estimate_time
    }

    fun setEstimate_time(estimate_time: Int?) {
        this.estimate_time = estimate_time
    }

    fun getCar_name(): Int? {
        return car_name
    }

    fun setCar_name(car_name: Int?) {
        this.car_name = car_name
    }

    fun getEstimatedTime(): String? {
        return estimatedTime
    }

    fun setEstimatedTime(estimatedTime: String?) {
        this.estimatedTime = estimatedTime
    }

    fun getEstimatedDis(): Double? {
        return estimatedDis
    }

    fun setEstimatedDis(estimatedDis: Double?) {
        this.estimatedDis = estimatedDis
    }

    fun getFare(): Int? {
        return fare
    }

    fun setFare(fare: Int?) {
        this.fare = fare
    }

    fun getTimeTaken(): String? {
        return timeTaken
    }

    fun setTimeTaken(timeTaken: String?) {
        this.timeTaken = timeTaken
    }

    fun getWaitTime(): Any? {
        return waitTime
    }

    fun setWaitTime(waitTime: String?) {
        this.waitTime = waitTime
    }

    fun getBaseFare(): String? {
        return baseFare
    }

    fun setBaseFare(baseFare: String?) {
        this.baseFare = baseFare
    }

    fun getTripFare(): Double? {
        return tripFare
    }

    fun setTripFare(tripFare: Double?) {
        this.tripFare = tripFare
    }

    fun getTaxAmt(): Int? {
        return taxAmt
    }

    fun setTaxAmt(taxAmt: Int?) {
        this.taxAmt = taxAmt
    }

    fun getServiceTax(): String? {
        return serviceTax
    }

    fun setServiceTax(serviceTax: String?) {
        this.serviceTax = serviceTax
    }

    fun getServiceAmt(): Int? {
        return serviceAmt
    }

    fun setServiceAmt(serviceAmt: Int?) {
        this.serviceAmt = serviceAmt
    }

    fun getSubTotal(): Int? {
        return subTotal
    }

    fun setSubTotal(subTotal: Int?) {
        this.subTotal = subTotal
    }

    fun getPaidAmount(): Int? {
        return paidAmount
    }

    fun setPaidAmount(paidAmount: Int?) {
        this.paidAmount = paidAmount
    }

    fun getDiscountPercent(): Int? {
        return discountPercent
    }

    fun setDiscountPercent(discountPercent: Int?) {
        this.discountPercent = discountPercent
    }

    fun getDiscount(): Int? {
        return discount
    }

    fun setDiscount(discount: Int?) {
        this.discount = discount
    }

    fun getDistance(): String? {
        return distance
    }

    fun setDistance(distance: String?) {
        this.distance = distance
    }

    fun getRouteImg(): String? {
        return routeImg
    }

    fun setRouteImg(routeImg: String?) {
        this.routeImg = routeImg
    }

    fun getFavStatus(): String? {
        return favStatus
    }

    fun setFavStatus(favStatus: String?) {
        this.favStatus = favStatus
    }

    fun getResult(): List<ModelCurrentBookingResult?>? {
        return result
    }

    fun setResult(result: List<ModelCurrentBookingResult?>?) {
        this.result = result
    }

    fun getMap(): String? {
        return map
    }

    fun setMap(map: String?) {
        this.map = map
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getDiffSecond(): String? {
        return diffSecond
    }

    fun setDiffSecond(diffSecond: String?) {
        this.diffSecond = diffSecond
    }

    fun getBookingDropoff(): List<Any?>? {
        return bookingDropoff
    }

    fun setBookingDropoff(bookingDropoff: List<Any?>?) {
        this.bookingDropoff = bookingDropoff
    }

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

}