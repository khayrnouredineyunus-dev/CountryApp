package com.example.countryapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.countryapp.databinding.ActivityPresensiBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PresensiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPresensiBinding

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusList = resources.getStringArray(R.array.presensi_status)

        with(binding) {
            val adapterStatus = ArrayAdapter(
                this@PresensiActivity,
                android.R.layout.simple_spinner_item,
                statusList
            )
            adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatus.adapter = adapterStatus

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }

            timePickerPresensi.setOnTimeChangedListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
            }

            spinnerStatus.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        // Hadir Tepat Waktu (indeks 0) tidak butuh keterangan tambahan
                        edtKeterangan.visibility = if (position == 0) View.GONE else View.VISIBLE
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // tidak ada aksi saat tidak ada pilihan
                    }
                }

            btnSubmit.setOnClickListener {
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("in", "ID"))
                val timeFormat = SimpleDateFormat("hh:mm a", Locale("in", "ID"))
                val tanggal = dateFormat.format(calendar.time)
                val jam = timeFormat.format(calendar.time)
                val status = statusList[spinnerStatus.selectedItemPosition]

                val hasil = if (edtKeterangan.visibility == View.VISIBLE &&
                    edtKeterangan.text.isNotBlank()
                ) {
                    "Presensi berhasil $tanggal jam $jam ($status - ${edtKeterangan.text})"
                } else {
                    "Presensi berhasil $tanggal jam $jam"
                }

                tvResult.text = hasil
                tvResult.visibility = View.VISIBLE
            }
        }

        // Latihan: tambahkan fitur exit custom dialog saat tombol back ditekan
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = DialogExit()
                dialog.show(supportFragmentManager, "dialogExitPresensi")
            }
        })
    }
}
