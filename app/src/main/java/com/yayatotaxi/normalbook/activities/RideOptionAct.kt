package com.yayatotaxi.normalbook.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.RadioGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.yayatopartnerapp.models.ModelCarsType
import com.yayatotaxi.R
import com.yayatotaxi.adapters.AdapterRideOption
import com.yayatotaxi.carpool.activities.AvailableDriversAct
import com.yayatotaxi.carpool.activities.PoolRequestAct
import com.yayatotaxi.databinding.DialogSearchDriverBinding
import com.yayatotaxi.listener.CarListener
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.normalbook.adapters.AdapterCarTypes
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.MyApplication
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_ride_option.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class RideOptionAct : AppCompatActivity(), CarListener {
    lateinit var modelLogin: ModelLogin
    lateinit var sharedPref: SharedPref

    var mContext: Context = this@RideOptionAct
    lateinit var dialogSerach: Dialog
    var taxiNamesList = ArrayList<ModelCarsType.Result>()
    var booking_type: String = ""
    var sourceAddress: String = ""
    var sourceAddressLat: String = ""
    var sourceAddressLon: String = ""

    var car_id: String = ""
    var amount: String = ""
    var position:Int=0

    var paymentType: String = ""


    var destinationAddress: String = ""
    var destinationAddressLat: String = ""
    var destinationAddressLon: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_option)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        sourceAddress = intent.getStringExtra("sourceAddress").toString()
        sourceAddressLat = intent.getStringExtra("sourceAddressLat").toString()
        sourceAddressLon = intent.getStringExtra("sourceAddressLon").toString()
        destinationAddress = intent.getStringExtra("destinationAddress").toString()
        destinationAddressLat = intent.getStringExtra("destinationAddressLat").toString()
        destinationAddressLon = intent.getStringExtra("destinationAddressLon").toString()


        if(intent.getStringExtra("type").equals("vtc")) {
            bookingType.visibility=View.GONE
        }else{
            bookingType.visibility=View.VISIBLE

        }

        bookingType.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                carpoolLayout.visibility = View.VISIBLE
                recycle_view.visibility = View.GONE
                paymentoption.visibility = View.GONE
                btnBook.text="Search Pool"
            } else {
                carpoolLayout.visibility = View.GONE
                recycle_view.visibility = View.VISIBLE
                paymentoption.visibility = View.VISIBLE
                btnBook.text="Book Ride"
            }
        }


        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbCash -> {
                    paymentType="cash"
                }
                R.id.rbCard -> {
                    paymentType="card"
                }
                R.id.rbWallet -> {
                    paymentType="wallet"
                }
            }
        })

        itit()


        etdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                etdate.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)

            }, year, month, day)

            dpd.show()
        }

        etpickupTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    etpickupTime.setText(String.format("%d : %d", hourOfDay, minute))
                }
            }, hour, minute, false)
            mTimePicker.show()
        }

        etnoOfSeats.setOnClickListener {view->
            val popupMenu: PopupMenu = PopupMenu(this,view)
            popupMenu.menu.add("1")
            popupMenu.menu.add("2")
            popupMenu.menu.add("3")
            popupMenu.menu.add("4")
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                etnoOfSeats.setText(item.title)
                true
            })
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
//        getCars()
        getEstimateAmountApi()
    }

    private fun itit() {
        recycle_view.adapter = AdapterCarTypes(mContext, ArrayList())

        btnBook.setOnClickListener {
            if(bookingType.isChecked){
                if (etdate.getText().isNotEmpty()&&etpickupTime.getText().isNotEmpty()&&etnoOfSeats.getText().isNotEmpty() ){
                    startActivity(Intent(mContext, AvailableDriversAct::class.java))
                }
            }else {
                if (amount.isNotEmpty() && car_id.isNotEmpty() && paymentType.isNotEmpty()) {
                    driverSerachDialog()
                    newBookingRequestApi()
//            Handler(Looper.getMainLooper()).postDelayed({
//                finish()
//                startActivity(Intent(mContext, TrackAct::class.java))
//            }, 4000)
                }
            }
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
        dialogSerach.setCancelable(false)
    }


    private fun getCars() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getCarList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            val modelCarsType: ModelCarsType =
                                Gson().fromJson(stringResponse, ModelCarsType::class.java)
                            for (item in modelCarsType.getResult()!!) {
                                taxiNamesList.add(item!!)
                            }
                            val adapterRideOption =
                                AdapterRideOption(mContext, taxiNamesList, this@RideOptionAct)
                            recycle_view.adapter = adapterRideOption

                        } else {
                            Toast.makeText(
                                mContext,
                                getString(R.string.data_not_found),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })


    }

    override fun onClick(position: Int,model: ModelCarsType.Result) {

        if (bookingType.isChecked) {
            booking_type = "CarPool"
        } else {
            booking_type = "now"
        }
        this.position =position
        car_id = model.id.toString()
        amount= model.total.toString()
//        getEstimateAmountApi()
    }


    private fun getEstimateAmountApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getEstimateAmount(
//            booking_type,
//            car_id,
            sourceAddressLat,
            sourceAddressLon,
            destinationAddressLat,
            destinationAddressLon
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("driversignup", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        val modelCarsType: ModelCarsType =
                            Gson().fromJson(responseString, ModelCarsType::class.java)
                        for (item in modelCarsType.getResult()!!) {
                            taxiNamesList.add(item!!)
                        }
                        val adapterRideOption =
                            AdapterRideOption(mContext, taxiNamesList, this@RideOptionAct)
                        recycle_view.adapter = adapterRideOption
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
                        MyApplication.showAlert(mContext, getString(R.string.user_already_exits))
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


    private fun newBookingRequestApi() {
//        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.newBookingRequest(
            modelLogin?.getResult()?.id!!,
            car_id,
            sourceAddressLat,
            sourceAddressLon,
            booking_type,
            amount,
            sourceAddress,
            destinationAddress,
            destinationAddressLat,
            destinationAddressLon,
            "",
            ""
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dialogSerach!!.dismiss()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("driversignup", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
//                        modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)
//                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
//                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
//                        startActivity(Intent(mContext, HomeAct::class.java))
//                        finish()
                        startActivity(Intent(mContext, TrackAct::class.java))
                    } else {

                        MyApplication.showAlert(mContext, "Driver not available")
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dialogSerach!!.dismiss()
                Log.e("Exception", "Throwable = " + t.message)
            }

        })
    }

}