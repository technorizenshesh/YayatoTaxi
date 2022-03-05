package com.yayatotaxi.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yayatopartnerapp.models.ModelCarsType
import com.yayatopartnerapp.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.databinding.ItemPoolBookBinding
import com.yayatotaxi.databinding.ItemRideBookBinding
import com.yayatotaxi.listener.CarListener
import com.yayatotaxi.listener.PoolCarListener
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import com.yayatotaxi.utils.retrofit.MyApplication
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AdapterPoolOption(
    val mContext: Context,
    var transList: ArrayList<ModelTaxiRequest.Result>?, val listener: PoolCarListener
  ) : RecyclerView.Adapter<AdapterPoolOption.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
//    lateinit var modelLogin: ModelLogin


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: ItemPoolBookBinding = DataBindingUtil.inflate (
            LayoutInflater.from(mContext), R.layout.item_pool_book, parent, false
        )
        sharedPref = SharedPref(mContext)
//        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelTaxiRequest.Result = transList!!.get(position)
//        holder.binding.tvCarName.text = data.car_name

        holder.binding.tvPickup.text=data.picuplocation

        holder.binding.tvDrop.text=data.dropofflocation

        holder.binding.tvSeats.text=data.seats_avaliable_pool+" Seats\nAvailable"



//        holder.binding.tvTotal.text = data.total+"$"
//
        holder.binding.tv.text="Date Time\n"+data.picklaterdate+" "+data.picklatertime
//
//        if(data.isSelected!!){
//            holder.binding.layoutbg.setBackgroundColor(mContext.getColor(R.color.slectedcolor))
//        }else{
//            holder.binding.layoutbg.setBackgroundColor(mContext.getColor(R.color.white))
//        }
//
//
//        Glide.with(mContext).load(data.car_image)
//            .error(R.drawable.car)
//            .placeholder(R.drawable.car)
//            .into(holder.binding.ivCar)
//
//        holder.binding.cardcar.setOnClickListener {
//            listener.onClick(position,data)
//
//            for (i in 0 until transList!!.size){
//                transList?.get(i)?.isSelected=false
//            }
//            transList?.get(position)?.isSelected=true
//            notifyDataSetChanged()
//
//        }



//        if ("Credit" == data.transaction_type) {
//            holder.binding.tvDebitCredit.text = "Credit"
//            holder.binding.ivSendRecive.setImageResource(R.drawable.credit_icon)
//            holder.binding.tvDebitCredit.setTextColor(ContextCompat.getColor(mContext, R.color.green))
//        } else {
//            holder.binding.ivSendRecive.setImageResource(R.drawable.debit_icon)
//            holder.binding.tvDebitCredit.text = "Debit"
//            holder.binding.tvDebitCredit.setTextColor(ContextCompat.getColor(mContext, R.color.red))
//        }

        holder.binding.btSend.setOnClickListener {

            listener.onClick(position,data)
        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: ItemPoolBookBinding) :
        RecyclerView.ViewHolder(binding.root) {}




}