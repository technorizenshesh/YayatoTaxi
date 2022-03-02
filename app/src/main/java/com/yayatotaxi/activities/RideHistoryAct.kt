package com.yayatotaxi.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.yayatopartnerapp.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.adapters.AdapterRideHistory
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_ride_history.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RideHistoryAct : AppCompatActivity() {

    var mContext: Context = this@RideHistoryAct
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_history)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
        getCurrentTaxiBookingApi()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

    }

    private fun getCurrentTaxiBookingApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getHistoryTaxiBooking(
            modelLogin.getResult()?.id.toString(),
            AppConstant.USER
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        val modelTaxiRequest: ModelTaxiRequest =
                            Gson().fromJson(responseString, ModelTaxiRequest::class.java)
                        val adapterRideHistory =
                            AdapterRideHistory(mContext, modelTaxiRequest.getResult())
                        recycleView.adapter = adapterRideHistory

                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
                Log.e("Exception", "Throwable = " + t.message)
            }

        })
    }

}