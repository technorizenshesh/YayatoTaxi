package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import com.yayatotaxi.activities.HomeAct
import kotlinx.android.synthetic.main.activity_end_car_pool.*

class EndCarPoolAct : AppCompatActivity() {

    var mContext: Context = this@EndCarPoolAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_car_pool)
        itit()
    }

    private fun itit() {

        btnPay.setOnClickListener {
            finish()
            startActivity(Intent(mContext, HomeAct::class.java))
        }

    }
}