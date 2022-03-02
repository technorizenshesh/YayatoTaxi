package com.yayatotaxi.activities

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.yayatotaxi.R
import com.yayatotaxi.carpool.adapters.AdapterChilds
import com.yayatotaxi.databinding.AddChildsDialogBinding
import com.yayatotaxi.models.ModelChilds
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import com.yayatotaxi.utils.retrofit.MyApplication
import kotlinx.android.synthetic.main.activity_school_ride.*
import kotlinx.android.synthetic.main.add_childs_dialog.*
import kotlinx.android.synthetic.main.add_childs_dialog.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SchoolRideAct : AppCompatActivity(), AdapterChilds.CallBackMethod {

    private val AUTOCOMPLETE_REQUEST_HOME: Int = 101
    private val AUTOCOMPLETE_REQUEST_SCHOOL: Int = 102
    private val AUTOCOMPLETE_REQUEST_HOME_EDIT: Int = 103
    private val AUTOCOMPLETE_REQUEST_SCHOOL_EDIT: Int = 104
    var mContext: Context = this@SchoolRideAct
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    lateinit var dialogBinding: AddChildsDialogBinding
    lateinit var dialog: Dialog
    lateinit var editDialogBinding: AddChildsDialogBinding
    lateinit var editDialog: Dialog
    var homelatlng: LatLng? = null
    var schoollatlng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_ride)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    private fun itit() {

        ivBackk.setOnClickListener {
            finish()
        }

        getchildsApi()

        btAddChild.setOnClickListener {
            openAddChildDialog()
        }

        swipLayout.setOnRefreshListener {
            getchildsApi()
        }

    }

    private fun getchildsApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var paramHash = HashMap<String, String>()

        paramHash.put("user_id", modelLogin.getResult()!!.id!!)

        Log.e("asdfasdfasf", "paramHash = $paramHash")

        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.getChildsApiCall(paramHash)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                swipLayout.isRefreshing = false
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("getchildsApi", "getchildsApi = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        var modelChilds: ModelChilds = Gson().fromJson(responseString, ModelChilds::class.java)
                        var adapterChilds = AdapterChilds(this@SchoolRideAct, mContext, modelChilds.getResult()!!)
                        rvChilds.adapter = adapterChilds
                        // Toast.makeText(mContext,getString(R.string.success),Toast.LENGTH_LONG).show()
                    } else {
                        var adapterChilds = AdapterChilds(this@SchoolRideAct, mContext, null)
                        rvChilds.adapter = adapterChilds
                        MyApplication.showAlert(mContext, getString(R.string.please_add_child))
                    }
                } catch (e: Exception) {
                    // Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
                swipLayout.isRefreshing = false
            }

        })

    }

    fun editOpenAddChildDialog(data: ModelChilds.Result) {

        editDialog = Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT)
        editDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_childs_dialog, null, false)

        editDialog.setContentView(editDialogBinding.root)

        editDialog.text.setText("Edit Children")

        try {
            homelatlng = LatLng(data.home_lat!!.toDouble(), data.home_lon!!.toDouble())
        } catch (e: Exception) {
        }

        try {
            schoollatlng = LatLng(data.school_lat!!.toDouble(), data.school_lon!!.toDouble())
        } catch (e: Exception) {
        }

        editDialogBinding.etName.setText(data.name)
        editDialogBinding.etAge.setText(data.age)
        editDialogBinding.etDropTime.setText(data.drop_time)
        editDialogBinding.etPickTime.setText(data.pick_time)
        editDialogBinding.etHomeAddress.setText(data.home_address)
        editDialogBinding.etSchoolAddress.setText(data.school_address)

        editDialogBinding.etHomeAddress.setOnClickListener {
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_HOME_EDIT)
        }

        editDialogBinding.etSchoolAddress.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_SCHOOL_EDIT)
        }

        editDialogBinding.etPickTime.setOnClickListener {
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
                    editDialogBinding.etPickTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        editDialogBinding.etDropTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(
                this, { view, hourOfDay, minute ->
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mcurrentTime.set(Calendar.MINUTE, minute);
                    var simpleDateFormat = SimpleDateFormat("hh:mm a")
                    var finaldate = simpleDateFormat.format(mcurrentTime.time)
                    editDialogBinding.etDropTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        editDialogBinding.btnSubmit.setOnClickListener {

            if (TextUtils.isEmpty(editDialogBinding.etName.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_name))
            } else if (TextUtils.isEmpty(editDialogBinding.etAge.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_age))
            } else if (TextUtils.isEmpty(editDialogBinding.etPickTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_pickuptime))
            } else if (TextUtils.isEmpty(editDialogBinding.etDropTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_dropofftime))
            } else if (TextUtils.isEmpty(editDialogBinding.etHomeAddress.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_selecthome_address))
            } else if (TextUtils.isEmpty(editDialogBinding.etDropTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_schoohl_add))
            } else {
                editChildApiCall(data.id!!)
            }

        }

        editDialogBinding.ivBack.setOnClickListener {
            editDialog.dismiss()
        }

        editDialog.show()

    }

    private fun openAddChildDialog() {

        dialog = Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT)
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.add_childs_dialog, null, false
        )
        dialog.setContentView(dialogBinding.root)

        dialogBinding.etHomeAddress.setOnClickListener {
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_HOME)
        }

        dialogBinding.etSchoolAddress.setOnClickListener {
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_SCHOOL)
        }

        dialogBinding.etPickTime.setOnClickListener {
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
                    dialogBinding.etPickTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        dialogBinding.etDropTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(
                this, { view, hourOfDay, minute ->
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mcurrentTime.set(Calendar.MINUTE, minute);
                    var simpleDateFormat = SimpleDateFormat("hh:mm a")
                    var finaldate = simpleDateFormat.format(mcurrentTime.time)
                    dialogBinding.etDropTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        dialogBinding.btnSubmit.setOnClickListener {

            if (TextUtils.isEmpty(dialogBinding.etName.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_name))
            } else if (TextUtils.isEmpty(dialogBinding.etAge.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_age))
            } else if (TextUtils.isEmpty(dialogBinding.etPickTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_pickuptime))
            } else if (TextUtils.isEmpty(dialogBinding.etDropTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_dropofftime))
            } else if (TextUtils.isEmpty(dialogBinding.etHomeAddress.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_selecthome_address))
            } else if (TextUtils.isEmpty(dialogBinding.etDropTime.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_select_schoohl_add))
            } else {
                addChildApiCall()
            }

        }

        dialogBinding.ivBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun editChildApiCall(id: String) {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var paramHash = HashMap<String, String>()

        paramHash.put("child_id", id)
        paramHash.put("name", editDialogBinding.etName.text.toString().trim())
        paramHash.put("age", editDialogBinding.etAge.text.toString().trim())
        paramHash.put("pick_time", editDialogBinding.etPickTime.text.toString().trim())
        paramHash.put("drop_time", editDialogBinding.etDropTime.text.toString().trim())
        paramHash.put("home_address", editDialogBinding.etHomeAddress.text.toString().trim())
        paramHash.put("home_lat", homelatlng!!.latitude.toString())
        paramHash.put("home_lon", homelatlng!!.longitude.toString())
        paramHash.put("school_address", editDialogBinding.etSchoolAddress.text.toString().trim())
        paramHash.put("school_lat", schoollatlng!!.latitude.toString())
        paramHash.put("school_lon", schoollatlng!!.longitude.toString())

        Log.e("asdfasdfasf", "paramHash = $paramHash")
        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.updateRequest(paramHash)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("responseString", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        getchildsApi();
                        editDialog.dismiss()
                        Toast.makeText(mContext, getString(R.string.success), Toast.LENGTH_LONG)
                            .show()
                    } else {
                        MyApplication.showAlert(mContext, getString(R.string.something_went_wrong))
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })

    }

    private fun addChildApiCall() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var paramHash = HashMap<String, String>()

        paramHash.put("user_id", modelLogin.getResult()!!.id!!)
        paramHash.put("name", dialogBinding.etName.text.toString().trim())
        paramHash.put("age", dialogBinding.etAge.text.toString().trim())
        paramHash.put("pick_time", dialogBinding.etPickTime.text.toString().trim())
        paramHash.put("drop_time", dialogBinding.etDropTime.text.toString().trim())
        paramHash.put("home_address", dialogBinding.etHomeAddress.text.toString().trim())
        paramHash.put("home_lat", homelatlng!!.latitude.toString())
        paramHash.put("home_lon", homelatlng!!.longitude.toString())
        paramHash.put("school_address", dialogBinding.etSchoolAddress.text.toString().trim())
        paramHash.put("school_lat", schoollatlng!!.latitude.toString())
        paramHash.put("school_lon", schoollatlng!!.longitude.toString())

        Log.e("asdfasdfasf", "paramHash = $paramHash")
        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.addChilds(paramHash)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("responseString", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        getchildsApi();
                        dialog.dismiss()
                        Toast.makeText(mContext, getString(R.string.success), Toast.LENGTH_LONG)
                            .show()
                    } else {
                        MyApplication.showAlert(mContext, getString(R.string.something_went_wrong))
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_HOME) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                homelatlng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext,
                        place.latLng!!.latitude,
                        place.latLng!!.longitude
                    )
                    dialogBinding.etHomeAddress.setText(addresses)
                } catch (e: Exception) {
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_SCHOOL) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                schoollatlng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext,
                        place.latLng!!.latitude,
                        place.latLng!!.longitude
                    )
                    dialogBinding.etSchoolAddress.setText(addresses)
                } catch (e: Exception) {
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_HOME_EDIT) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                homelatlng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext,
                        place.latLng!!.latitude,
                        place.latLng!!.longitude
                    )
                    editDialogBinding.etHomeAddress.setText(addresses)
                } catch (e: Exception) {
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_SCHOOL_EDIT) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                schoollatlng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext,
                        place.latLng!!.latitude,
                        place.latLng!!.longitude
                    )
                    editDialogBinding.etSchoolAddress.setText(addresses)
                } catch (e: Exception) {
                }
            }
        }

    }

    override fun getChildItem(data: ModelChilds.Result) {
        editOpenAddChildDialog(data)
    }

}