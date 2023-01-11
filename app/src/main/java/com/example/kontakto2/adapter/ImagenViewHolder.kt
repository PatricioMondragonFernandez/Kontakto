package com.example.kontakto2.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kontakto2.R
import com.example.kontakto2.modelo.imagenes

class ImagenViewHolder(view: View ):RecyclerView.ViewHolder(view) {

    val imagen = view.findViewById<ImageView>(R.id.ivimagen)

    fun render(imagenes: imagenes, onClickListener: (imagenes) -> Unit){
        imagen.setImageBitmap(imagenes.imagen)
        itemView.setOnClickListener { onClickListener(imagenes)}
    }
}