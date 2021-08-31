package com.example.coder87.tuitionmanager

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyNewAccount : Activity() {

    private lateinit var phone: TextView
    private lateinit var verifiTxt: EditText
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    var verificationId = ""
    var user=""
    var usertype=""
    var pass=""
    var isVerifying=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_new_account)
        phone=findViewById(R.id.verification_phone_nc)
        verifiTxt=findViewById(R.id.verifi_text_nc)
        mAuth = FirebaseAuth.getInstance()
        getValues()
        phone.text="+88"+user
        if(isVerifying==false){
            verify()
            isVerifying=true
        }

    }
    private fun verificationCallbacks () {
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                callSignUp()
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                printToast(p0!!.message.toString())
                p0!!.printStackTrace();
                Log.e(ContentValues.TAG, p0!!.message.toString());
            }

            override fun onCodeSent(verfication: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verfication, p1)
                verificationId = verfication.toString()

            }

        }
    }

    private fun verify () {

        verificationCallbacks()
        var phnNo=phone.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phnNo,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        )
    }
    fun verifyToContinueNewAccount(view: View){
        val progress = ProgressDialog(this@VerifyNewAccount).apply {
            setTitle("Verifying Account")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        if(progress!=null) progress.show()
        authenticate()
        if(progress!=null) progress.dismiss()
    }
    private fun authenticate () {

        val verifiNo = verifiTxt.text.toString()
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verifiNo)

        signIn(credential)

    }
    private fun signIn (credential: PhoneAuthCredential) {
        callSignUp()
    }

    private fun callSignUp(){
        if(usertype=="Tutor"){
            val intent= Intent(this,SignUpTutor::class.java)
            intent.putExtra(emailPhone,user)
            intent.putExtra(type,usertype)
            intent.putExtra(password,pass)
            startActivity(intent)
            finish()
        }

        else if(usertype=="Student"){
            val intent= Intent(this,SignUpStudent::class.java)
            intent.putExtra(emailPhone,user)
            intent.putExtra(type,usertype)
            intent.putExtra(password,pass)
            startActivity(intent)
            finish()
        }

    }

    private fun toast (msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun sendVeficationCodeNewAccount(view: View){
        verify()
    }

    private fun getValues(){
        user=intent.getStringExtra(emailPhone)
        usertype=intent.getStringExtra(type)
        pass=intent.getStringExtra(password)

    }
    fun printToast(s:String){

        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
    override fun onBackPressed() {

        finish()
        val intent = Intent(this,SignerType::class.java)
        startActivity(intent)
    }
}
