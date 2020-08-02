package com.codiinggeek.myfirebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var eAge : EditText
    lateinit var eName : EditText
    lateinit var txtOutputAge: TextView
    lateinit var txtOutputName: TextView
    lateinit var btnRunCode: Button
    lateinit var btnReadData: Button
    lateinit var mDataBase: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    lateinit var mListener: ValueEventListener
    lateinit var mChildListener: ChildEventListener

    lateinit var mRecyclerAdapter: AdapterClass
    lateinit var mRecyclerView: RecyclerView
    var mDataList = arrayListOf<User>()

    lateinit var layoutManager: RecyclerView.LayoutManager

    var uniqueKey = ""
    val TAG = "MyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eAge = findViewById(R.id.eAge)
        eName = findViewById(R.id.eName)
        txtOutputAge = findViewById(R.id.txtOutputAge)
        txtOutputName = findViewById(R.id.txtOutputName)

        btnRunCode = findViewById(R.id.btnRunCode)
        btnReadData = findViewById(R.id.btnReadData)

        mRecyclerView = findViewById(R.id.mRecyclerView)
        layoutManager = LinearLayoutManager(this)


        mDataBase = FirebaseDatabase.getInstance()
        mRef = mDataBase.getReference("users")

        /**
         * It is done so that no multiple copies of Child Listener is attached
         * and a single copy is generated for all processes.
         */
        /*
        On Destroy method is called to destroy the child listener attached!!
         */
        mChildListener = object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
               /* Log.d(TAG, "onChildChanged called")
                var user = snapshot.getValue(User::class.java)
                Log.d(TAG, "onChildChanged: Name: "+ user!!.name)
                Log.d(TAG, "onChildChanged: Age: "+user.age)
           */ }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var user = snapshot.getValue(User::class.java)
                Log.d(TAG, "onChildAdded: Name: "+ user!!.name)
                Log.d(TAG, "onChildAdded: Age: "+user.age)
                user.uid = snapshot.key.toString()
                mDataList.add(user)
                mRecyclerAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                /**
                 * Removing the user from the list that is removed instantly from the database
                 */
                for (i in 0 until mDataList.size){
                    if(mDataList[i].uid == snapshot.key){
                        Log.d(TAG, "onChildRemoved: "+snapshot.key)
                        mDataList.removeAt(i)
                        break
                    }
                }
                mRecyclerAdapter.notifyDataSetChanged()
            }

        }
        mRef.addChildEventListener(mChildListener)
       /* mListener = object :ValueEventListener{
            *//**
             * By this way only a single listener will be attached and would be
             * updated automatically.
             *//*
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(String::class.java)
                txtOutput.text = data
            }
        }
        mRef.child("user1").addValueEventListener(mListener)

       */
        /**
         * If child named users is not created yet then 1st the child will be created
         * then the data will be inserted.
         */

        btnRunCode.setOnClickListener {
            var age = eAge.text.toString()
            var name = eName.text.toString()

            /** This isn't an atomic method and different api calls will be generated,
             * if more than one api calls are made and one call falls other gets updated hence no
             * atomic.
             * each .setValue() function creates its own API calls
                s = mRef.push().key as String
                mRef.child(s).child("age").setValue(age)
                mRef.child(s).child("name").setValue(name)
           */
            /**
             * A way to create a child and insert a data through one API call only
             * it is done as a HashMap is created then data is inserted in it
             * and that HashMap is uploaded upon the database
             */
            /*s = mRef.push().key as String
            var insertValue: HashMap<String, String> = HashMap()
            insertValue.put("name", name)
            insertValue.put("age", age)
            mRef.child(s).setValue(insertValue)
            */
            /**
             * Alternative method to insert data inplace of hashmap,
             * create a data class and upload its child
             */
            uniqueKey = mRef.push().key as String
            var user = User(name = name,age = age)
            mRef.child(uniqueKey).setValue(user)
            /*---------------------------------------------------------------------------*/
            /**
             * UPDATION PERFORMED!!
             * This the atomic way for updation!!
             *
            var updatedValues : HashMap<String, Any> = HashMap()
            updatedValues.put("/-MBzwm4hy3wqqrLWAA-P/name", name)
            updatedValues.put("/-MBzwm4hy3wqqrLWAA-P/age", age)
            updatedValues.put("/-MBzxQyLV_sKByPtLRh6/name", name)
            updatedValues.put("/-MBzxQyLV_sKByPtLRh6/age", age)
            mRef.updateChildren(updatedValues)
            */
            /*---------------------------------------------------------------------------*/
            /**
             * Deletion performed!!*/
            /**1st Method
            mRef.child("-MBzxQyLV_sKByPtLRh6").child("name").setValue(null)
            2nd Method
            mRef.child("-MBzxQyLV_sKByPtLRh6").removeValue()
            .removeValue(): returns a task so functions like onSuccess and onFailure method
            could be formed*/
        }

        btnReadData.setOnClickListener {
            /**
             * ".addValueEventListener is used to update the UI every time database is updated,
             *   multiple listeners will be attached P.S.: NOT SUGGESTED"
             * ".addListenerForSingleValueEvent updates only when the command for updation is implemented,
             *   only single listener is attached "
             */
           /* mRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "On Failure: "+ error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    *//*var map = snapshot.value as HashMap<String, String>
                    txtOutputAge.text = map["age"].toString()
                    txtOutputName.text = map["name"].toString()
                    *//*

                    for(snap in snapshot.children ){
                        var data = snap.value as HashMap<*, *>
                        Log.d(TAG, "onDataChange: Name: "+data["name"])
                        Log.d(TAG, "onDataChange: Age: "+data["age"])
                        Log.d(TAG, "onDataChange: Key: "+snap.key)
                    }
                }

            })*/

            /**
             * Direct way to access child
             * But .addChildEventListener creates multiple listener every time it is called
             * so It is advised to create a single .addChildEventListener in the onCreate method()
             */
            /*mRef.addChildEventListener(object: ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildChanged called: ")
                    var data = snapshot.value as HashMap<*, *>
                    Log.d(TAG, "onChildChanged: Name: "+data["name"])
                    Log.d(TAG, "onChildChanged: Age: "+data["age"])
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var data = snapshot.value as HashMap<*, *>
                    Log.d(TAG, "onChildAdded: Name: "+data["name"])
                    Log.d(TAG, "onChildAdded: Age: "+ data["age"])
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

            })*/
        }

        mRecyclerView.layoutManager = layoutManager
        mRecyclerAdapter = AdapterClass(applicationContext, mDataList)
        mRecyclerView.adapter = mRecyclerAdapter

    }
    @Override
    override fun onDestroy() {
        super.onDestroy()
        mRef.removeEventListener(mChildListener)
    }
}