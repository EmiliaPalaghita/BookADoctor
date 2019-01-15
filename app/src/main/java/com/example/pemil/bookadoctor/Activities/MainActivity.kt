package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
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
import kotlinx.android.synthetic.main.appointment_item.view.*
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var appointmentAdapter: AppointmentAdapter

    private var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("appointments")
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var appointments: MutableList<Appointment> = mutableListOf()
    private var isAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        profileIB.setOnClickListener { openProfileActivity() }

        (add_appointment as View).setOnClickListener {
            openNewAppointmentActivity()
        }

        appointmentsListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val animShake = AnimationUtils.loadAnimation(this@MainActivity, R.anim.shake)
            view.startAnimation(animShake)
            view.cardTopPart.cancel_button.visibility = View.VISIBLE
            view.cardTopPart.cancel_button.setOnClickListener { deleteAppointment(view, position) }
            true
        }


    }

    private fun deleteAppointment(view: View?, position: Int) {
        view?.clearAnimation()
        view?.cardTopPart?.cancel_button?.visibility = View.GONE
        // TODO - delete based on position
        val appointment = appointments[position]
    }


    override fun onStart() {
        super.onStart()
        if (!isAlreadyLoaded) {
            val currentUser = mAuth.currentUser
            updateUI(currentUser)
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
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
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

                appointmentAdapter = AppointmentAdapter(this@MainActivity, appointments)
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

    override fun onBackPressed() {}
}
