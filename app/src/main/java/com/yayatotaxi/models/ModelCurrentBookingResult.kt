package com.yayatotaxi.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ModelCurrentBookingResult : Serializable {

    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("user_id")
    @Expose
    private var userId: String? = null

    @SerializedName("driver_id")
    @Expose
    private var driverId: String? = null

    @SerializedName("otp")
    @Expose
    private var otp: String? = null

    @SerializedName("picuplocation")
    @Expose
    private var picuplocation: String? = null

    @SerializedName("service_type")
    @Expose
    private var serviceType: Any? = null

    @SerializedName("dropofflocation")
    @Expose
    private var dropofflocation: String? = null

    @SerializedName("payment_status")
    @Expose
    private var payment_status: String? = null

    @SerializedName("picuplat")
    @Expose
    private var picuplat: String? = null

    @SerializedName("pickuplon")
    @Expose
    private var pickuplon: String? = null

    @SerializedName("droplat")
    @Expose
    private var droplat: String? = null

    @SerializedName("droplon")
    @Expose
    private var droplon: String? = null

    @SerializedName("shareride_type")
    @Expose
    private var sharerideType: String? = null

    @SerializedName("booktype")
    @Expose
    private var booktype: String? = null

    @SerializedName("car_type_id")
    @Expose
    private var carTypeId: String? = null

    @SerializedName("car_seats")
    @Expose
    private var carSeats: String? = null

    @SerializedName("passenger")
    @Expose
    private var passenger: String? = null

    @SerializedName("booked_seats")
    @Expose
    private var bookedSeats: String? = null

    @SerializedName("req_datetime")
    @Expose
    private var reqDatetime: String? = null

    @SerializedName("timezone")
    @Expose
    private var timezone: String? = null

    @SerializedName("picklatertime")
    @Expose
    private var picklatertime: String? = null

    @SerializedName("picklaterdate")
    @Expose
    private var picklaterdate: String? = null

    @SerializedName("route_img")
    @Expose
    private var routeImg: String? = null

    @SerializedName("start_time")
    @Expose
    private var startTime: String? = null

    @SerializedName("end_time")
    @Expose
    private var endTime: Any? = null

    @SerializedName("wt_start_time")
    @Expose
    private var wtStartTime: Any? = null

    @SerializedName("wt_end_time")
    @Expose
    private var wtEndTime: Any? = null

    @SerializedName("accept_time")
    @Expose
    private var acceptTime: Any? = null

    @SerializedName("waiting_status")
    @Expose
    private var waitingStatus: String? = null

    @SerializedName("waiting_cnt")
    @Expose
    private var waitingCnt: Any? = null

    @SerializedName("reason_id")
    @Expose
    private var reasonId: Any? = null

    @SerializedName("card_id")
    @Expose
    private var cardId: String? = null

    @SerializedName("apply_code")
    @Expose
    private var applyCode: String? = null

    @SerializedName("payment_type")
    @Expose
    private var paymentType: String? = null

    @SerializedName("favorite_ride")
    @Expose
    private var favoriteRide: String? = null

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("user_review_rating")
    @Expose
    private var userRatingStatus: String? = null

    @SerializedName("tip_amount")
    @Expose
    private var tipAmount: String? = null

    @SerializedName("pay_status")
    @Expose
    private var payStatus: String? = null

    @SerializedName("hourdiff")
    @Expose
    private var hourdiff: Double? = null

    @SerializedName("driver_details")
    @Expose
    private var driver_details: List<ModelLogin.Result?>? = null

    @SerializedName("user_details")
    @Expose
    private var user_details: List<ModelLogin.Result?>? = null

    @SerializedName("type_name")
    @Expose
    private var typeName: String? = null

    @SerializedName("type_image")
    @Expose
    private var typeImage: String? = null

    @SerializedName("time_diff")
    @Expose
    private var timeDiff: String? = null

    @SerializedName("st_milisecond")
    @Expose
    private var stMilisecond: Int? = null

    @SerializedName("ed_milisecond")
    @Expose
    private var edMilisecond: Int? = null

    @SerializedName("milisecond")
    @Expose
    private var milisecond: Int? = null

    @SerializedName("estimate_time")
    @Expose
    private var estimateTime: String? = null

    @SerializedName("estimate_distance")
    @Expose
    private var estimateDistance: String? = null

    @SerializedName("distance")
    @Expose
    private var distance: String? = null

    @SerializedName("car_name")
    @Expose
    private var car_name: String? = null

    @SerializedName("service_name")
    @Expose
    private var service_name: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: String? = null

    fun getService_name(): String? {
        return service_name
    }

    fun setService_name(service_name: String?) {
        this.service_name = service_name
    }

    fun getOtp(): String? {
        return otp
    }

    fun setOtp(otp: String?) {
        this.otp = otp
    }

    fun getPayment_status(): String? {
        return payment_status
    }

    fun setPayment_status(payment_status: String?) {
        this.payment_status = payment_status
    }

    fun getAmount(): String? {
        return amount
    }

    fun setAmount(amount: String?) {
        this.amount = amount
    }

    fun getDistance(): String? {
        return distance
    }

    fun setDistance(distance: String?) {
        this.distance = distance
    }

    fun getCar_name(): String? {
        return car_name
    }

    fun setCar_name(car_name: String?) {
        this.car_name = car_name
    }

    fun getDriver_details(): List<ModelLogin.Result?>? {
        return driver_details
    }

    fun setDriver_details(driver_details: List<ModelLogin.Result?>?) {
        this.driver_details = driver_details
    }

    fun getUser_details(): List<ModelLogin.Result?>? {
        return user_details
    }

    fun setUser_details(user_details: List<ModelLogin.Result?>?) {
        this.user_details = user_details
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun getDriverId(): String? {
        return driverId
    }

    fun setDriverId(driverId: String?) {
        this.driverId = driverId
    }

    fun getPicuplocation(): String? {
        return picuplocation
    }

    fun setPicuplocation(picuplocation: String?) {
        this.picuplocation = picuplocation
    }

    fun getServiceType(): Any? {
        return serviceType
    }

    fun setServiceType(serviceType: Any?) {
        this.serviceType = serviceType
    }

    fun getDropofflocation(): String? {
        return dropofflocation
    }

    fun setDropofflocation(dropofflocation: String?) {
        this.dropofflocation = dropofflocation
    }

    fun getPicuplat(): String? {
        return picuplat
    }

    fun setPicuplat(picuplat: String?) {
        this.picuplat = picuplat
    }

    fun getPickuplon(): String? {
        return pickuplon
    }

    fun setPickuplon(pickuplon: String?) {
        this.pickuplon = pickuplon
    }

    fun getDroplat(): String? {
        return droplat
    }

    fun setDroplat(droplat: String?) {
        this.droplat = droplat
    }

    fun getDroplon(): String? {
        return droplon
    }

    fun setDroplon(droplon: String?) {
        this.droplon = droplon
    }

    fun getSharerideType(): String? {
        return sharerideType
    }

    fun setSharerideType(sharerideType: String?) {
        this.sharerideType = sharerideType
    }

    fun getBooktype(): String? {
        return booktype
    }

    fun setBooktype(booktype: String?) {
        this.booktype = booktype
    }

    fun getCarTypeId(): String? {
        return carTypeId
    }

    fun setCarTypeId(carTypeId: String?) {
        this.carTypeId = carTypeId
    }

    fun getCarSeats(): String? {
        return carSeats
    }

    fun setCarSeats(carSeats: String?) {
        this.carSeats = carSeats
    }

    fun getPassenger(): String? {
        return passenger
    }

    fun setPassenger(passenger: String?) {
        this.passenger = passenger
    }

    fun getBookedSeats(): String? {
        return bookedSeats
    }

    fun setBookedSeats(bookedSeats: String?) {
        this.bookedSeats = bookedSeats
    }

    fun getReqDatetime(): String? {
        return reqDatetime
    }

    fun setReqDatetime(reqDatetime: String?) {
        this.reqDatetime = reqDatetime
    }

    fun getTimezone(): String? {
        return timezone
    }

    fun setTimezone(timezone: String?) {
        this.timezone = timezone
    }

    fun getPicklatertime(): String? {
        return picklatertime
    }

    fun setPicklatertime(picklatertime: String?) {
        this.picklatertime = picklatertime
    }

    fun getPicklaterdate(): String? {
        return picklaterdate
    }

    fun setPicklaterdate(picklaterdate: String?) {
        this.picklaterdate = picklaterdate
    }

    fun getRouteImg(): String? {
        return routeImg
    }

    fun setRouteImg(routeImg: String?) {
        this.routeImg = routeImg
    }

    fun getStartTime(): String? {
        return startTime
    }

    fun setStartTime(startTime: String?) {
        this.startTime = startTime
    }

    fun getEndTime(): Any? {
        return endTime
    }

    fun setEndTime(endTime: Any?) {
        this.endTime = endTime
    }

    fun getWtStartTime(): Any? {
        return wtStartTime
    }

    fun setWtStartTime(wtStartTime: Any?) {
        this.wtStartTime = wtStartTime
    }

    fun getWtEndTime(): Any? {
        return wtEndTime
    }

    fun setWtEndTime(wtEndTime: Any?) {
        this.wtEndTime = wtEndTime
    }

    fun getAcceptTime(): Any? {
        return acceptTime
    }

    fun setAcceptTime(acceptTime: Any?) {
        this.acceptTime = acceptTime
    }

    fun getWaitingStatus(): String? {
        return waitingStatus
    }

    fun setWaitingStatus(waitingStatus: String?) {
        this.waitingStatus = waitingStatus
    }

    fun getWaitingCnt(): Any? {
        return waitingCnt
    }

    fun setWaitingCnt(waitingCnt: Any?) {
        this.waitingCnt = waitingCnt
    }

    fun getReasonId(): Any? {
        return reasonId
    }

    fun setReasonId(reasonId: Any?) {
        this.reasonId = reasonId
    }

    fun getCardId(): String? {
        return cardId
    }

    fun setCardId(cardId: String?) {
        this.cardId = cardId
    }

    fun getApplyCode(): String? {
        return applyCode
    }

    fun setApplyCode(applyCode: String?) {
        this.applyCode = applyCode
    }

    fun getPaymentType(): String? {
        return paymentType
    }

    fun setPaymentType(paymentType: String?) {
        this.paymentType = paymentType
    }

    fun getFavoriteRide(): String? {
        return favoriteRide
    }

    fun setFavoriteRide(favoriteRide: String?) {
        this.favoriteRide = favoriteRide
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getUserRatingStatus(): String? {
        return userRatingStatus
    }

    fun setUserRatingStatus(userRatingStatus: String?) {
        this.userRatingStatus = userRatingStatus
    }

    fun getTipAmount(): String? {
        return tipAmount
    }

    fun setTipAmount(tipAmount: String?) {
        this.tipAmount = tipAmount
    }

    fun getPayStatus(): String? {
        return payStatus
    }

    fun setPayStatus(payStatus: String?) {
        this.payStatus = payStatus
    }

    fun getHourdiff(): Double? {
        return hourdiff
    }

    fun setHourdiff(hourdiff: Double?) {
        this.hourdiff = hourdiff
    }

    fun getTypeName(): String? {
        return typeName
    }

    fun setTypeName(typeName: String?) {
        this.typeName = typeName
    }

    fun getTypeImage(): String? {
        return typeImage
    }

    fun setTypeImage(typeImage: String?) {
        this.typeImage = typeImage
    }

    fun getTimeDiff(): String? {
        return timeDiff
    }

    fun setTimeDiff(timeDiff: String?) {
        this.timeDiff = timeDiff
    }

    fun getStMilisecond(): Int? {
        return stMilisecond
    }

    fun setStMilisecond(stMilisecond: Int?) {
        this.stMilisecond = stMilisecond
    }

    fun getEdMilisecond(): Int? {
        return edMilisecond
    }

    fun setEdMilisecond(edMilisecond: Int?) {
        this.edMilisecond = edMilisecond
    }

    fun getMilisecond(): Int? {
        return milisecond
    }

    fun setMilisecond(milisecond: Int?) {
        this.milisecond = milisecond
    }

    fun getEstimateTime(): String? {
        return estimateTime
    }

    fun setEstimateTime(estimateTime: String?) {
        this.estimateTime = estimateTime
    }

    fun getEstimateDistance(): String? {
        return estimateDistance
    }

    fun setEstimateDistance(estimateDistance: String?) {
        this.estimateDistance = estimateDistance
    }


}