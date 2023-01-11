package com.example.kontakto2.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kontakto2.R
import com.example.kontakto2.modelo.certificaciones

class CertificacionesViewHolder(view: View): ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.nombreCert)
    val imagenCert = view.findViewById<ImageView>(R.id.ivCert)

    fun render(certificacion: certificaciones, onClickListener:(certificaciones) -> Unit){
        nombre.text = certificacion.nombre
        imagenCert.setImageResource(certificacion.imagen)
        itemView.setOnClickListener {onClickListener(certificacion)}

    }

}