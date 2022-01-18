package com.yayatotaxi.normalbook.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yayatotaxi.R
import com.yayatotaxi.activities.HomeAct
import kotlinx.android.synthetic.main.activity_ride_cancellation.*

class RideCancellationAct : AppCompatActivity() {

    var mContext: Context = this@RideCancellationAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_cancellation)
        itit()
    }

    private fun itit() {
        cb2.setOnClickListener { v ->
            cb3.setChecked(false)
            cb4.setChecked(false)
            cb5.setChecked(false)
        }
        cb3.setOnClickListener { v ->
            cb2.setChecked(false)
            cb4.setChecked(false)
            cb5.setChecked(false)
        }
        cb4.setOnClickListener { v ->
            cb2.setChecked(false)
            cb3.setChecked(false)
            cb5.setChecked(false)
        }
        cb5.setOnClickListener { v ->
            cb2.setChecked(false)
            cb3.setChecked(false)
            cb4.setChecked(false)
        }
        ivBack.setOnClickListener { v -> finish() }
        btnSubmit.setOnClickListener { v ->
            finish()
            startActivity(Intent(mContext, HomeAct::class.java))
        }
    }

}