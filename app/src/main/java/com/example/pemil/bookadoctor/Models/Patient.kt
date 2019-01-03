package com.example.pemil.bookadoctor.Models

data class Patient(val uuid: String = "",
                   val fullName: String = "",
                   val birthday: String = "",
                   val sex: String = "",
                   val address: String = "",
                   val socialSecurityNumber: String = "",
                   val seriesNumber: String = "",
                   val healthCardNumber: String = "",
                   val username: String = "",
                   val email: String = ""
)