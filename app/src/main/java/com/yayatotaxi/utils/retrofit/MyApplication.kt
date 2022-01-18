package com.yayatotaxi.utils.retrofit

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import com.google.android.libraries.places.api.Places
import com.yayatotaxi.R

class MyApplication : Application() {

    //    private onRefreshSchedule schedule;
    private val downTimer: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key))
    }

    fun get(): MyApplication? {
        return MyApplication()
    }

    fun showConnectionDialog(mContext: Context) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage(mContext.getString(R.string.please_check_internet))
            .setCancelable(false)
            .setPositiveButton(
                mContext.getString(R.string.ok)
            ) { dialog, which -> dialog.dismiss() }.create().show()
    }

}