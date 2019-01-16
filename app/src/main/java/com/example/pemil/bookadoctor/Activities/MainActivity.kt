package com.example.pemil.bookadoctor.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import com.example.pemil.bookadoctor.Adapters.AppointmentAdapter
import com.example.pemil.bookadoctor.Adapters.DoctorAppointmentAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.Models.Patient
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
    private lateinit var doctorAppointmentAdapter: DoctorAppointmentAdapter
    private var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("appointments")
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser
    private var appointments: MutableList<Appointment> = mutableListOf()
    private var isAlreadyLoaded = false
    var patient: Patient? = null
    var doctor: Doctor? = null

    var doctorsReference = database.getReference("doctors")
    var patientsReference = database.getReference("patients")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        profileIB.setOnClickListener { openProfileActivity() }

        (add_appointment as View).setOnClickListener {
            openNewAppointmentActivity()
        }

        retrieveDataFromDB()

        appointmentsListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val animShake = AnimationUtils.loadAnimation(this@MainActivity, R.anim.shake)
            view.startAnimation(animShake)

            val alertDialog = AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete appointment")
                    .setMessage("Do you really want to delete the appointment?")
                    .setNegativeButton("No") { _, _ -> view?.clearAnimation() }
                    .setPositiveButton("Yes") { _, _ ->
                        deleteAppointment(view, position)
                    }.create()
            alertDialog.show()
            true
        }

    }

    private fun retrieveDataFromDB() {
        doctorsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    if (data.key == user!!.uid) {
                        doctor = data.getValue(Doctor::class.java)!!
                    }
                }

            }
        })

        patientsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    if (data.key == user!!.uid) {
                        patient = data.getValue(Patient::class.java)!!
                    }
                }
            }
        })

    }

    private fun displayFirebaseError() {
        Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAppointment(view: View?, position: Int) {
        view?.clearAnimation()
        val appointment = appointments[position]

        myRef.child(FirebaseAuth.getInstance().uid!!).child(appointment.hashCode().toString()).removeValue()
        appointments = mutableListOf()
    }


    override fun onStart() {
        super.onStart()
        if (!isAlreadyLoaded) {
            updateUI(user)
            isAlreadyLoaded = true
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            retrieveAppointmentsData(user)
        }
    }

    private fun retrieveAppointmentsData(user: FirebaseUser) {
        appointments = mutableListOf()
        myRef.child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val appointment = data.getValue(Appointment::class.java)
                    if (appointment != null) {
                        appointments.add(appointment)
                    }
                }

                val sortedList = ArrayList(appointments)
                        .sortedWith(compareByDescending<Appointment> { it.date }
                                .thenBy { it.time })

                appointments = mutableListOf(*sortedList.toTypedArray())

                if (patient != null) {
                    appointmentAdapter = AppointmentAdapter(this@MainActivity, appointments)
                    appointmentsListView.adapter = appointmentAdapter
                } else if (doctor != null) {
                    doctorAppointmentAdapter = DoctorAppointmentAdapter(this@MainActivity, appointments)
                    appointmentsListView.adapter = doctorAppointmentAdapter
                }


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

    override fun onBackPressed() {}
}
