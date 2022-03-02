package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.yayatotaxi.R
import com.yayatotaxi.normalbook.activities.RideOptionAct
import com.yayatotaxi.utils.ProjectUtil
import kotlinx.android.synthetic.main.activity_car_pool_home.*

class CarPoolHomeAct : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_pool_home)
        itit()
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