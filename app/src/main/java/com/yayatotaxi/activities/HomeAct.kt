package com.yayatotaxi.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.yayatotaxi.R
import com.yayatotaxi.carpool.activities.CarPoolHomeAct
import com.yayatotaxi.carpool.activities.PoolRequestAct
import com.yayatotaxi.carpool.activities.RentRequestAct
import com.yayatotaxi.databinding.ActivityHomeBinding
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.normalbook.activities.NormalBookHomeAct
import com.yayatotaxi.normalbook.activities.TrackAct
import com.yayatotaxi.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeAct : AppCompatActivity(), OnMapReadyCallback/*OnStreetViewPanoramaReadyCallback*/ {

    var mContext: Context = this@HomeAct
    lateinit var binding: ActivityHomeBinding
    lateinit var mapFragment: SupportMapFragment
    private var mStreetViewPanorama: StreetViewPanorama? = null

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    lateinit var tracker: GPSTracker
    var currentLocation: LatLng? = null
    lateinit var googleMap: GoogleMap
    var currentLocationMarker: Marker? = null
    var requestId:String=""

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref(mContext)

        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()


//        // Construct a FusedLocationProviderClient.
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//
//        currentLocation = LatLng(fusedLocationProviderClient.lastLocation.result.latitude, fusedLocationProviderClient.lastLocation.result.longitude)


    }

    override fun onResume() {
        super.onResume()
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        Glide.with(mContext).load(modelLogin.getResult()?.image)
            .error(R.drawable.user_ic)
            .placeholder(R.drawable.user_ic)
            .into(binding.childNavDrawer.userImg)
        binding.childNavDrawer.tvUsername.setText(modelLogin.getResult()?.user_name)
        binding.childNavDrawer.tvEmail.setText(modelLogin.getResult()?.email)
        Log.e("sfasdasdas", "modelLogin.getResult()?.image = " + modelLogin.getResult()?.image)
        Log.e("sfasdasdas", "modelLogin Gson = " + Gson().toJson(modelLogin))

        tracker = GPSTracker(mContext)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)

        getCurrentTaxiBookingApi()
        if(modelLogin.getResult()!!.social_status.equals("False")) showAlert(mContext,"Please Complete your profile")
    }


    fun showAlert(mContext: Context, text: String) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage(text).setCancelable(false).setPositiveButton(
            mContext.getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(mContext, UpdateProfielAct::class.java))
         }.create().show()
    }



    private fun itit() {

        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)

//        val streetViewFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportStreetViewPanoramaFragment?
//        streetViewFragment!!.getStreetViewPanoramaAsync(this)


        binding.chlidDashboard.btnAddChild.setOnClickListener {
            startActivity(Intent(mContext, SchoolRideAct::class.java))
        }

        binding.childNavDrawer.signout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            ProjectUtil.logoutAppDialog(mContext)
        }

        binding.chlidDashboard.cvCarPool.setOnClickListener {
            startActivity(Intent(mContext, CarPoolHomeAct::class.java)
                .putExtra("type", "classic")
            )
        }


        binding.chlidDashboard.cvBookNow.setOnClickListener {
            startActivity(
                Intent(mContext, CarPoolHomeAct::class.java)
                    .putExtra("type", "vtc")
            )
        }

        binding.chlidDashboard.cvREntalTaxi.setOnClickListener {
            startActivity(
                Intent(mContext, NormalBookHomeAct::class.java)
                    .putExtra("type", "rent")
            )
        }

        binding.chlidDashboard.navbar.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.childNavDrawer.tvHome.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.childNavDrawer.tvProfile.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, UpdateProfielAct::class.java))
        }

        binding.childNavDrawer.tvPoolRequest.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, PoolRequestAct::class.java))
        }

        binding.childNavDrawer.tvRentRequest.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, RentRequestAct::class.java))
        }

        binding.childNavDrawer.tvWallet.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, WalletAct::class.java))
        }

        binding.childNavDrawer.tvRideHistory.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, RideHistoryAct::class.java))
        }

        binding.chlidDashboard.goDetail.setOnClickListener {
            startActivity(Intent(mContext, TrackAct::class.java).putExtra("id", requestId))
        }
    }

    override fun onMapReady(maps: GoogleMap) {
        googleMap = maps

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled=true
        showMarkerCurrentLocation(currentLocation!!)
    }

    private fun showMarkerCurrentLocation(currentLocation: LatLng) {
        if (currentLocation != null) {
            if (currentLocationMarker == null) {
                if (googleMap != null) {
                    val height = 95
                    val width = 65
                    val b = BitmapFactory.decodeResource(resources, R.drawable.ic_setloc)
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
                    val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)
                    currentLocationMarker = googleMap.addMarker(
                        MarkerOptions().position(currentLocation).title("My Location")
                            .icon(smallMarkerIcon)
                    )
                    animateCamera(currentLocation)
                }
            } else {
                Log.e("sdfdsfdsfds", "Hello Marker Anuimation")
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


    private fun getCurrentTaxiBookingApi() {
//        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getCurrentTaxiBooking(
            modelLogin.getResult()?.id.toString(),
            AppConstant.USER
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        val modelTaxiRequest: ModelTaxiRequest =
                            Gson().fromJson(responseString, ModelTaxiRequest::class.java)
                        requestId = modelTaxiRequest.getResult()?.get(0)?.id.toString()
                        binding.chlidDashboard.tvDateTime.text =
                            modelTaxiRequest.getResult()?.get(0)?.req_datetime
                        binding.chlidDashboard.tvFrom.text =
                            modelTaxiRequest.getResult()?.get(0)?.picuplocation
                        binding.chlidDashboard.etDestination.text =
                            modelTaxiRequest.getResult()?.get(0)?.dropofflocation
                        binding.chlidDashboard.tvStatus.text =
                            modelTaxiRequest.getResult()?.get(0)?.status

                        if(modelTaxiRequest.getResult()?.get(0)?.driver_details?.size!! >0) {

                            binding.chlidDashboard.tvName.text =
                                modelTaxiRequest.getResult()?.get(0)?.driver_details!![0].user_name
                            binding.chlidDashboard.tvEmail.text =
                                modelTaxiRequest.getResult()?.get(0)?.driver_details!![0].email
                            Glide.with(mContext)
                                .load(
                                    modelTaxiRequest.getResult()?.get(0)?.driver_details!![0].image
                                )
                                .error(R.drawable.user_ic)
                                .placeholder(R.drawable.user_ic)
                                .into(binding.chlidDashboard.image)
                            binding.chlidDashboard.layoutDriver.visibility=View.VISIBLE

                        }else{
                            binding.chlidDashboard.layoutDriver.visibility=View.GONE

                        }

//                        if (modelTaxiRequest.getResult()?.get(0)?.status.equals("Pending")) {
//                            binding.chlidDashboard.btAccept.visibility = View.VISIBLE
//                            binding.chlidDashboard.btDecline.visibility = View.VISIBLE
//
//                        } else {
//                            binding.chlidDashboard.btAccept.visibility = View.GONE
//                            binding.chlidDashboard.btDecline.visibility = View.GONE
//                        }

                        binding.chlidDashboard.currentCardRequest.visibility = View.VISIBLE
                        binding.chlidDashboard.serviceLayout.visibility = View.GONE


                    } else {
//                        MyApplication.showAlert(mContext, getString(R.string.user_already_exits))
                        binding.chlidDashboard.currentCardRequest.visibility = View.GONE
                        binding.chlidDashboard.serviceLayout.visibility = View.VISIBLE


                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                ProjectUtil.pauseProgressDialog()
                Log.e("Exception", "Throwable = " + t.message)


            }

        })
    }

//    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama) {
//        mStreetViewPanorama = streetViewPanorama;
//        streetViewPanorama.setPosition(currentLocation!!, StreetViewSource.OUTDOOR)
//        streetViewPanorama.setStreetNamesEnabled(true);
//        streetViewPanorama.setPanningGesturesEnabled(true);
//        streetViewPanorama.setZoomGesturesEnabled(true);
//        streetViewPanorama.setUserNavigationEnabled(true);
//        streetViewPanorama.animateTo(
//            StreetViewPanoramaCamera.Builder().orientation(StreetViewPanoramaOrientation(20F, 20F))
//                .zoom(streetViewPanorama.panoramaCamera.zoom)
//                .build(), 2000
//        )
//
//        streetViewPanorama.setOnStreetViewPanoramaChangeListener(panoramaChangeListener)
//    }
//    private val panoramaChangeListener =
//        OnStreetViewPanoramaChangeListener { streetViewPanoramaLocation ->
//            Toast.makeText(
//                applicationContext,
//                "Lat: " + streetViewPanoramaLocation.position.latitude + " Lng: " + streetViewPanoramaLocation.position.longitude,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
}