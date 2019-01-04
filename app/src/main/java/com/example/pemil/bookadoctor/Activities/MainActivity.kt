package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.pemil.bookadoctor.Adapters.AppointmentAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var appointmentAdapter: AppointmentAdapter

    private var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("appointments")
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var appointmets: MutableList<Appointment> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        createDummyData()

        profileIB.setOnClickListener { openProfileActivity() }

        (add_appointment as View).setOnClickListener { openNewAppointmentActivity() }

        val currentUser = mAuth.currentUser
        updateUI(currentUser)

        //TODO - create add appointment
        //TODO - generate unique ID for each appointment for a patient, so that patient's uid will have a list of id's each representing an appointment
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            retrieveAppointmentsData(user)
        }
    }

    private fun retrieveAppointmentsData(user: FirebaseUser) {
        myRef.child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val appointment = data.getValue(Appointment::class.java)
                    if (appointment != null) {
                        appointmets.add(appointment)
                    }
                }

                val sortedList = ArrayList(appointmets)
                        .sortedWith(compareByDescending<Appointment> { it.date }
                                .thenBy { it.time })

                appointmets = mutableListOf(*sortedList.toTypedArray())

                appointmentAdapter = AppointmentAdapter(this@MainActivity, appointmets)
                appointmentsListView.adapter = appointmentAdapter
            }

        })
    }

    private fun openNewAppointmentActivity() {
        val intent = Intent(applicationContext, AppointmentActivity::class.java)
        startActivity(intent)
    }

    private fun openProfileActivity() {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun createDummyData() {
        var appointments: MutableList<Appointment> = mutableListOf()
        appointments.add(Appointment(time = "15:30"))
        appointments.add(Appointment(time = "12:30"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time.toString(),
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardiologie"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen2",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time.toString(),
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardiologie"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen3",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time.toString(),
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardiologie"))

        val sortedList = ArrayList(appointments)
                .sortedWith(compareByDescending<Appointment> { it.date }
                        .thenBy { it.time })

        appointments = mutableListOf(*sortedList.toTypedArray())

        appointmentAdapter = AppointmentAdapter(this, appointments)
        appointmentsListView.adapter = appointmentAdapter
    }

    override fun onBackPressed() {}
}
