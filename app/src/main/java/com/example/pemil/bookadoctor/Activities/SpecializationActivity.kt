package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pemil.bookadoctor.Adapters.SpecializationDetailsAdapter
import com.example.pemil.bookadoctor.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.specializations_informations_activity.*

class SpecializationActivity : AppCompatActivity() {

    private lateinit var specializationAdapter: SpecializationDetailsAdapter

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("specialties")

    private var specializationList = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.specializations_informations_activity)

        retrieveDataFromDB()

        back_ib.setOnClickListener {
            val intent = Intent(applicationContext, AppointmentActivity::class.java)
            startActivity(intent)
        }

    }

    private fun retrieveDataFromDB() {
        specializationList = mutableListOf()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SpecializationActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (d in p0.children) {
                    val name = d.child("name").getValue(String::class.java)!!
                    val description = d.child("description").getValue(String::class.java)!!

                    specializationList.add(Pair(name, description))
                }

                specializationAdapter = SpecializationDetailsAdapter(this@SpecializationActivity, specializationList)
                specializationListView.adapter = specializationAdapter
            }

        })
    }
}