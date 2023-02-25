package com.example.kontakto2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kontakto2.adapter.Coloresdapter
import com.example.kontakto2.modelo.colores
import org.w3c.dom.Text
import java.lang.Integer.parseInt

class recyclerviewColoresDialog : DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Se crea el dialog con el recycler view de colores y se muestra
        val view = inflater.inflate(R.layout.recycler_view_colores, container)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewC)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //Se le añade la lista de colores recibida en la activity DisenioTarjeta y se le pasa la funcion onItemSelected
        recyclerView.adapter = Coloresdapter((activity as? DisenioTarjeta)!!.listaColores, {onItemSelected(it)})
        return view
    }

    //Para obtener un color un poco mas obscuro que va en la barrita de las redes sociales
    @ColorInt fun darkenColor(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.9f
        })
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @RequiresApi(Build.VERSION_CODES.M)
    fun onItemSelected(colores: colores){
        val color1 = Color.parseColor(colores.color1)
        val color2 = darkenColor(color1)
        println(color1)
        println(color2)
        //Se inicializan todos los elementos del activity DisenioTarjeta que van a cambiar de color
        val fondoGrande = activity?.findViewById<ConstraintLayout>(R.id.backgroundAcTarjeta)
        val fondoLogo = activity?.findViewById<ConstraintLayout>(R.id.layoutDisenio1)
        val fondoAcerca = activity?.findViewById<ConstraintLayout>(R.id.acercadeLayout)
        val fondocert = activity?.findViewById<ConstraintLayout>(R.id.certificacionesLayout)
        val nombreTv = activity?.findViewById<TextView>(R.id.editaNombreTv)
        val puestoTv = activity?.findViewById<TextView>(R.id.editaPuestotv)
        val tuLogotv = activity?.findViewById<TextView>(R.id.tuLogoTv)
        val direccionTv = activity?.findViewById<TextView>(R.id.direccionTv)
        val emailTv = activity?.findViewById<TextView>(R.id.emailTv)
        val telefonoTv = activity?.findViewById<TextView>(R.id.telefonoTv)
        val celularTv = activity?.findViewById<TextView>(R.id.celularTv)
        val siteTv = activity?.findViewById<TextView>(R.id.siteTv)
        val acercaTv = activity?.findViewById<TextView>(R.id.acercaTv)
        val infoEmpresaTv = activity?.findViewById<TextView>(R.id.infoEmpresatv)
        val certTv = activity?.findViewById<TextView>(R.id.certTv)
        val layoutRedesC = activity?.findViewById<ConstraintLayout>(R.id.layoutRedesC)
        val aniadered = activity?.findViewById<ImageButton>(R.id.aniadeRed)
        val aniadeCert = activity?.findViewById<ImageButton>(R.id.aniadeCert)
        //Se les añade el color indicado
        fondoGrande?.setBackgroundColor(Color.parseColor(colores.color2))
        fondoLogo?.setBackgroundColor(Color.parseColor(colores.color1))
        fondoAcerca?.setBackgroundColor(Color.parseColor(colores.color3))
        fondocert?.setBackgroundColor(Color.parseColor(colores.color1))
        nombreTv?.setTextColor(Color.parseColor(colores.color4))
        puestoTv?.setTextColor(Color.parseColor(colores.color4))
        tuLogotv?.setTextColor(Color.parseColor(colores.color4))
        direccionTv?.setTextColor(Color.parseColor(colores.color5))
        direccionTv?.compoundDrawableTintList = getColorStateList(requireContext(), R.color.white)
        emailTv?.setTextColor(Color.parseColor(colores.color5))
        emailTv?.compoundDrawableTintList = getColorStateList(requireContext(), R.color.white)
        telefonoTv?.setTextColor(Color.parseColor(colores.color5))
        telefonoTv?.compoundDrawableTintList = getColorStateList(requireContext(), R.color.white)
        celularTv?.setTextColor(Color.parseColor(colores.color5))
        celularTv?.compoundDrawableTintList = getColorStateList(requireContext(), R.color.white)
        siteTv?.compoundDrawableTintList = getColorStateList(requireContext(), R.color.white)
        siteTv?.setTextColor(Color.parseColor(colores.color5))
        acercaTv?.setTextColor(Color.parseColor(colores.color6))
        infoEmpresaTv?.setTextColor(Color.parseColor(colores.color6))
        certTv?.setTextColor(Color.parseColor(colores.color4))
        layoutRedesC?.setBackgroundColor(color2)
        aniadered?.imageTintList = getColorStateList(requireContext(), R.color.white)
        aniadeCert?.imageTintList = getColorStateList(requireContext(), R.color.white)

    }

}