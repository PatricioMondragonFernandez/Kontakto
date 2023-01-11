package com.example.kontakto2

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.adapter.CertificacionesAdapter
import com.example.kontakto2.modelo.ListaCert
import com.example.kontakto2.modelo.certificaciones

class recyclerViewCertDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_certificaciones, container)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCert)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CertificacionesAdapter(ListaCert.list, {onItemSelected(it)})
        return view
    }

    fun onItemSelected(certificacion: certificaciones){
        val view = layoutInflater.inflate(R.layout.item_cert, null, false)
        val iv = view.findViewById<ImageView>(R.id.ivCert)
        var bandera = true
        val nombre = view.findViewById<TextView>(R.id.nombreCert)
        val layout = activity?.findViewById<LinearLayoutCompat>(R.id.layoutCertificaciones)
        val layout2 = activity?.findViewById<LinearLayoutCompat>(R.id.layoutCertificaciones2)
        for(i in (0 until (activity as? DisenioTarjeta)!!.certificaciones.size)){
            if (certificacion.nombre == (activity as? DisenioTarjeta)!!.certificaciones[i]){
                Toast.makeText(context, "Ya añadiste esa certificacion.", Toast.LENGTH_SHORT).show()
                bandera = false
                break
            }else{
            }
        }
        if (bandera){
            (activity as? DisenioTarjeta)!!.numCert += 1
            println((activity as? DisenioTarjeta)!!.numCert)
            if((activity as? DisenioTarjeta)!!.numCert % 2 == 0){
                layout2?.addView(view)
                iv.setImageResource(certificacion.imagen)
                nombre.text = certificacion.nombre
                (activity as? DisenioTarjeta)!!.certificaciones.add(certificacion.nombre)

            }else{
                layout?.addView(view)
                iv.setImageResource(certificacion.imagen)
                nombre.text = certificacion.nombre
                (activity as? DisenioTarjeta)!!.certificaciones.add(certificacion.nombre)
            }
        }else{

        }
    }
}