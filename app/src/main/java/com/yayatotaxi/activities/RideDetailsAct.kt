package com.yayatotaxi.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_ride_details.*

class RideDetailsAct : AppCompatActivity() {

    var mContext: Context = this@RideDetailsAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_details)
        itit()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

    }

}