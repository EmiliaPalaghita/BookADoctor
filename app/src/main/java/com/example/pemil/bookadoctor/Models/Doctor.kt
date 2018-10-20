package com.example.pemil.bookadoctor.Models

data class Doctor(val doctorId: Int = 1,
                  val name: String = "Ion Popescu",
                  val specialty: String = "Oftalmologie",
                  val availableTimePeriods: List<String> = mutableListOf(),
                  val locationId: Int = 5)