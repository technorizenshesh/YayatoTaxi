package com.yayatotaxi.normalbook.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yayatotaxi.R
import com.yayatotaxi.databinding.DialogSearchDriverBinding
import com.yayatotaxi.normalbook.adapters.AdapterCarTypes
import kotlinx.android.synthetic.main.activity_ride_option.*

class RideOptionAct : AppCompatActivity() {

    var mContext: Context = this@RideOptionAct
    lateinit var dialogSerach: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_option)
        itit()
    }

    private fun itit() {
        recycle_view.setAdapter(AdapterCarTypes(mContext, ArrayList()))

        btnBook.setOnClickListener {
            driverSerachDialog()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
                startActivity(Intent(mContext, TrackAct::class.java))
            }, 4000)
        }
    }

    private fun driverSerachDialog() {
        dialogSerach = Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT)
        val dialogBinding: DialogSearchDriverBinding = DataBindingUtil
            .inflate(LayoutInflater.from(mContext), R.layout.dialog_search_driver, null, false)
        dialogSerach.setContentView(dialogBinding.getRoot())
        dialogBinding.ripple.startRippleAnimation()

//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                getCurrentBookingApi();
//            }
//        }, 0, 4000);

        dialogBinding.btnCancel.setOnClickListener { dialogSerach!!.dismiss() }
        dialogSerach!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogSerach!!.show()
    }

}