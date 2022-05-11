package com.yayatotaxi.normalbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatotaxi.models.ModelTaxiRequest
import com.yayatotaxi.R
import com.yayatotaxi.databinding.AdapterCarOnRentBinding
import com.yayatotaxi.listener.CarOnRentListener
import com.yayatotaxi.models.ModelCarsRent
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.SharedPref

class AdapterCarOnRent(
    val mContext: Context/*,
    var transList: ArrayList<ModelTaxiRequest.Result>?*/
) : RecyclerView.Adapter<AdapterCarOnRent.TransViewHolder>() {
    private lateinit var transList: ArrayList<ModelCarsRent.Result>

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    lateinit var crOnRentListener: CarOnRentListener


    fun setListener(crOnRentListener: CarOnRentListener) {
        this.crOnRentListener = crOnRentListener
    }
    fun setList(transList: ArrayList<ModelCarsRent.Result>) {
        this.transList = transList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterCarOnRentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_car_on_rent, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelCarsRent.Result = transList!!.get(position)
        holder.binding.tvDateTime.text = data.date_time
        holder.binding.tvPickup.text = data.user_details!![0].user_name+"\n"+data.user_details!![0].email

//        holder.binding.tvFrom.text = data.picuplocation
//        holder.binding.etDestination.text = data.dropofflocation

        holder.binding.noofseats.text = data.start_date + " - " + data.end_date+"\n"+data.start_time + " - " + data.end_time

//        holder.binding.tvAmount.text=data.amount

        if(data.request_detailss!!.size >0) {
            if ("Finish" == data.request_detailss?.get(0)?.status) {
                holder.binding.tvStatus.text = "Complete"
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.green_spalsh
                    )
                )
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.GONE
            } else if ("Cancel" == data.request_detailss?.get(0)?.status || "Cancel_by_driver" == data.request_detailss?.get(
                    0
                )?.status || "Cancel_by_user" == data.request_detailss?.get(0)?.status
            ) {
                holder.binding.tvStatus.text = "Canceled"
                holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.GONE
            }


         else   if ("Pending" == data.request_detailss?.get(0)?.status) {
                holder.binding.tvStatus.text = "Pending"
                holder.binding.btTrackDriver.visibility = View.VISIBLE
                holder.binding.btSend.visibility = View.GONE
            }




            else if ("Accept" == data.request_detailss?.get(0)?.status) {
                holder.binding.tvStatus.text = data.request_detailss?.get(0)?.status
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.black
                    )
                )
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.VISIBLE
            } else {
                holder.binding.tvStatus.text = data.request_detailss?.get(0)?.status
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.black
                    )
                )
                holder.binding.btTrackDriver.visibility = View.GONE
                holder.binding.btSend.visibility = View.VISIBLE
            }
        }else{
            holder.binding.tvStatus.text = data.status
                holder.binding.btTrackDriver.visibility = View.VISIBLE
            holder.binding.btSend.visibility = View.GONE
        }
        holder.binding.btTrackDriver.setOnClickListener {
            crOnRentListener.onClick(data,"Pending",position)
        }
        holder.binding.btSend.setOnClickListener {
            crOnRentListener.onClick(data,"Cancel",position)

        }


//        holder.binding.goDetail.setOnClickListener {
//            if ("Finish" == data.status || "Cancel_by_driver" == data.status || "Cancel_by_user" == data.status) {
//                mContext.startActivity(
//                    Intent(
//                        mContext,
//                        RideDetailsAct::class.java
//                    ).putExtra("id", data.id)
//                )
//            } else {
//                mContext.startActivity(
//                    Intent(mContext, TrackPoolAct::class.java).putExtra(
//                        "id",
//                        data.id
//                    ).putExtra("status", data.status)
//                )
//
//            }
//        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterCarOnRentBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}