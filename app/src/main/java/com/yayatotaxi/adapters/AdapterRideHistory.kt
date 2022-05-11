package com.yayatotaxi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.activities.RideDetailsAct
import com.yayatotaxi.databinding.AdapterRideHistoryBinding
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.SharedPref

class AdapterRideHistory(
    val mContext: Context,
    var transList: ArrayList<ModelTaxiRequest.Result>?
) : RecyclerView.Adapter<AdapterRideHistory.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterRideHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_ride_history, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelTaxiRequest.Result = transList!!.get(position)
        holder.binding.tvDateTime.text = data.req_datetime

        holder.binding.tvFrom.text = data.picuplocation
        holder.binding.etDestination.text = data.dropofflocation

        if ("Finish" == data.status) {
            holder.binding.tvStatus.text = "Complete"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_spalsh))
        } else if ("Cancel" == data.status||"Cancel_by_driver" == data.status) {
            holder.binding.tvStatus.text = "Canceled"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        } else {
            holder.binding.tvStatus.text = data.status
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        }

        holder.binding.goDetail.setOnClickListener {
            mContext.startActivity(
                Intent(
                    mContext,
                    RideDetailsAct::class.java
                ).putExtra("id",data.id)
            )
        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterRideHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}