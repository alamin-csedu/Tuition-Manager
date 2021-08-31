package com.example.coder87.tuitionmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import com.example.coder87.tuitionmanager.R.id.ratingBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RatingPage : Activity() {
    var signer=""
    var sigenrId=""

    private lateinit var ratingBar: RatingBar
    private lateinit var ratingText:TextView
    private lateinit var bugReports:EditText
    private lateinit var feedback: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_page)
        getValues()
        bindWidgets()
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            var rat=ratingBar.rating.toString()
            if(rat=="1.0"){
                ratingText.text="Fine"
            }
            if(rat=="2.0"){
                ratingText.text="Good"
            }
            if(rat=="3.0"){
                ratingText.text="Awesome"
            }
            if(rat=="4.0"){
                ratingText.text="I like it"
            }
            if(rat=="5.0"){
                ratingText.text="I love it"
            }
        }
    }
    fun bindWidgets(){
        ratingBar=findViewById(R.id.ratingBar)
        ratingText=findViewById(R.id.rating_text)
        bugReports=findViewById(R.id.bug_reports)
        feedback=findViewById(R.id.feedback)
    }
    fun saveFeedback(view: View){
        val rating=ratingBar.rating.toString()
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("Feedback")
        var id=""
        id=firebaseDatabase.push().key.toString()
        var firebase=firebaseDatabase.child(id)
        firebase.child("Rating").setValue(rating)
        firebase.child("Phone").setValue(sigenrId)
        firebase.child("Account Type").setValue(signer)
        firebase.child("Bug Reports").setValue(bugReports.text.toString())
        firebase.child("Feedback").setValue(feedback.text.toString())
        val intent= Intent(this,HomePage::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()
    }
    fun getValues(){
        signer=intent.getStringExtra(type)
        sigenrId=intent.getStringExtra(emailPhone)
    }
    fun printToast(s:String){

        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
    override fun onBackPressed() {
        val intent=Intent(this,HomePage::class.java)
        intent.putExtra(emailPhone,sigenrId)
        intent.putExtra(type,signer)
        startActivity(intent)
        finish()
    }
}
