package com.example.coder87.tuitionmanager

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForgottenPasswordChange : Activity() {
    private lateinit var currentPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var retypePassword: EditText
    var sigenrId=""
    var signer=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotten_password_change)
        getValues()
    }
    fun bindWidgets(){
        currentPassword=findViewById(R.id.change_password_curr_fp)
        newPassword=findViewById(R.id.change_password_new_fp)
        retypePassword=findViewById(R.id.change_password_renew_fp)
    }
    private fun validateInput(): Boolean {
        var allInputsValid = true
        bindWidgets()
        if(newPassword.text.isEmpty() || newPassword.text.toString().length<6)
        {
            showError(newPassword,R.string.password_messege)
            allInputsValid=false
        }
        if(retypePassword.text.toString()!=newPassword.text.toString())
        {
            showError(retypePassword,R.string.confirm_password_messege)
            allInputsValid=false
        }
        return allInputsValid
    }

    private fun showError(field: EditText, messageRes: Int) {
        field.error = getString(messageRes)
    }
    fun changePasswordForgotten(view: View){

        if(validateInput()){
            val progress = ProgressDialog(this@ForgottenPasswordChange).apply {
                setTitle("Creating new password")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            if(progress!=null) progress.show()
            var firebaseDatabase= FirebaseDatabase.getInstance().getReference(signer).child(sigenrId)
            firebaseDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                        firebaseDatabase.child("Password").setValue(newPassword.text.toString())
                    if(progress!=null) progress.dismiss()
                    savePassword()

                }

            })
        }

    }
    fun printToast(s:String){

        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
    fun backToHomeOnCancelForgotten(view: View){
        val intent= Intent(this,SignIn::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()

    }
    fun savePassword(){
        printToast("Password changed successfully")
        val intent= Intent(this,SignIn::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()
    }
    fun getValues(){
        signer=intent.getStringExtra(type)
        sigenrId=intent.getStringExtra(emailPhone)
    }
    override fun onBackPressed() {
        val intent= Intent(this,SignIn::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()
    }
}
