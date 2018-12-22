package com.example.pemil.bookadoctor.Models

import java.util.Date

data class Patient(val uuid: String,
                   val fullName: String = "default_name",
                   val birthday: Date = Date(),
                   val sex: String = "male",
                   val address: String = "Strada Floarea Soarelui nr 5",
                   val socialSecurityNumber: String = "1234567890123",
                   val seriesNumber: String = "AB12345",
                   val healthCardNumber: String = "1234567812345678",
                   val username: String = "user",
                   val email: String = "test@test.com"
)