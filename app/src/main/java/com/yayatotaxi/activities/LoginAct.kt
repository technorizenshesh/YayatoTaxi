package com.yayatotaxi.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.yayatotaxi.R
import com.yayatotaxi.models.ModelLogin
import com.yayatotaxi.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class LoginAct : AppCompatActivity() {

    var mContext: Context = this@LoginAct
    private lateinit var registerId: String
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    private lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var GOOGLE_SIGN_IN_REQUEST_CODE: Int = 1234
    private lateinit var callbackManager: CallbackManager

    lateinit var tracker: GPSTracker
    var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = SharedPref(mContext)

        FirebaseApp.initializeApp(mContext)
        callbackManager = CallbackManager.Factory.create()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                registerId = token
                Log.e("dsfsfsdfdsf", "token = " + registerId)
            }
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Build a GoogleSignInClient with the options specified by gso.

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        itit()



    }

    override fun onResume() {
        super.onResume()
        tracker = GPSTracker(mContext)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)
    }

    private fun itit() {

        btnSignin.setOnClickListener {
            if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.enter_email_text))
            } else if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_pass))
            } else {
                if (InternetConnection.checkConnection(mContext)) {
                    loginApiCall()
                } else {
                    MyApplication.showConnectionDialog(mContext)
                }
            }
        }

        cv_google.setOnClickListener {
            ProjectUtil.blinkAnimation(cv_google)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }

        cvFacebook.setOnClickListener {
            ProjectUtil.blinkAnimation(cvFacebook)
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        Log.e("kjsgdfkjdgsf", "onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.e("kjsgdfkjdgsf", "error = " + error.message)
                    }
                })
        }

        ivForgotPass.setOnClickListener {
            startActivity(Intent(mContext, ForgotPassAct::class.java))
        }

        btSignup.setOnClickListener {
            startActivity(Intent(mContext, SignUpAct::class.java))
        }

    }

    private fun loginApiCall() {

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var paramHash = HashMap<String, String>()

        paramHash.put("email", etEmail.text.toString().trim())
        paramHash.put("password", etPassword.text.toString().trim())
        paramHash.put("lat", currentLocation?.latitude.toString())
        paramHash.put("lon", currentLocation?.longitude.toString())
        paramHash.put("type", AppConstant.USER)
        paramHash.put("register_id", registerId)

        Log.e("asdfasdfasf", "paramHash = $paramHash")
        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.loginApiCall(paramHash)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("responseString", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {

                        modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)

                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)

                        startActivity(Intent(mContext, HomeAct::class.java))
                        finish()

                    } else {
                        MyApplication.showAlert(mContext, getString(R.string.invalid_credentials))
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

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val profilePhoto = "https://graph.facebook.com/" + token.userId + "/picture?height=500"
                    Log.e("kjsgdfkjdgsf", "profilePhoto = $profilePhoto")
                    Log.e("kjsgdfkjdgsf", "name = " + user!!.displayName)
                    Log.e("kjsgdfkjdgsf", "email = " + user.email)
                    Log.e("kjsgdfkjdgsf", "Userid = " + user.uid)
                    socialLoginCall (user.displayName.toString(), user.email, profilePhoto, user.uid
                    )
                } else {
                    Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun socialLoginCall(username: String, email: String?, image: String, socialId: String) {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        var api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var paramHash = HashMap<String, String>()

        paramHash.put("user_name", username)

        if (email == null) {
            paramHash.put("email", "")
        } else {
            paramHash.put("email", email)
        }

        paramHash.put("mobile", "")
        paramHash.put("lat", "")
        paramHash.put("lon", "")
        paramHash.put("image", "")
        paramHash.put("type", "USER")
        paramHash.put("image", image)
        paramHash.put("register_id", registerId)
        paramHash.put("social_id", socialId)

        Log.e("socialLogin", "socialLogin = $paramHash")
        val call = api.socialLogin(paramHash)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringRes = response.body()!!.string()
                    val jsonObject = JSONObject(stringRes)
                    if (jsonObject.getString("status") == "1") {
                        modelLogin = Gson().fromJson(stringRes, ModelLogin::class.java)
                        Log.e("jafhkjdf", "stringRes = $stringRes")
                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
                        startActivity(Intent(mContext, HomeAct::class.java))
                        finish()
                        // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(mContext, getString(R.string.unsuccessful), Toast.LENGTH_SHORT).show();
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mAuth.signOut()
        LoginManager.getInstance().logOut()
        signOut()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(
                this
            ) { task -> task.result }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
            val credential = GoogleAuthProvider.getCredential(idToken!!, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        if (user != null) {
                            Log.e("kjsgdfkjdgsf", "profilePhoto = " + user.photoUrl)
                            Log.e("kjsgdfkjdgsf", "name = " + user.displayName)
                            Log.e("kjsgdfkjdgsf", "email = " + user.email)
                            Log.e("kjsgdfkjdgsf", "Userid = " + user.uid)
                            socialLoginCall(
                                user.displayName!!,
                                user.email, user.photoUrl.toString(),
                                user.uid
                            )
                        }
                    } else {
                    }
                }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            /* Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

}
