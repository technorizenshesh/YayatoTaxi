package com.yayatotaxi.carpool.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.yayatopartnerapp.models.ModelCarsType
import com.yayatopartnerapp.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.adapters.AdapterPoolOption
import com.yayatotaxi.adapters.AdapterRideOption
import com.yayatotaxi.listener.CarListener
import com.yayatotaxi.listener.PoolCarListener
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.MyApplication
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_available_drivers.*
import net.frakbot.jumpingbeans.JumpingBeans
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AvailableDriversAct : AppCompatActivity(), PoolCarListener {

    var mContext: Context = this@AvailableDriversAct

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_drivers)


        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    private fun itit() {
        ivBack.setOnClickListener { finish() }

//        btSend.setOnClickListener {
//            btSend.setText("Wait For Driver To Accept The Request....")
//            JumpingBeans.with(btSend).appendJumpingDots().build()
//            Handler(Looper.getMainLooper()).postDelayed(
//                { startActivity(Intent(mContext, PoolTrackAct::class.java)) },
//                3000
//            )
//        }
    }

    override fun onResume() {
        super.onResume()
        getEstimateAmountApi()
    }


    private fun getEstimateAmountApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_avaliable_pool(
            intent.getStringExtra("date")!!,
            intent.getStringExtra("noofseats")!!
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("driversignup", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        val modelTaxiRequest: ModelTaxiRequest =
                            Gson().fromJson(responseString, ModelTaxiRequest::class.java)
//                        for (item in modelCarsType.getResult()!!) {
//                            taxiNamesList.add(item!!)
//                        }
                        val adapterPoolOption =
                            AdapterPoolOption(mContext, modelTaxiRequest.getResult(), this@AvailableDriversAct)
                        recycle_view.adapter = adapterPoolOption
//                         val modelCarsType:ModelCarsType= Gson().fromJson(responseString, ModelCarsType::class.java)
//                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
//                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
//                        startActivity(Intent(mContext, HomeAct::class.java))
//                        finish()
//                        tv_ride_distance.text = "0min"
//                        tv_ride_price.text = modelCarsType.getResult()?.get(position)?.total+"$"

                    } else {
//                        tv_ride_distance.text = "0min"
//                        tv_ride_price.text = "0$"
                        MyApplication.showAlert(mContext, getString(R.string.data_not_found))
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
    override fun onClick(position: Int, model: ModelTaxiRequest.Result) {
        acceptCancelOrderCallTaxiApi("Pending", model?.id!!, modelLogin?.getResult()?.id!!,model?.status!!)
    }

    private fun acceptCancelOrderCallTaxiApi(statuspool: String,requestId: String,driverID: String,status: String) {
        val tz: TimeZone = TimeZone.getDefault()
        ProjectUtil.showProgressDialog(mContext, false, mContext.getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.acceptCancelOrderCallTaxiPool(
            statuspool,
            requestId,
            driverID,
            tz.id.toString()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    Log.e("acceptCancel", "responseString = $responseString")
                    val jsonObject = JSONObject(responseString)
                    if (jsonObject.getString("status") == "1") {

               mContext.startActivity(Intent(mContext, PoolTrackAct::class.java).putExtra("id", requestId).putExtra("status",status))

                        finish()
                    } else {
                        MyApplication.showAlert(mContext, jsonObject.getString("message"))
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