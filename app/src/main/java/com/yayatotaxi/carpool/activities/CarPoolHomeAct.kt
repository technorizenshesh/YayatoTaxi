package com.yayatotaxi.carpool.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.yayatotaxi.R
import com.yayatotaxi.normalbook.activities.RideOptionAct
import com.yayatotaxi.utils.GPSTracker
import com.yayatotaxi.utils.LatLngInterpolator
import com.yayatotaxi.utils.MarkerAnimation
import com.yayatotaxi.utils.ProjectUtil
import kotlinx.android.synthetic.main.activity_car_pool_home.*

class CarPoolHomeAct : AppCompatActivity(), OnMapReadyCallback {

    var mContext: Context = this@CarPoolHomeAct
    private var AUTOCOMPLETE_REQUEST_CODE: Int = 101
    private var latLng: LatLng? = null
    var sourceAddress: String = ""
    var sourceAddressLat: String = ""
    var sourceAddressLon: String = ""

    var destinationAddress: String = ""
    var destinationAddressLat: String = ""
    var destinationAddressLon: String = ""
    var addressType: String = ""

    lateinit var mapFragment: SupportMapFragment
    var currentLocationMarker: Marker? = null

    var currentLocation: LatLng? = null
    lateinit var googleMap: GoogleMap
    lateinit var tracker: GPSTracker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_pool_home)

        tracker = GPSTracker(mContext)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)

        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)

        itit()
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
                    val b = BitmapFactory.decodeResource(resources, R.drawable.car_top)
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
                    val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)
//                    currentLocationMarker = googleMap.addMarker(
//                        MarkerOptions().position(currentLocation).title("My Location")
//                            .icon(smallMarkerIcon)
//                    )
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

    private fun itit() {
        btnBack.setOnClickListener { finish() }

        btnFindDriver.setOnClickListener {
            if (sourceAddress.isNotEmpty() && destinationAddress.isNotEmpty()) {
//                startActivity(Intent(mContext, AvailableDriversAct::class.java))
                startActivity(
                    Intent(mContext, RideOptionAct::class.java)
                        .putExtra("sourceAddress", sourceAddress)
                        .putExtra("sourceAddressLat", sourceAddressLat)
                        .putExtra("sourceAddressLon", sourceAddressLon)
                        .putExtra("destinationAddress", destinationAddress)
                        .putExtra("destinationAddressLat", destinationAddressLat)
                        .putExtra("destinationAddressLon", destinationAddressLon)
                        .putExtra("type",intent.getStringExtra("type"))
                )

            }
        }

        tvFrom.setOnClickListener {
            addressType = "source"
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        tv_Destination.setOnClickListener {
            addressType = "destination"
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext, place.latLng!!.latitude, place.latLng!!.longitude
                    )
                    if (addressType.equals("source")) {
                        sourceAddress = addresses.toString()
                        sourceAddressLat = place.latLng!!.latitude.toString()
                        sourceAddressLon = place.latLng!!.longitude.toString()

                        tvFrom.setText(addresses)

                    } else {
                        destinationAddress = addresses.toString()
                        destinationAddressLat = place.latLng!!.latitude.toString()
                        destinationAddressLon = place.latLng!!.longitude.toString()
                        tv_Destination.setText(addresses)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }


}