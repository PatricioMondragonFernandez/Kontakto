package com.example.kontakto2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.R
import com.example.kontakto2.modelo.colores

class Coloresdapter(private val coloresList: List<colores>, private val onClickListener:(colores) -> Unit) : RecyclerView.Adapter<ColoresViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColoresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ColoresViewHolder(layoutInflater.inflate(R.layout.item_colores, parent, false))

    }

    override fun onBindViewHolder(holder: ColoresViewHolder, position: Int) {
        val item = coloresList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return coloresList.size
    }
}