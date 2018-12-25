package com.example.pemil.bookadoctor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.appointment_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(context: Context, private val elems: List<Appointment>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.appointment_item, parent, false)
        val appointment = elems[position]
        val dateFormat = SimpleDateFormat("dd - MMM - yyyy", Locale.ENGLISH)

        return rowView.apply {
            appointmentTypeTextView.text = appointment.appointmentType
            appointmentDateTextView.text = dateFormat.format(appointment.date)
            appointmentTimeTextView.text = appointment.time
            clinicAddressTextView.text = appointment.locationName
            doctorNameTextView.text = appointment.doctorName
            medicalSpecialtyTextView.text = appointment.specialty
        }
    }

    override fun getItem(position: Int) = elems[position]

    override fun getItemId(position: Int) = elems[position].hashCode().toLong()

    override fun getCount() = elems.size
}