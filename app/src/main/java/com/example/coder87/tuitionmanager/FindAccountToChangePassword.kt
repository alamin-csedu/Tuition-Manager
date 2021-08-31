package com.example.coder87.tuitionmanager

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_find_account_to_change_password.*

const val findPhone="phone number"
const val accType="Tutor"
class FindAccountToChangePassword : Activity() {
    private lateinit var phone:EditText
    private lateinit var pp:CircleImageView
    private lateinit var name:TextView
    private lateinit var rbTutor:RadioButton
    private lateinit var rbStudent:RadioButton
    private lateinit var fid:LinearLayout

    var phoneNumber=""
    var userType=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_account_to_change_password)

        bindWidgets()
    }
    private fun bindWidgets(){
        phone=findViewById(R.id.find_account_fp)
        pp=findViewById(R.id.pic_profile_forgotten_password)
        name=findViewById(R.id.user_name_forgotten_password)
        rbTutor=findViewById(R.id.radioButton_tutor_fp)
        rbStudent=findViewById(R.id.radioButton_student_fp)
        fid=findViewById(R.id.found_id_forgotten_password)
    }
     fun findId(view:View){

        if(phone.text.toString().length==11){
            val progress = ProgressDialog(this@FindAccountToChangePassword).apply {
                setTitle("Finding Your Account")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            if(progress!=null) progress.show()
            phoneNumber=phone.text.toString()
            if(rbTutor.isChecked){
                var found=false
                userType="Tutor"
                val firebaseDatabase=FirebaseDatabase.getInstance().getReference("Tutor")
                firebaseDatabase.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for(h in p0.children){
                            val uname=h.key.toString()
                            if(uname==phone.text.toString()){
                                found=true
                                if(progress!=null) progress.dismiss()
                                fid.visibility=View.VISIBLE
                                name.text=h.child("Name").getValue().toString()
                                var url=h.child("Profile Picture").getValue().toString()
                                GlideApp.with(this@FindAccountToChangePassword).load(url).into(pp)
                                break
                            }
                        }
                        if(progress!=null) progress.dismiss()
                        if(found==false)
                            printToast("Please enter that phone number which has account")

                    }

                })
            }
            else if(rbStudent.isChecked){
                userType="Student"
                var found=false
                val firebaseDatabase=FirebaseDatabase.getInstance().getReference("Student")
                firebaseDatabase.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for(h in p0.children){
                            val uname=h.key.toString()
                            if(uname==phone.text.toString()){
                                found=true
                                if(progress!=null) progress.dismiss()
                                fid.visibility=View.VISIBLE
                                name.text=h.child("Name").getValue().toString()
                                var url=h.child("Profile Picture").getValue().toString()
                                GlideApp.with(this@FindAccountToChangePassword).load(url).into(pp)
                                break
                            }
                        }
                        if(progress!=null) progress.dismiss()
                        if(found==false)
                            printToast("Please enter that phone number which has account")
                    }

                })
            }
            else{
                printToast("Please select an account type")
            }

        }
        else{
            printToast("Please enter your phone number")
        }
    }

     fun callVerifycode(view: View){

        val intent= Intent(this,VerifyForgottenPassword::class.java)
        intent.putExtra(findPhone,phoneNumber)
         intent.putExtra(accType,userType)
        startActivity(intent)
         finish()
    }
    fun printToast(s:String){

        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
    override fun onBackPressed() {

        finish()
        val intent = Intent(this,SignIn::class.java)
        startActivity(intent)
    }
}
