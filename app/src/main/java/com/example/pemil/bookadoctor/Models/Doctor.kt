package com.example.pemil.bookadoctor.Models

data class Doctor(val name: String = "Ion Popescu",
                  val email: String = "test@test.com",
                  val specialty: String = "Oftalmologie",
                  val availableTimePeriods: List<String> = mutableListOf(),
                  val locationAddress: String = "Strada Soarelui nr 5")