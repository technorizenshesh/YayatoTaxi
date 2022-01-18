package com.yayatotaxi.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_ride_history.*

class RideHistoryAct : AppCompatActivity() {

    var mContext: Context = this@RideHistoryAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_history)
        itit()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

        cvhistoryDetail.setOnClickListener {
            startActivity(Intent(mContext, RideDetailsAct::class.java))
        }

    }

}