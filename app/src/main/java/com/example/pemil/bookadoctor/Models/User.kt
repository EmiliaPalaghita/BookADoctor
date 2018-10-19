package com.example.pemil.bookadoctor.Models

data class User(val isPatient: Boolean = true,
                val name: String = "default_name",
                val email: String = "test@test.com",
                val cnp: String = "1234567890123"
)