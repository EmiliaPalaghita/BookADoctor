package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.R
import com.google.firebase.auth.FirebaseAuth
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
    var appointmentsDatabase = database.getReference("appointments")

    var specialtiesList = mutableListOf<String>()
    var doctorsList = mutableListOf<String>()
    var doctorsMap = mutableMapOf<String, Doctor>()
    var healthClinicList = mutableListOf<String>()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var isDoctorSelected = false

    var time = mutableListOf("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00")

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

        specialization_spinner.layoutMode = Spinner.MODE_DROPDOWN

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
                isDoctorSelected = if (position != 0) {
                    filterSpinnersForDoctor(position)
                    true
                } else {
                    false
                }
            }
        }

        health_center_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isDoctorSelected) {
                    if (position != 0) {
                        filterSpinnersForHealthClinic(position)
                    }
                }
            }
        }

        retrieveDataFromDB()

    }

    private fun filterSpinnersForHealthClinic(position: Int) {
        val clinicName = healthClinicList[position]

        val selectedDoctors = mutableListOf<String>()
        selectedDoctors.add(DEFAULT_VALUE)
        selectedDoctors.addAll(doctorsMap
                .filter { it.value.healthClinicName + ", " + it.value.healthClinicAddress == clinicName }
                .values.map { it -> it.fullName }.toMutableList()
        )

        if (selectedDoctors.size == 1) {
            updateDoctorsAdapter(mutableListOf(DEFAULT_VALUE))
        } else {
            updateDoctorsAdapter(selectedDoctors)
        }

        val specializations = mutableListOf<String>()
        specializations.add(DEFAULT_VALUE)
        specializations.addAll(doctorsMap
                .filter { it.value.healthClinicName + ", " + it.value.healthClinicAddress == clinicName }
                .values.map { it -> it.specialty }.toMutableList())
    }

    private fun filterSpinnersForDoctor(position: Int) {
        isDoctorSelected = false
        val doctorName = doctorsList[position]
        val doctor = doctorsMap[doctorName]

        val indexOfSpecialization = IntStream.range(0, specialtiesList.size)
                .filter { it -> specialtiesList[it] == doctor!!.specialty }
                .findFirst()
                .orElse(0)

        if (specialization_spinner.selectedItemPosition != indexOfSpecialization) {
            specialization_spinner.setSelection(indexOfSpecialization)
        }

        val currentClinics = mutableListOf<String>()

        for (i in 0 until health_center_spinner.adapter.count) {
            currentClinics.add(health_center_spinner.adapter.getItem(i) as String)
        }

        val indexOfClinic = IntStream.range(0, currentClinics.size)
                .filter { it -> currentClinics[it] == doctor!!.healthClinicName + ", " + doctor.healthClinicAddress }
                .findFirst()
                .orElse(0)

        if (health_center_spinner.selectedItemPosition != indexOfClinic) {
            health_center_spinner.setSelection(indexOfClinic, true)

        }

        updateTimeAdapter(time)
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

    private fun updateTimeAdapter(timeList: List<String>) {
        time_spinner.adapter = ArrayAdapter<String>(this@AppointmentActivity,
                android.R.layout.simple_spinner_item, timeList).apply {
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

        if (selectedDoctors.size == 1) {
            updateDoctorsAdapter(mutableListOf(DEFAULT_VALUE))
        } else {
            updateDoctorsAdapter(selectedDoctors)
        }

        val selectedClinics = mutableListOf<String>()
        selectedClinics.add(DEFAULT_VALUE)
        selectedClinics.addAll(doctorsMap.filter { it.value.specialty == selectedSpecialization }
                .values.map { it -> it.healthClinicName + ", " + it.healthClinicAddress }.toMutableList())

        updateHealthClinicAdapter(selectedClinics)

        updateTimeAdapter(mutableListOf())
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
        if (checkIfInputsEmpty()) {
            val doctor = doctorsMap[doctor_name_spinner.selectedItem]
            val appointment = Appointment(mAuth.currentUser!!.uid,
                    doctor!!.uuid,
                    doctor.fullName,
                    doctor.specialty,
                    date_picker.text.toString(),
                    time_spinner.selectedItem.toString(),
                    health_center_spinner.selectedItemPosition,
                    health_center_spinner.selectedItem.toString(),
                    "Consult"
            )

            val id = appointment.hashCode()

            appointmentsDatabase.child(mAuth.currentUser!!.uid).child(id.toString()).setValue(appointment)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setErrorMessageForSpinner(view: TextView) {
        view.error = ""
        view.text = "This field can't be empty"
        view.setTextColor(Color.RED)
    }

    private fun isEditTextEmpty(editText: EditText): Boolean {
        val value = editText.text.toString()
        if (value.isEmpty()) {
            editText.error = "This field can't be empty"
            return true
        }
        return false
    }

    private fun checkIfInputsEmpty(): Boolean {
        var check = true
        if (specialization_spinner.selectedItem == DEFAULT_VALUE) {
            val errorText = specialization_spinner.selectedView as TextView
            setErrorMessageForSpinner(errorText)
            check = false
        }

        if (doctor_name_spinner.selectedItem == DEFAULT_VALUE) {
            val errorText = doctor_name_spinner.selectedView as TextView
            setErrorMessageForSpinner(errorText)
            check = false
        }

        if (health_center_spinner.selectedItem == DEFAULT_VALUE) {
            val errorText = health_center_spinner.selectedView as TextView
            setErrorMessageForSpinner(errorText)
            check = false
        }

        if (isEditTextEmpty(date_picker)) check = false

        return check
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