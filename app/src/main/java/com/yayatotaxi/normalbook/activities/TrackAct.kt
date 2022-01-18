package com.yayatotaxi.normalbook.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.yayatotaxi.R
import com.yayatotaxi.activities.HomeAct
import com.yayatotaxi.databinding.TripStatusDialogNewBinding
import com.yayatotaxi.models.ModelCurrentBooking
import kotlinx.android.synthetic.main.activity_track.*

class TrackAct : AppCompatActivity() {

    var mContext: Context = this@TrackAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        itit()
    }

    private fun itit() {
        ivCancelTrip.setOnClickListener { v ->
            startActivity(Intent(mContext, RideCancellationAct::class.java))
        }
        Handler(Looper.getMainLooper()).postDelayed({ tripStatusDialog("Your Trip is Ended", "End", null) }, 4000)
    }

    private fun tripStatusDialog(text: String, status: String, data: ModelCurrentBooking?) {
        val dialog = Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(false)
        val dialogNewBinding: TripStatusDialogNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.trip_status_dialog_new, null, false
        )
        dialogNewBinding.tvMessage.setText(text)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogNewBinding.tvOk.setOnClickListener { v ->
            if ("End" == status) {
                val j = Intent(mContext, EndUserAct::class.java)
                //                j.putExtra("data", data);
                startActivity(j)
                finish()
            } else if ("Finish" == status) {
                finishAffinity()
                startActivity(Intent(mContext, HomeAct::class.java))
            }
            dialog.dismiss()
        }
        dialog.setContentView(dialogNewBinding.getRoot())
        dialog.show()
    }

}