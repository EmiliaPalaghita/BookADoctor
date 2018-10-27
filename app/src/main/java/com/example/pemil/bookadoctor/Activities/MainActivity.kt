package com.example.pemil.bookadoctor.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pemil.bookadoctor.Adapters.AppointmentAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

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

        //TODO de ce nu apare by default textul in Design?! test it on phone
    }
}
