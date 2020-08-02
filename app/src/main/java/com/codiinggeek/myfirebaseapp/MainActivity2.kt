package com.codiinggeek.myfirebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.*

class MainActivity2 : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textView = findViewById(R.id.textView)

        var uid = intent.getStringExtra("UID")
        Log.d("MyTag", "onCreate: "+uid)
        var dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid!!)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var us = snapshot.getValue(User::class.java)
                Log.d("MyTag", "onDataChange: Name "+ us!!.name)
                textView.text = us!!.name+" "+us.age
            }

        })
    }
}