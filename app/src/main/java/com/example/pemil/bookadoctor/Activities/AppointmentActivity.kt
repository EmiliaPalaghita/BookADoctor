package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.appointment_activity.*
import java.util.stream.IntStream


class AppointmentActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    var specialtiesReference = database.getReference("specialties")
    var doctorsReference = database.getReference("doctors")
    var clinicsReference = database.getReference("health-clinics")

    var specialtiesList = mutableListOf<String>()
    var doctorsList = mutableListOf<String>()
    var doctorsMap = mutableMapOf<String, Doctor>()
    var healthClinicList = mutableListOf<String>()

    var isDoctorSelected = false

    companion object {
        const val DEFAULT_VALUE = "Choose an option"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appointment_activity)

        edit_cancel_tv.visibility = View.GONE

        back_ib.setOnClickListener { goBackWithoutSaving() }

        save_button.setOnClickListener { saveAppointment() }

        spec_info_ib.setOnClickListener { openSpecializationDetails() }

        specialtiesList.add(DEFAULT_VALUE)
        doctorsList.add(DEFAULT_VALUE)
        healthClinicList.add(DEFAULT_VALUE)

        specialization_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isDoctorSelected) {
                    if (position != 0) {
                        filterSpinnersForSpecialization(position)
                    } else {
                        updateDoctorsAdapter(doctorsList)
                        updateHealthClinicAdapter(healthClinicList)
                    }
                } else {
                    isDoctorSelected = false
                }
            }
        }

        doctor_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    filterSpinnersForDoctor(position)
                    isDoctorSelected = true
                }
            }
        }

        retrieveDataFromDB()

    }

    private fun filterSpinnersForDoctor(position: Int) {
        val doctorName = doctorsList[position]
        val doctor = doctorsMap[doctorName]

        val indexOfSpecialization = IntStream.range(0, specialtiesList.size)
                .filter { it -> specialtiesList[it] == doctor!!.specialty }
                .findFirst()
                .orElse(0)

        specialization_spinner.setSelection(indexOfSpecialization)

        val indexOfClinic = IntStream.range(0, healthClinicList.size)
                .filter { it -> healthClinicList[it] == doctor!!.healthClinicName + ", " + doctor.healthClinicAddress }
                .findFirst()
                .orElse(0)

        health_center_spinner.setSelection(indexOfClinic, true)

    }

    private fun updateSpecializationAdapter(specList: List<String>) {
        specialization_spinner.adapter = ArrayAdapter<String>(this@AppointmentActivity,
                android.R.layout.simple_spinner_item, specList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun updateDoctorsAdapter(doctorList: List<String>) {
        doctor_name_spinner.adapter = ArrayAdapter<String>(this@AppointmentActivity,
                android.R.layout.simple_spinner_item, doctorList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun updateHealthClinicAdapter(clinicList: List<String>) {
        health_center_spinner.adapter = ArrayAdapter<String>(this@AppointmentActivity,
                android.R.layout.simple_spinner_item, clinicList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun displayFirebaseError() {
        Toast.makeText(this@AppointmentActivity, "Failed to load data", Toast.LENGTH_LONG).show()
    }

    private fun filterSpinnersForSpecialization(position: Int) {
        val selectedSpecialization = specialtiesList[position]

        val selectedDoctors = mutableListOf<String>()
        selectedDoctors.add(DEFAULT_VALUE)
        selectedDoctors.addAll(doctorsMap
                .filter { it.value.specialty == selectedSpecialization }
                .values.map { it -> it.fullName }.toMutableList()
        )

        updateDoctorsAdapter(selectedDoctors)

        val selectedClinics = mutableListOf<String>()
        selectedClinics.add(DEFAULT_VALUE)
        selectedClinics.addAll(doctorsMap.filter { it.value.specialty == selectedSpecialization }
                .values.map { it -> it.healthClinicName + ", " + it.healthClinicAddress }.toMutableList())

        updateHealthClinicAdapter(selectedClinics)
    }

    private fun openSpecializationDetails() {
        val newIntent = Intent(this@AppointmentActivity, SpecializationActivity::class.java)
        startActivity(newIntent)
    }

    private fun retrieveDataFromDB() {
        updateSpecialization()
        updateDoctor()
        updateClinics()
    }

    private fun updateClinics() {
        clinicsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val clinic = data.getValue(String::class.java)
                    healthClinicList.add(clinic!!)
                }

                updateHealthClinicAdapter(healthClinicList)
            }
        })
    }

    private fun updateDoctor() {
        doctorsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val doctor = data.getValue(Doctor::class.java)
                    doctorsList.add(doctor!!.fullName)
                    doctorsMap[doctor.fullName] = doctor

                    updateDoctorsAdapter(doctorsList)
                }
            }
        })
    }

    private fun updateSpecialization() {
        specialtiesReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val specialtyName = data.child("name").getValue(String::class.java)
                    specialtiesList.add(specialtyName!!)
                }

                updateSpecializationAdapter(specialtiesList)
            }
        })
    }

    private fun saveAppointment() {
        // TODO
    }

    private fun goBackWithoutSaving() {
        val alertDialog = AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Are you sure you want to discard changes?")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ ->
                    val newIntent = Intent(this@AppointmentActivity, MainActivity::class.java)
                    startActivity(newIntent)
                }.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        goBackWithoutSaving()
    }
}