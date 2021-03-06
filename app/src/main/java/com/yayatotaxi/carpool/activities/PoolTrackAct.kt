package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.yayatotaxi.R
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.normalbook.activities.RideCancellationAct
import com.yayatotaxi.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_pool_track.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PoolTrackAct : AppCompatActivity(), OnMapReadyCallback {

    var mContext: Context = this@PoolTrackAct
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    var currentLocationMarker: Marker? = null
    var currentLocation: LatLng? = null
    lateinit var tracker: GPSTracker

    var requestId: String = ""
    var driverID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pool_track)
        itIt()
    }

    override fun onResume() {
        super.onResume()

        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)

        tracker = GPSTracker(mContext)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)

        get_booking_detailsApi()
    }

    override fun onMapReady(maps: GoogleMap) {
        googleMap = maps
        showMarkerCurrentLocation(currentLocation!!)
    }

    private fun showMarkerCurrentLocation(currentLocation: LatLng) {
        if (currentLocation != null) {
            if (currentLocationMarker == null) {
                if (googleMap != null) {
                    val height = 95
                    val width = 65
                    val b = BitmapFactory.decodeResource(resources, R.drawable.car_top)
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
                    val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)
                    currentLocationMarker = googleMap.addMarker(
                        MarkerOptions().position(currentLocation).title("My Location")
                            .icon(smallMarkerIcon)
                    )
                    animateCamera(currentLocation)
                }
            } else {
                Log.e("Animation", "Hello Marker Animation")
                animateCamera(currentLocation)
                MarkerAnimation.animateMarkerToGB(
                    currentLocationMarker!!,
                    currentLocation,
                    LatLngInterpolator.Companion.Spherical()
                )
            }
        }
    }

    private fun animateCamera(location: LatLng) {
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                getCameraPositionWithBearing(
                    location
                )
            )
        )
    }

    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(16f).build()
    }

    private fun itIt() {
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        ivCancelTrip.setOnClickListener { v ->
            startActivity(
                Intent(
                    mContext,
                    RideCancellationAct::class.java
                )
            )

        }
        btnBack.setOnClickListener { v -> finish() }
    }

//    private fun tripStatusDialog(text: String, status: String, data: ModelCurrentBooking?) {
//        val dialog = Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT)
//        dialog.setCancelable(false)
//        val dialogNewBinding: TripStatusDialogNewBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(mContext), R.layout.trip_status_dialog_new, null, false
//        )
//        dialogNewBinding.tvMessage.setText(text)
//        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
//        dialogNewBinding.tvOk.setOnClickListener { v ->
//            if ("End" == status) {
//                val j = Intent(mContext, EndCarPoolAct::class.java)
//                //                j.putExtra("data", data);
//                startActivity(j)
//                finish()
//            } else if ("Finish" == status) {
//                finishAffinity()
//                startActivity(Intent(mContext, HomeAct::class.java))
//            }
//            dialog.dismiss()
//        }
//        dialog.setContentView(dialogNewBinding.getRoot())
//        dialog.show()
//    }


    private fun get_booking_detailsApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_booking_details(
            intent.getStringExtra("id").toString(),//modelLogin.getResult()?.id.toString(),
            AppConstant.USER
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        val modelTaxiRequest: ModelTaxiRequest =
                            Gson().fromJson(responseString, ModelTaxiRequest::class.java)
                        requestId = modelTaxiRequest.getResult()?.get(0)?.id.toString()
                        if (modelTaxiRequest.getResult()?.get(0)?.user_details?.size!! > 0) {
                            driverID =
                                modelTaxiRequest.getResult()
                                    ?.get(0)?.user_details!![0].id.toString()

                            tvName.text =
                                modelTaxiRequest.getResult()
                                    ?.get(0)?.user_details!![0].user_name + "\n" +
                                        modelTaxiRequest.getResult()
                                            ?.get(0)?.user_details!![0].email
                            Glide.with(mContext)
                                .load(
                                    modelTaxiRequest.getResult()?.get(0)?.user_details!![0].image
                                )
                                .error(R.drawable.user_ic)
                                .placeholder(R.drawable.user_ic)
                                .into(driver_image)
                        }
                        tvCarNumber.text =
                            modelTaxiRequest.getResult()?.get(0)?.pool_details!![0]?.status
                        tvCarName.text = modelTaxiRequest.getResult()?.get(0)?.car_name
                        tvTime.text = modelTaxiRequest.getResult()?.get(0)?.estimate_time + " min"
                        tvPrice.text = "$" + modelTaxiRequest.getResult()?.get(0)?.amount
                        Glide.with(mContext)
                            .load("")//modelTaxiRequest.getResult()?.get(0)?.car_image)
                            .error(R.drawable.car)
                            .placeholder(R.drawable.car)
                            .into(ivCar)


                        if (googleMap != null) {

                            val latLngPickup: LatLng = LatLng(
                                modelTaxiRequest.getResult()?.get(0)?.picuplat?.toDouble()!!,
                                modelTaxiRequest.getResult()?.get(0)?.pickuplon?.toDouble()!!
                            )
                            currentLocationMarker = googleMap.addMarker(
                                MarkerOptions().position(latLngPickup)
                                    .title(modelTaxiRequest.getResult()?.get(0)?.picuplocation)
                            )

                            val latLngDrop: LatLng = LatLng(
                                modelTaxiRequest.getResult()?.get(0)?.droplat?.toDouble()!!,
                                modelTaxiRequest.getResult()?.get(0)?.droplon?.toDouble()!!
                            )
                            currentLocationMarker = googleMap.addMarker(
                                MarkerOptions().position(latLngDrop)
                                    .title(modelTaxiRequest.getResult()?.get(0)?.dropofflocation)
                            )
                        }

                    } else {
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
                Log.e("Exception", "Throwable = " + t.message)
            }
        })
    }

}