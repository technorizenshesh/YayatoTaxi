package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_car_pool_home.*

class CarPoolHomeAct : AppCompatActivity() {

    var mContext: Context = this@CarPoolHomeAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_pool_home)
        itit()
    }

    private fun itit() {
        btnBack.setOnClickListener { finish() }

        btnFindDriver.setOnClickListener {
            startActivity(Intent(mContext, AvailableDriversAct::class.java))
        }
    }


}