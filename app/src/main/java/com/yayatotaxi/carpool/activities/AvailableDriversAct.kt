package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_available_drivers.*
import net.frakbot.jumpingbeans.JumpingBeans

class AvailableDriversAct : AppCompatActivity() {

    var mContext: Context = this@AvailableDriversAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_drivers)
        itit()
    }

    private fun itit() {
        ivBack.setOnClickListener { finish() }

        btSend.setOnClickListener {
            btSend.setText("Wait For Driver To Accept The Request....")
            JumpingBeans.with(btSend).appendJumpingDots().build()
            Handler(Looper.getMainLooper()).postDelayed(
                { startActivity(Intent(mContext, PoolTrackAct::class.java)) },
                3000
            )
        }
    }

}