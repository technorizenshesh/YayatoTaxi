package com.yayatotaxi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.SharedPref
import com.yayatotaxi.R
import com.yayatotaxi.activities.RideDetailsAct
import com.yayatotaxi.carpool.activities.PoolTrackAct
import com.yayatotaxi.databinding.AdapterOfferPoolBinding
import com.yayatotaxi.models.ModelLogin

class AdapterOfferPool(
    val mContext: Context,
    var transList: ArrayList<ModelTaxiRequest.Result>?
) : RecyclerView.Adapter<AdapterOfferPool.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterOfferPoolBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_offer_pool, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelTaxiRequest.Result = transList!!.get(position)
        holder.binding.tv.text = "Date Time\n" + data.picklaterdate + " " + data.picklatertime

        holder.binding.tvPickup.text = data.picuplocation
        holder.binding.tvDrop.text = data.dropofflocation


        holder.binding.tvSeats.text = data.seats_avaliable_pool + " seats\navailable"

        holder.binding.tvAmount.text = data.amount

        if ("Finish" == data.status) {
            holder.binding.tvStatus.text = "Complete"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_spalsh))

            holder.binding.btTrackDriver.visibility= View.VISIBLE
            holder.binding.btSend.visibility= View.GONE
            holder.binding.btTrackDriver.text="Detail"
        } else
        if ("Cancel" == data.status || "Cancel_by_driver" == data.status || "Cancel_by_user" == data.status) {
            holder.binding.tvStatus.text = "Canceled"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            holder.binding.btTrackDriver.visibility = View.VISIBLE
            holder.binding.btSend.visibility = View.GONE
            holder.binding.btTrackDriver.text = "Detail"

        } else {
            if (data.pool_details!![0]?.status.equals("Pending")||data.pool_details!![0]?.status.equals("Accept")) {
                holder.binding.tvStatus.text = data.pool_details!![0]?.status
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.green_spalsh
                    )
                )
                holder.binding.btTrackDriver.visibility = View.VISIBLE
                holder.binding.btSend.visibility = View.VISIBLE
                holder.binding.btTrackDriver.text = "Track Driver"
            }else  if (data.pool_details!![0]?.status.equals("Cancel")||data.pool_details!![0]?.status.equals("Cancel_by_user")) {
                holder.binding.tvStatus.text = "Canceled"
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.red
                    )
                )
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.GONE
                holder.binding.btTrackDriver.text = "Track Driver"
            }else{
                holder.binding.tvStatus.text = data.pool_details!![0]?.status
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.black
                    )
                )
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.GONE
                holder.binding.btTrackDriver.text = "Track Driver"
            }
        }

        holder.binding.btTrackDriver.setOnClickListener {
            if ("Finish" == data.status || "Cancel" == data.status || "Cancel_by_driver" == data.status || "Cancel_by_user" == data.status) {
                mContext.startActivity(
                    Intent(
                        mContext,
                        RideDetailsAct::class.java
                    ).putExtra("id", data.id)
                )
            } else {
                mContext.startActivity(
                    Intent(mContext, PoolTrackAct::class.java).putExtra(
                        "id",
                        data.id
                    ).putExtra("status", data.status)
                )

            }
        }

        holder.binding.btSend.setOnClickListener { }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterOfferPoolBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}