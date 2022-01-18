package com.yayatotaxi.normalbook.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import com.yayatotaxi.activities.HomeAct
import kotlinx.android.synthetic.main.activity_end_user.*

class EndUserAct : AppCompatActivity() {

    var mContext: Context = this@EndUserAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_user)
        itit()
    }

    private fun itit() {
        btnPay.setOnClickListener { v ->
            finish()
            startActivity(Intent(mContext, HomeAct::class.java))
        }
    }
}