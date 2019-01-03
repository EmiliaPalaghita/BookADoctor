package com.example.pemil.bookadoctor.Models

data class Appointment(val patientId: Int = 0,
                       val doctorId: Int = 0,
                       val doctorName: String = "",
                       val specialty: String = "",
                       val date: String = "",
                       val time: String = "",
                       val locationId: Int = 1,
                       val locationName: String = "",
                       val appointmentType: String = "")