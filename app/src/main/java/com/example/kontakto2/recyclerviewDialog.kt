package com.example.kontakto2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.tv.TvContract.Programs.Genres.encode
import android.os.Bundle
import android.util.Base64.encode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kontakto2.adapter.ImagenAdapter
import com.example.kontakto2.modelo.ListaImagenes
import com.example.kontakto2.modelo.imagenes
import java.io.ByteArrayOutputStream
import android.util.Base64

class recyclerviewDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_foto_fondo, container)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewI)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ImagenAdapter((activity as? DisenioTarjeta)!!.listaImagenes, {onItemSelected(it)})
        return view
    }

    fun onItemSelected(imagen: imagenes){
        val ivFondo = activity?.findViewById<ImageView>(R.id.ivfondo)
        val src = imagen.imagen
        if (ivFondo != null) {
            Glide.with(ivFondo.context).load(src).into(ivFondo)
        }
        val baos = ByteArrayOutputStream()
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val encodedImgage = Base64.encodeToString(b, Base64.DEFAULT)

    }
}