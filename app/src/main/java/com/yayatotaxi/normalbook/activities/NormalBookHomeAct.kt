package com.yayatotaxi.normalbook.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yayatotaxi.R
import kotlinx.android.synthetic.main.activity_normal_book_home.*

class NormalBookHomeAct : AppCompatActivity() {

    var mContext: Context = this@NormalBookHomeAct
    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_book_home)
        type = intent.getStringExtra("type")!!

        itit()
    }

    private fun itit() {
        btnBack.setOnClickListener { v -> finish() }
        btnNext.setOnClickListener { v ->
            val intent = Intent(mContext, RideOptionAct::class.java)
            if (type != null && type == "rent") {
                intent.putExtra("type", "rent")
            }
            startActivity(intent)
        }
    }

}