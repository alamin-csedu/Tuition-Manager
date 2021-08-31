package com.example.coder87.tuitionmanager

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signertype.*

class ChangePassword : Activity() {
    private lateinit var currentPassword: EditText
    private lateinit var newPassword:EditText
    private lateinit var retypePassword: EditText
    var sigenrId=""
    var signer=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        getValues()
    }
    fun bindWidgets(){
        currentPassword=findViewById(R.id.change_password_curr)
        newPassword=findViewById(R.id.change_password_new)
        retypePassword=findViewById(R.id.change_password_renew)
    }
    private fun validateInput(): Boolean {
        var allInputsValid = true
        bindWidgets()
        if(currentPassword.text.isEmpty()||currentPassword.text.toString().length<6)
        {
            showError(currentPassword,R.string.password_messege)
            allInputsValid=false
        }
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
    fun changePassword(view: View){

        if(validateInput()){
            val progress = ProgressDialog(this@ChangePassword).apply {
                setTitle("Changing Password")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            if(progress!=null) progress.show()
            var firebaseDatabase=FirebaseDatabase.getInstance().getReference(signer).child(sigenrId)
            firebaseDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val pass=p0.child("Password").getValue().toString()
                    if(pass==currentPassword.text.toString()){
                        firebaseDatabase.child("Password").setValue(newPassword.text.toString())
                        if(progress!=null) progress.dismiss()
                        savePassword()
                    }
                    else{
                        if(progress!=null) progress.dismiss()
                        printToast("Please enter your current password")
                    }

                }

            })
        }

    }
    fun printToast(s:String){

        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
    fun backToHomeOnCancel(view: View){
        val intent=Intent(this,HomePage::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()

    }
    fun savePassword(){
        printToast("Password changed successfully")
        val intent=Intent(this,HomePage::class.java)
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
        val intent=Intent(this,HomePage::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()
    }

}
