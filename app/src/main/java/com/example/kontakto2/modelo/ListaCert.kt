package com.example.kontakto2.modelo

import com.example.kontakto2.R

class ListaCert {
    companion object{
        val list = listOf<certificaciones>(certificaciones(R.drawable.iso27001, "ISO 27001"),
            certificaciones(R.drawable.gptow, "GPTW"), certificaciones(R.drawable.iso90001, "ISO 90001"))
    }
}