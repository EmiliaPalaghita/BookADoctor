package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.pemil.bookadoctor.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.appointment_activity.*


class AppointmentActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    var specialtiesReference = database.getReference("specialties")
    var doctorsReference = database.getReference("doctors")

    var specialtiesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appointment_activity)

        edit_cancel_tv.visibility = View.GONE

        back_ib.setOnClickListener { goBackWithoutSaving() }

        save_button.setOnClickListener { saveAppointment() }

        spec_info_ib.setOnClickListener { openSpecializationDetails() }

        retrieveDataFromDB()

    }

    private fun openSpecializationDetails() {
        val newIntent = Intent(this@AppointmentActivity, SpecializationActivity::class.java)
        startActivity(newIntent)
    }

    private fun retrieveDataFromDB() {
        updateSpecialization()

    }

    private fun updateSpecialization() {
        if (doctor_name_spinner.selectedItem.toString() != null &&
                health_center_spinner.selectedItem.toString() != null) {
            specialtiesReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@AppointmentActivity, "Failed to load data", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (data in p0.children) {
                        val specialtyName = data.child("name").getValue(String::class.java)
                        specialtiesList.add(specialtyName!!)
                    }

                    specialization_spinner.adapter = ArrayAdapter<String>(this@AppointmentActivity,
                            android.R.layout.simple_spinner_item, specialtiesList).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
                }
            })
        } else {
            // TODO - implement get from DB with filter
        }
    }

    private fun saveAppointment() {

    }

    private fun goBackWithoutSaving() {
        val alertDialog = AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Are you sure you want to discard changes?")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ ->
                    val newIntent = Intent(this@AppointmentActivity, MainActivity::class.java)
                    startActivity(newIntent)
                }.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        goBackWithoutSaving()
    }
}