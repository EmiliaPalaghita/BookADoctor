package com.example.pemil.bookadoctor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pemil.bookadoctor.Adapters.AppointmentAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var appointments: MutableList<Appointment> = mutableListOf()
        appointments.add(Appointment(time = "15:30"))
        appointments.add(Appointment(time = "12:30"))
        appointments.add(Appointment(doctorName = "Dr. George Jmen",
                date = GregorianCalendar(2017, Calendar.MARCH, 15).time,
                time = "12:00",
                locationName = "Splaiul Indepententei 124, Sector 4",
                specialty = "Cardioogie"))

        val sortedList = ArrayList(appointments)
                .sortedWith(compareByDescending<Appointment> { it.date }
                        .thenBy { it.time })

        appointments = mutableListOf(*sortedList.toTypedArray())

        appointmentAdapter = AppointmentAdapter(this, appointments)
        appointmentsListView.adapter = appointmentAdapter

        //TODO de ce nu apare by default textul in Design?! test it on phone
    }
}
