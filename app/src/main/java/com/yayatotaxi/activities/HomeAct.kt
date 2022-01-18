package com.yayatotaxi.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.yayatotaxi.R
import com.yayatotaxi.carpool.activities.CarPoolHomeAct
import com.yayatotaxi.carpool.activities.PoolRequestAct
import com.yayatotaxi.databinding.ActivityHomeBinding
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.normalbook.activities.NormalBookHomeAct
import com.yayatotaxi.utils.AppConstant
import com.yayatotaxi.utils.ProjectUtil
import com.yayatotaxi.utils.SharedPref
import kotlinx.android.synthetic.main.activity_home.*

class HomeAct : AppCompatActivity(), OnMapReadyCallback {

    var mContext: Context = this@HomeAct
    lateinit var binding: ActivityHomeBinding
    lateinit var mapFragment: SupportMapFragment
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    override fun onResume() {
        super.onResume()
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        Glide.with(mContext).load(modelLogin.getResult()?.image)
            .error(R.drawable.user_ic)
            .placeholder(R.drawable.user_ic)
            .into(binding.childNavDrawer.userImg)
        binding.childNavDrawer.tvUsername.setText(modelLogin.getResult()?.user_name)
        binding.childNavDrawer.tvEmail.setText(modelLogin.getResult()?.email)
        Log.e("sfasdasdas","modelLogin.getResult()?.image = " + modelLogin.getResult()?.image)
    }

    private fun itit() {

        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)

        binding.childNavDrawer.signout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            ProjectUtil.logoutAppDialog(mContext)
        }

        binding.chlidDashboard.cvCarPool.setOnClickListener {
            startActivity(Intent(mContext, CarPoolHomeAct::class.java))
        }

        binding.chlidDashboard.cvBookNow.setOnClickListener {
            startActivity(Intent(mContext, NormalBookHomeAct::class.java)
                .putExtra("type", "rent")
            )
        }

        binding.chlidDashboard.cvREntalTaxi.setOnClickListener {
            startActivity(Intent(mContext, NormalBookHomeAct::class.java)
                    .putExtra("type", "rent")
            )
        }

        binding.chlidDashboard.navbar.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.childNavDrawer.tvHome.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.childNavDrawer.tvProfile.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, UpdateProfielAct::class.java))
        }

        binding.childNavDrawer.tvPoolRequest.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, PoolRequestAct::class.java))
        }

        binding.childNavDrawer.tvWallet.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, WalletAct::class.java))
        }

        binding.childNavDrawer.tvRideHistory.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(mContext, RideHistoryAct::class.java))
        }
    }

    override fun onMapReady(p0: GoogleMap) {}

}