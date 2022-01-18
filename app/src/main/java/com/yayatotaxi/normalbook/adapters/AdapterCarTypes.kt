package com.yayatotaxi.normalbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatotaxi.R
import com.yayatotaxi.databinding.ItemRideBookBinding
import com.yayatotaxi.normalbook.adapters.AdapterCarTypes.MyRideHolder
import com.yayatotaxi.normalbook.models.ModelCar
import java.util.ArrayList

class AdapterCarTypes(val mContext: Context,val arrayList: ArrayList<ModelCar>) :
    RecyclerView.Adapter<AdapterCarTypes.MyRideHolder>() {

    interface onCarSelectListener {
        fun onCarSelected(car: ModelCar?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCarTypes.MyRideHolder {
        val binding: ItemRideBookBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_ride_book, parent, false
        )
        return MyRideHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterCarTypes.MyRideHolder, position: Int) {
       // holder.binding.tvCarName.setText("Hello")
    }

    override fun getItemCount(): Int {
        return 4
    }

    class MyRideHolder(var binding: ItemRideBookBinding) : RecyclerView.ViewHolder(
        binding.root
    )

}
