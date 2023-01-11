package com.example.kontakto2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.R
import com.example.kontakto2.modelo.certificaciones

class CertificacionesAdapter(private val certificacionesList : List<certificaciones>, private val onClickListener: (certificaciones) -> Unit) : RecyclerView.Adapter<CertificacionesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificacionesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CertificacionesViewHolder(layoutInflater.inflate(R.layout.item_cert, parent, false))

    }

    override fun onBindViewHolder(holder: CertificacionesViewHolder, position: Int) {
        val item = certificacionesList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return certificacionesList.size
    }
}