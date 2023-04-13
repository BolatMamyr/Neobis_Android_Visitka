package com.example.visitka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.visitka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var doctor1: Doctor
    lateinit var doctor2: Doctor
    lateinit var doctor3: Doctor
    lateinit var doctor4: Doctor
    lateinit var doctor5: Doctor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDoctors()
        setClickListeners()
    }

    private fun setClickListeners() {
        val intent = Intent(this, ProfileActivity::class.java)
        binding.apply {
            btn1.setOnClickListener {
                intent.putExtra(EXTRA_DOCTOR, doctor1)
                startActivity(intent)
            }
            btn2.setOnClickListener {
                intent.putExtra(EXTRA_DOCTOR, doctor2)
                startActivity(intent)
            }
            btn3.setOnClickListener {
                intent.putExtra(EXTRA_DOCTOR, doctor3)
                startActivity(intent)
            }
            btn4.setOnClickListener {
                intent.putExtra(EXTRA_DOCTOR, doctor4)
                startActivity(intent)
            }
            btn5.setOnClickListener {
                intent.putExtra(EXTRA_DOCTOR, doctor5)
                startActivity(intent)
            }
        }
    }

    private fun initDoctors() {
        // Used my phone number
        doctor1 = Doctor(
            name = "Azamat",
            lastName = "Novikov",
            email = "anovikov@gmail.com"
        )
        doctor2 = Doctor(
            "Zamir",
            "Nurmatov",
            "znurmatov@gmail.com"
        )
        doctor3 = Doctor(
            "Munisa",
            "Khalilova",
            "mkhalilova@gmail.com"
        )
        doctor4 = Doctor(
            "Adina",
            "Alieva",
            "aalieva@gmail.com"
        )
        doctor5 = Doctor(
            "Viktoria",
            "Kolesova",
            "vkolesova@gmail.com"
        )
    }

    companion object {
        const val EXTRA_DOCTOR = "doctor"
    }
}