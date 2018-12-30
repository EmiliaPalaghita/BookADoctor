package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.pemil.bookadoctor.Adapters.AppointmentAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var appointmentAdapter: AppointmentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        createDummyData()

        profileIB.setOnClickListener { openProfileActivity() }

        (add_appointment as View).setOnClickListener { openNewAppointmentActivity() }

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
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time,
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardiologie"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen2",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time,
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardiologie"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen3",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time,
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
}
