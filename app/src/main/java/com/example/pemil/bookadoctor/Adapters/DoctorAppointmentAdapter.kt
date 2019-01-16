package com.example.pemil.bookadoctor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.doctor_appointment_item.view.*

class DoctorAppointmentAdapter(context: Context, private val elems: MutableList<Appointment>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.doctor_appointment_item, parent, false)
        val appointment = elems[position]

        return rowView.apply {
            clinicAddressTextView.text = "Maria Iona"
            appointmentDateTextView.text = appointment.date
            appointmentTimeTextView.text = appointment.time
        }
    }

    override fun getItem(position: Int) = elems[position]

    override fun getItemId(position: Int) = elems[position].hashCode().toLong()

    override fun getCount() = elems.size
}