package com.yayatotaxi.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatotaxi.R
import com.yayatotaxi.databinding.ActivitySetDateTimeBinding
import com.yayatotaxi.listener.CarOnRentListener
import com.yayatotaxi.models.ModelCarsRent
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.normalbook.adapters.AdapterCarOnRent
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.MyApplication
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SetDateTimeActivity : AppCompatActivity(),CarOnRentListener {
    lateinit var binding: ActivitySetDateTimeBinding
    var mContext: Context = this@SetDateTimeActivity
    private lateinit var adapterCarOnRent: AdapterCarOnRent
    private lateinit var transList: ArrayList<ModelVehicalList.Result>

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_date_time)



        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)




        adapterCarOnRent = AdapterCarOnRent(mContext)
        adapterCarOnRent.setList(ArrayList())
        adapterCarOnRent.setListener(this)
        binding.recyclerview.adapter = adapterCarOnRent



        binding.etStartDate.setOnClickListener {
            var cal = Calendar.getInstance()
            // create an OnDateSetListener
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
//                    datecurr=sdf.format(cal.getTime())
                    binding.etStartDate!!.setText(sdf.format(cal.getTime()))

                }
            }
            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }
        binding.etEndDate.setOnClickListener {
            var cal = Calendar.getInstance()
            // create an OnDateSetListener
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
//                    datecurr=sdf.format(cal.getTime())
                    binding.etEndDate!!.setText(sdf.format(cal.getTime()) )

                }
            }
            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        binding.etStartTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mcurrentTime.set(Calendar.MINUTE, minute);
                    var simpleDateFormat = SimpleDateFormat("hh:mm a")
                    var finaldate = simpleDateFormat.format(mcurrentTime.time)
                    binding.etStartTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        binding.etEndTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mcurrentTime.set(Calendar.MINUTE, minute);
                var simpleDateFormat = SimpleDateFormat("hh:mm a")
                var finaldate = simpleDateFormat.format(mcurrentTime.time)
                binding.etEndTime.setText(finaldate)
            }, hour, minute, false
            )
            mTimePicker.show()
        }

        binding.ivBack.setOnClickListener { finish() }


        binding.btnNext.setOnClickListener {
            if (TextUtils.isEmpty(binding.etStartDate.text.toString().trim())) {
                MyApplication.showAlert(mContext, "please select start date")
            } else if (TextUtils.isEmpty(binding.etEndDate.text.toString().trim())) {
                MyApplication.showAlert(mContext, "please select start date")
            } else if (TextUtils.isEmpty(binding.etStartTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, "please select start time")
            } else if (TextUtils.isEmpty(binding.etEndTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, "please select end time")
            } else {
//                val intent = Intent(mContext, SetDateTimeActivity::class.java)
//                    .putExtra("sourceAddress", intent.getStringExtra("sourceAddress"))
//                    .putExtra("sourceAddressLat",  intent.getStringExtra("sourceAddressLat"))
//                    .putExtra("sourceAddressLon",  intent.getStringExtra("sourceAddressLon"))
//                    .putExtra("destinationAddress",  intent.getStringExtra("destinationAddress"))
//                    .putExtra("destinationAddressLat",  intent.getStringExtra("destinationAddressLat"))
//                    .putExtra("destinationAddressLon",  intent.getStringExtra("destinationAddressLon"))
//                startActivity(intent)

                getBookingHistoryPoolApi()

            }
        }
    }



    override fun onResume() {
        super.onResume()
    }

    private fun getBookingHistoryPoolApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_car_detial(
            /*"2022-01-21"*/binding.etStartDate.text.toString(),
            modelLogin.getResult()?.id!!,intent.getStringExtra("sourceAddressLat").toString(),
            intent.getStringExtra("sourceAddressLon").toString()
        )
       Log.e("lat===",intent.getStringExtra("sourceAddressLat").toString())
        Log.e("lat===",intent.getStringExtra("sourceAddressLat").toString())


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
            modelLogin.getResult()?.id!!
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
                        Toast.makeText(mContext, "Request has benn sent", Toast.LENGTH_SHORT).show()
                        finish()
                        //getBookingHistoryPoolApi()

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