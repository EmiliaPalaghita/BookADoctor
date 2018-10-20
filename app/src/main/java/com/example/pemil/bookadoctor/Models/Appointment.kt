package com.example.pemil.bookadoctor.Models

import java.util.*

data class Appointment(val patientId: Int = 0,
                       val doctorId: Int = 0,
                       val doctorName: String = "Ion Popescu",
                       val specialty: String = "Oftalmologie",
                       val date: Date = Calendar.getInstance().time,
                       val time: String = "HH:mm",
                       val locationId: Int = 1,
                       val locationName: String = "Strada Soarelui nr 5, Sector 5, Bucuresti",
                       val appointmentType: String = "Consult")