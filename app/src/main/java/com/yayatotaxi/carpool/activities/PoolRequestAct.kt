package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.yayatotaxi.R
import com.yayatotaxi.adapters.AdapterOfferPool
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_pool_request.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PoolRequestAct : AppCompatActivity() {

    var mContext: Context = this@PoolRequestAct

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pool_request)

        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

//        btTrackDriver.setOnClickListener {
//            startActivity(Intent(mContext, PoolTrackAct::class.java))
//        }
//
//        btSend.setOnClickListener{
//            finish()
//        }

    }

    override fun onResume() {
        super.onResume()
        getBookingHistoryPoolApi()
    }

    private fun getBookingHistoryPoolApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_new_to_old_request(
            modelLogin.getResult()?.id.toString()//, AppConstant.USER
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
                        val adapterOfferPool =
                            AdapterOfferPool(mContext, modelTaxiRequest.getResult())
                        recycle_view.adapter = adapterOfferPool

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