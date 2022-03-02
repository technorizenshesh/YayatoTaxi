package com.yayatotaxi.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yayatopartnerapp.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_ride_details.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RideDetailsAct : AppCompatActivity() {

    var mContext: Context = this@RideDetailsAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_details)
        itit()
        get_booking_detailsApi()

    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

    }

    private fun get_booking_detailsApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_booking_details(
            intent.getStringExtra("id").toString(),
            AppConstant.DRIVER
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


                        tvDateTime.text = modelTaxiRequest.getResult()!![0].req_datetime

                        tvFrom.text = modelTaxiRequest.getResult()!![0].picuplocation
                        etDestination.text = modelTaxiRequest.getResult()!![0].dropofflocation

                        if ("Finish" == modelTaxiRequest.getResult()!![0].status) {
                            tvStatus.text = "Complete"
                            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_spalsh))
                        } else if ("Cancel" == modelTaxiRequest.getResult()!![0].status||"Cancel_by_driver" == modelTaxiRequest.getResult()!![0].status) {
                            tvStatus.text = "Canceled"
                            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
                        } else {
                            tvStatus.text = modelTaxiRequest.getResult()!![0].status
                            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                        }

                        if(modelTaxiRequest.getResult()!![0].driver_details?.size!!>0) {
                            tvDriverName.text =
                                modelTaxiRequest.getResult()!![0].driver_details!![0].user_name
                            tvEmail.text =
                                modelTaxiRequest.getResult()!![0].driver_details!![0].email
                            tvMobile.text =
                                modelTaxiRequest.getResult()!![0].driver_details!![0].mobile

                            Glide.with(mContext)
                                .load(modelTaxiRequest.getResult()!![0].driver_details!![0].image)
                                .error(R.drawable.user_ic)
                                .placeholder(R.drawable.user_ic)
                                .into(ivDriverPic)

//                            tvDriverOrUser.visibility=View.VISIBLE
//                            GoToDriver.visibility=View.VISIBLE
                        }
//                        tvDriverOrUser.visibility=View.GONE
//                        GoToDriver.visibility=View.GONE


                        tvPayType.text=modelTaxiRequest.getResult()!![0].payment_status

                        tvAmount.text="$"+modelTaxiRequest.getResult()!![0].amount

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