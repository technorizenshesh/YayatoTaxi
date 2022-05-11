package com.yayatotaxi.utils.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("change_password")
    fun changePass(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("update_lat_lon")
    fun updateLocation(@FieldMap params: Map<String, String>): Call<Map<String, String>>

    @FormUrlEncoded
    @POST("get_my_transaction")
    fun getTransactionApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("wallet_transfer")
    fun walletTransferApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_wallet")
    fun addWalletApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("forgot_password")
    fun forgotPass(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_booking_history")
    fun getTaxiHistory(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("update_online_status")
    fun updateOnOffApi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_profile")
    fun getProfileCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("login")
    fun loginApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_childs_request")
    fun addChilds(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("update_childs_request")
    fun updateRequest(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_childs_request")
    fun getChildsApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("available_car_driver")
    fun getAvailableDrivers(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_available_driver")
    fun getAvailableCarDriversHome(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_car_type_list")
    fun getCarTypeListApi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_dev_order")
    fun getDevOrdersApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("social_login")
    fun socialLogin(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_bank_account")
    fun addBankAccount(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    fun acceptCancelOrderCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_current_booking")
    fun getCurrentTaxiBooking(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("booking_request")
    fun bookingRequestApi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    fun acceptCancelOrderCallTaxi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("place_order")
    fun placeDevOrderApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("signup")
    fun signUpApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("available_car_driver")
    fun getAvailableCarCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_to_cart")
    fun updateOrderStatusApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_count_cart")
    fun getCartCountApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_current_booking")
    fun getCurrentBooking(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_lat_lon")
    fun getLatLonDriver(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @Multipart
    @POST("add_document")
    fun addDriverDocumentApiCall(
        @Part("user_id") user_id: RequestBody,
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("signup")
    fun signUpDriverCallApi(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("register_id") register_id: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("password") password: RequestBody,
        @Part("type") type: RequestBody,
        @Part("step") step: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("update_profile")
    fun updateDriverCallApi(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("type") type: RequestBody,
        @Part("id") id: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("add_vehicle")
    fun addDriverVehicle(
        @Part("user_id") user_id: RequestBody,
        @Part("car_type_id") car_type: RequestBody,
        @Part("brand") car_brand: RequestBody,
        @Part("car_model") car_model: RequestBody,
        @Part("car_number") carNumber: RequestBody,
        @Part("year_of_manufacture") year_of_manufacture: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>


    @POST("car_list")
    fun getCarList(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("new_booking_request")
    fun newBookingRequest(
        @Field("user_id") user_id: String,
        @Field("car_type_id") cartype_id: String,
        @Field("picuplat") picuplat: String,
        @Field("pickuplon") pickuplon: String,
        @Field("booktype") booktype: String,
        @Field("amount") amount: String,
        @Field("picuplocation") picuplocation: String,
        @Field("dropofflocation") dropofflocation: String,
        @Field("droplat") droplat: String,
        @Field("droplon") droplon: String,
        @Field("picklatertime") picklatertime: String,
        @Field("picklaterdate") picklaterdate: String,
        @Field("seats_avaliable_pool") seats_avaliable_pool: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_car_type_list")
    fun getEstimateAmount(
        @Field("picuplat") picuplat: String,
        @Field("pickuplon") pickuplon: String,
        @Field("droplat") droplat: String,
        @Field("droplon") droplon: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_booking_history")
    fun getHistoryTaxiBooking(
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_booking_details")
    fun get_booking_details(
        @Field("request_id") request_id: String,
        @Field("type") type: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_current_booking")
    fun getCurrentTaxiBooking(
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    fun acceptCancelOrderCallTaxi(
        @Field("status") status: String,
        @Field("request_id") request_id: String,
        @Field("driver_id") driver_id: String,
        @Field("timezone") timezone: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_avaliable_pool")
    fun get_avaliable_pool(
        @Field("date") date: String,
        @Field("number_of_seats") number_of_seats: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_booking_history_pool")
    fun getBookingHistoryPool(
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request_pool")
    fun acceptCancelOrderCallTaxiPool(
        @Field("status_pool") status: String,
        @Field("request_id") request_id: String,
        @Field("driver_id") driver_id: String,
        @Field("timezone") timezone: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_booking_request_new")
    fun addBookingRequestNew(
        @Field("user_id") user_id: String,
        @Field("booking_request_id") booking_request_id: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("noofseats") noofseats: String,
        @Field("pickuplocation") pickuplocation: String,
        @Field("pickuplocation_lat") pickuplocation_lat: String,
        @Field("pickuplocation_lon") pickuplocation_lon: String,
        @Field("droplocation") droplocation: String,
        @Field("droplocation_lat") droplocation_lat: String,
        @Field("droplocation_lon") droplocation_lon: String,
        @Field("status") status: String
    ): Call<ResponseBody>



    @FormUrlEncoded
    @POST("get_new_to_old_request")
    fun get_new_to_old_request(
        @Field("user_id") user_id: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_car_detial")
    fun get_car_detial(
        @Field("date") date: String,
        @Field("driver_id") driver_id: String,
        @Field("lat") lat: String,
        @Field("lon") lon: String

    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_car_detial_new")
    fun get_car_detial_new(
        @Field("driver_id") driver_id: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("update_car_request_status")
    fun update_car_request_status(
        @Field("id") id: String,
        @Field("status") status: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_car_request")
    fun add_car_request(
        @Field("status") status: String,
        @Field("car_detail_id") car_detail_id: String,
        @Field("driver_id") driver_id: String,
        @Field("lat") lat: String,
        @Field("lon") lon: String


    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_lat_lon")
    fun get_lat_lon(@Field("user_id") user_id: String): Call<ResponseBody>

}