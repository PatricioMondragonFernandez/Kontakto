package com.example.kontakto2

import android.content.Intent
import android.opengl.Visibility
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.kontakto2.databinding.ActivityCreacionDeUsuarioBinding

class CreacionDeUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityCreacionDeUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreacionDeUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.siguiente.setOnClickListener {
            val email = validateEmail()
            val password = validatePassword()
            if(email == null && password == null){
                startActivity(Intent(this, DisenioTarjeta::class.java).putExtra(usuario, binding.LoginEmailEt.text.toString()))
            }else if(email == null && password != null){
                binding.checkE.visibility = View.VISIBLE
                binding.passwordTIL.helperText = password
                binding.EmailTIL.helperText = email
                binding.checkP.visibility = View.GONE
            }else if(password == null && email != null){
                binding.checkP.visibility = View.VISIBLE
                binding.EmailTIL.helperText = email
                binding.passwordTIL.helperText = password
                binding.checkE.visibility = View.GONE
            }else {
                binding.passwordTIL.helperText = password
                binding.checkE.visibility = View.GONE
                binding.checkP.visibility = View.GONE
                binding.EmailTIL.helperText = email
            }
        }

    }


    private fun validatePassword(): String? {
        val passwordtext = binding.LoginPasswordEt.text.toString()
        if(passwordtext.length < 8){
            return "La contraseña debe tener mas de 8 caracteres"
        }
        if (!passwordtext.matches(".*[A-Z].*".toRegex())){
            return "La contraseña debe contener al menos una mayuscula."
        }
        if (!passwordtext.matches(".*[a-z].*".toRegex())){
            return "La contraseña debe contener al menos una mayuscula."
        }
        if (!passwordtext.matches(".*[!@#.?].*".toRegex())){
            return "La contraseña debe contener al menos uno de estos caracteres: !@#.?."
        }
        return null
    }


    private fun validateEmail(): String? {
        val emailtext = binding.LoginEmailEt.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailtext).matches()){
            return "Email inválido"
        }
        return null
    }

    companion object {
        const val usuario = "usuario"
    }
}