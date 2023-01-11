package com.example.kontakto2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.R
import com.example.kontakto2.modelo.imagenes

class ImagenAdapter(private val lista:List<imagenes>, private val onClickListener: (imagenes) -> Unit) : RecyclerView.Adapter<ImagenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImagenViewHolder(layoutInflater.inflate(R.layout.item_imagen, parent, false))
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        val item = lista[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return lista.size

    }
}