package com.yayatotaxi.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import androidx.databinding.DataBindingUtil
import com.yayatotaxi.R
import com.yayatotaxi.databinding.AddMoneyDialogBinding
import com.yayatotaxi.databinding.SendMoneyDialogNewBinding
import kotlinx.android.synthetic.main.activity_wallet.*
import java.lang.Exception

class WalletAct : AppCompatActivity() {

    var walletTmpAmt: Double = 0.0
    var mContext: Context = this@WalletAct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        itit()
    }

    private fun itit() {

        ivBack.setOnClickListener { finish() }

        cvAddMoney.setOnClickListener { addMoneyDialog() }

        cvTransfer.setOnClickListener { tranferMOneyDialog() }

    }

    private fun addMoneyDialog() {
        val dialog = Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding: AddMoneyDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.add_money_dialog, null, false
        )

        dialog.setContentView(dialogBinding.getRoot())
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) dialog.dismiss()
            false
        }

        dialogBinding.etMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!(s == null || s == "")) {
                    try {
                        walletTmpAmt = s.toString().toDouble()
                    } catch (e: Exception) {
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        dialogBinding.ivMinus.setOnClickListener {
            if (!(dialogBinding.etMoney.getText().toString().trim().equals("") ||
                        dialogBinding.etMoney.getText().toString().trim().equals("0"))) {
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() - 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            }
        }

        dialogBinding.ivPlus.setOnClickListener {
            if (TextUtils.isEmpty(dialogBinding.etMoney.getText().toString().trim())) {
                dialogBinding.etMoney.setText("0")
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() + 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            } else {
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() + 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            }
        }

        dialogBinding.tv699.setOnClickListener {
            dialogBinding.etMoney.setText("699")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.tv799.setOnClickListener {
            dialogBinding.etMoney.setText("799")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.tv899.setOnClickListener {
            dialogBinding.etMoney.setText("899")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.btDone.setOnClickListener {  dialog.dismiss() }
        dialogBinding.tvCancel.setOnClickListener {  dialog.dismiss() }
        val window = dialog.window
        val wlp = window!!.attributes
        dialog.window!!.setBackgroundDrawableResource(R.color.translucent_black)
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.show()
    }

    private fun tranferMOneyDialog() {
        val dialog = Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding: SendMoneyDialogNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.send_money_dialog_new, null, false
        )
        dialog.setContentView(dialogBinding.getRoot())
        dialogBinding.btDone.setOnClickListener {  dialog.dismiss() }
        dialogBinding.tvCancel.setOnClickListener {  dialog.dismiss() }
        val window = dialog.window
        val wlp = window!!.attributes
        dialog.window!!.setBackgroundDrawableResource(R.color.translucent_black)
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.show()
    }

}