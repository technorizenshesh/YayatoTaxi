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
import com.yayatotaxi.listener.CarOnRentListener
import com.yayatotaxi.models.ModelCarsRent
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.normalbook.adapters.AdapterCarOnRent
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
import java.util.ArrayList

class RentRequestAct : AppCompatActivity(), CarOnRentListener {

    var mContext: Context = this@RentRequestAct
    private lateinit var adapterCarOnRent: AdapterCarOnRent

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pool_request)


        adapterCarOnRent = AdapterCarOnRent(mContext)
        adapterCarOnRent.setList(ArrayList())
        adapterCarOnRent.setListener(this)
        recycle_view.adapter = adapterCarOnRent


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
        val call: Call<ResponseBody> = api.get_car_detial_new(
//            "2022-03-08",
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
                        val modelCarsRent: ModelCarsRent =
                            Gson().fromJson(responseString, ModelCarsRent::class.java)
                        adapterCarOnRent.setList(modelCarsRent.getResult()!!)
                        adapterCarOnRent.notifyDataSetChanged()

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

    override fun onClick(poolDetails: ModelCarsRent.Result, status: String, position: Int) {
        if(status.equals("Cancel")){
            update_car_request_statusApi(poolDetails?.id!!,status)

        }else{
            get_car_on_rentApi(poolDetails.id.toString(),status)
        }
    }

    private fun get_car_on_rentApi(id: String, status: String) {

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.add_car_request(
            status,id,
            modelLogin.getResult()?.id!!,"",""
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
//                        val modelVehicalList: ModelVehicalList =
//                            Gson().fromJson(responseString, ModelVehicalList::class.java)
//                        adapterCarOnRent.setList(modelVehicalList.getResult()!!)
//                        adapterCarOnRent.notifyDataSetChanged()

                        getBookingHistoryPoolApi()

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
    private fun update_car_request_statusApi(id:String,status:String) {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.update_car_request_status(
            id,
            status
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
//                        val modelVehicalList: ModelVehicalList =
//                            Gson().fromJson(responseString, ModelVehicalList::class.java)
//                        val adapterAllVehicle =
//                            AdapterAllVehicle(mContext, modelVehicalList.getResult())
//                        binding.chlidDashboard.recyclerView.adapter = adapterAllVehicle
                        getBookingHistoryPoolApi();

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