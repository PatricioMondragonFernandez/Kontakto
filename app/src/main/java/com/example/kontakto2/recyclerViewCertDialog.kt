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
        //Se crea el dialog y se pone en pantalla
        val view = inflater.inflate(R.layout.recyclerview_certificaciones, container)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCert)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //Se crea el adaptador del recycler view, se le pasa la lista de certificaciones y la funcion de click de los componentes
        recyclerView.adapter = CertificacionesAdapter(ListaCert.list, {onItemSelected(it)})
        return view
    }
    //Funcion que se llama cuando se da click en los componentes
    fun onItemSelected(certificacion: certificaciones){
        //Se crea un view de tipo item certificación
        val view = layoutInflater.inflate(R.layout.item_cert, null, false)
        //Se inicializa el image view del view de tipo item certificación
        val iv = view.findViewById<ImageView>(R.id.ivCert)
        //Bandera que cambia a falso si la certificacion clickeada ya fue añadida
        var bandera = true
        //Se inicializa el tex view del item certificación donde va el nombre
        val nombre = view.findViewById<TextView>(R.id.nombreCert)
        //Se inicializan los dos layouts donde se añaden los certificados que el usuario clickea
        val layout = activity?.findViewById<LinearLayoutCompat>(R.id.layoutCertificaciones)
        val layout2 = activity?.findViewById<LinearLayoutCompat>(R.id.layoutCertificaciones2)
        //Se checa si el certificado que el usuario clickeo ya fue añadido, en caso de que ya haya sido añadido, sale ese toast
        for(i in (0 until (activity as? DisenioTarjeta)!!.certificaciones.size)){
            if (certificacion.nombre == (activity as? DisenioTarjeta)!!.certificaciones[i]){
                Toast.makeText(context, "Ya añadiste esa certificacion.", Toast.LENGTH_SHORT).show()
                bandera = false
                break
            }else{
            }
        }
        //Si la bandera es true, quiere decir que el certificado clickeado no ha sido añadido y se añade a
        if (bandera){
            //Se suma 1 al numero de certificaciones(numCert), si el numero es non se añade en el layout 1, si es par se añade en el layout 2
            //Esto es para que se añadan simetricamente
            //(activity as? Diseniotarjeta)!!.variable  significa que estamos accediendo a una variable de la activity DisenioTarjeta
            (activity as? DisenioTarjeta)!!.numCert += 1
            //println((activity as? DisenioTarjeta)!!.numCert)
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