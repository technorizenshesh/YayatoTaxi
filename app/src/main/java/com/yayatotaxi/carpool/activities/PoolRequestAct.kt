package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_pool_request.*

class PoolRequestAct : AppCompatActivity() {

    var mContext: Context = this@PoolRequestAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pool_request)
        itit()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

        btTrackDriver.setOnClickListener {
            startActivity(Intent(mContext, PoolTrackAct::class.java))
        }

        btSend.setOnClickListener{
            finish()
        }

    }

}