package com.yayatotaxi.carpool.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatotaxi.R
import com.yayatotaxi.databinding.AdapterChildsBinding
import com.yayatotaxi.models.ModelChilds
import java.util.*

class AdapterChilds(val callBackMethod: CallBackMethod, val mContext: Context?, val childsList: ArrayList<ModelChilds.Result>?) :
    RecyclerView.Adapter<AdapterChilds.RideHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideHolder {
        val binding: AdapterChildsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.adapter_childs, parent, false)
        return RideHolder(binding)
    }

    interface CallBackMethod {
        fun getChildItem(data: ModelChilds.Result)
    }

    override fun onBindViewHolder(holder: RideHolder, position: Int) {
        var data: ModelChilds.Result = childsList!!.get(position)

        holder.binding.tvName.setText(data.name)
        holder.binding.tvAge.setText(data.age)
        holder.binding.tvPickup.setText(data.home_address)
        holder.binding.tvDrop.setText(data.school_address)

        holder.binding.tvTiming.setText("Timing\n" + data.pick_time + " - " + data.drop_time)

        holder.binding.ivEditChild.setOnClickListener {
            callBackMethod.getChildItem(data);
        }

    }

    override fun getItemCount(): Int {
        return childsList?.size ?: 0
    }

    class RideHolder(binding: AdapterChildsBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: AdapterChildsBinding

        init {
            this.binding = binding
        }

    }

}
