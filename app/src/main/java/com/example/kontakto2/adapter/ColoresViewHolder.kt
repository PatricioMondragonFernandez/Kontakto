package com.example.kontakto2.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.R
import com.example.kontakto2.modelo.colores

class ColoresViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val color1 = view.findViewById<ImageView>(R.id.color1)
    val color2 = view.findViewById<ImageView>(R.id.color2)
    val color3 = view.findViewById<ImageView>(R.id.color3)
    val nombre = view.findViewById<TextView>(R.id.nombreCTv)

    fun render(colores: colores, onClickListener:(colores) -> Unit){
        color1.setBackgroundColor(Color.parseColor(colores.color1))
        color2.setBackgroundColor(Color.parseColor(colores.color2))
        color3.setBackgroundColor(Color.parseColor(colores.color3))
        nombre.text = colores.nombre
        itemView.setOnClickListener {onClickListener(colores)}


    }
}