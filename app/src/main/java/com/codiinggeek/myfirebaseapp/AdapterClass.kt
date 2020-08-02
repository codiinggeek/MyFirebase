package com.codiinggeek.myfirebaseapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AdapterClass(val context: Context, val mDataList: ArrayList<User>):
    RecyclerView.Adapter<AdapterClass.ViewHolderClass>(){
    class ViewHolderClass(view: View): RecyclerView.ViewHolder(view){
        val textView =view.findViewById<TextView>(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.single_row_recycler, parent,false)
        return ViewHolderClass(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val obj = mDataList[position]
        holder.textView.text = obj.name+" | "+obj.age

        holder.textView.setOnClickListener {
            val uid = obj.uid
            Log.d("MyTag", "onBindViewHolder: "+uid)
            val intent = Intent(context, MainActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("UID", uid)
            context.applicationContext.startActivity(intent)
        }

        holder.textView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val uid = obj.uid
                val task = FirebaseDatabase.getInstance().getReference("users")
                    .child(uid).setValue(null)
                /**
                 * code now will be redirected to
                 * fun onChildRemoved() in Main Activity
                 */
                task.addOnSuccessListener {
                    Toast.makeText(context, "User removed from database", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
    }
}