package com.yayatotaxi.normalbook.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yayatotaxi.R
import com.yayatotaxi.activities.HomeAct
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import com.yayatotaxi.utils.retrofit.MyApplication
import kotlinx.android.synthetic.main.activity_ride_cancellation.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RideCancellationAct : AppCompatActivity() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    var mContext: Context = this@RideCancellationAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_cancellation)

        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    private fun itit() {
        cb2.setOnClickListener { v ->
            cb3.setChecked(false)
            cb4.setChecked(false)
            cb5.setChecked(false)
        }
        cb3.setOnClickListener { v ->
            cb2.setChecked(false)
            cb4.setChecked(false)
            cb5.setChecked(false)
        }
        cb4.setOnClickListener { v ->
            cb2.setChecked(false)
            cb3.setChecked(false)
            cb5.setChecked(false)
        }
        cb5.setOnClickListener { v ->
            cb2.setChecked(false)
            cb3.setChecked(false)
            cb4.setChecked(false)
        }
        ivBack.setOnClickListener { v -> finish() }
        btnSubmit.setOnClickListener { v ->
            if(cb5.isChecked||cb2.isChecked||cb3.isChecked||cb4.isChecked){
                acceptCancelOrderCallTaxiApi("Cancel_by_user")
            }
//            finish()
//            startActivity(Intent(mContext, HomeAct::class.java))
        }
    }

    private fun acceptCancelOrderCallTaxiApi(status: String) {
        val tz: TimeZone = TimeZone.getDefault()
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.acceptCancelOrderCallTaxi(
            status,
            intent.getStringExtra("requestId").toString(),
            intent.getStringExtra("driverID").toString(),//modelLogin.getResult()?.id.toString(),
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