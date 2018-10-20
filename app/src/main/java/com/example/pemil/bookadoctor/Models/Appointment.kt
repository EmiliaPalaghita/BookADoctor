package com.example.pemil.bookadoctor.Models

import java.util.*

data class Appointment(val patientId: Int = 0,
                       val doctorId: Int = 0,
                       val date: Date = Date(),
                       val time: String = "HH:mm",
                       val locationId: Int = 1,
                       val appointmentType: String = "Consult")