package com.example.kontakto2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kontakto2.databinding.ActivityDisenioTarjetaBinding
import com.example.kontakto2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonmain.setOnClickListener {
            startActivity(Intent(this, DisenioTarjeta::class.java))
        }





    }
}