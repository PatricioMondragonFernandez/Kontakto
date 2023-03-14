package com.example.kontakto2

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kontakto2.databinding.ActivityDisenioTarjetaBinding
import com.example.kontakto2.modelo.colores
import com.example.kontakto2.modelo.imagenes
import com.google.android.material.textfield.TextInputLayout
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.util.Base64


class DisenioTarjeta : AppCompatActivity() {

    private lateinit var binding: ActivityDisenioTarjetaBinding
    //Es el numero que se le asigna a una red para ver en que slot del arreglo de redes se va a colocar
    var red: Int = 0
    //Variable que maneja el numero de redes que el usuario ha añadido
    var numRdes = 0
    //Arreglo vacio que se va llenando con las redes que ingresa el usuario
    var redes = arrayOf<String>("0","0","0","0","0","0")
    var certificaciones = mutableListOf<String>("null")
    //Lista que recibira los colores disponibles para la terjeta
    var listaColores = mutableListOf<colores>()
    //Lista que recibira las imagenes de fondo de la tarjeta
    var listaImagenes = mutableListOf<imagenes>()
    //Arreglo de datos en donde se guardaran los datos que el usuario agregue para despues mandarlos en un json
    var arrayDatos = arrayOf("","","","","","","","","","","","","","","","","","","","","","","","","","","")
    //Instancia del dialog donde se mostraran las imagenes
    val dialogImagenes = recyclerviewDialog()
    var numCert = 0
    //Instancia de dialog donde se mostraran las certificaciones
    val dialogCertificaciones = recyclerViewCertDialog()
    //Instancia de dialog donde se mostraran los colores
    val dialogColores = recyclerviewColoresDialog()
    //Logica que abre el selector de imagenes para que el usuario elija su foto, soporta versiones pasadas de android y nuevas.
    val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null ){
            if(Build.VERSION.SDK_INT < 28){
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                //Le quita el fondo a la imagen, y la pone en el imageview de la foto del usuario
                BackgroundRemover.bitmapForProcessing(
                    bitmap,
                    true,
                    object: OnBackgroundChangeListener {
                        override fun onSuccess(bitmap: Bitmap) {
                            binding.ivfoto.setImageBitmap(bitmap)
                            binding.tvFoto.visibility = View.GONE
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val b = baos.toByteArray()
                            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                            //La base 64 se guarda en el arreglo de los datos
                            arrayDatos[8] = encodedImage

                        }

                        override fun onFailed(exception: Exception) {
                            Toast.makeText(DisenioTarjeta(), "Ocurrió un error cargando la foto.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }else{
                val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                BackgroundRemover.bitmapForProcessing(
                    bitmap,
                    true,
                    object: OnBackgroundChangeListener {
                        override fun onSuccess(bitmap: Bitmap) {
                            binding.ivfoto.setImageBitmap(bitmap)
                            binding.tvFoto.visibility = View.GONE
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val b = baos.toByteArray()
                            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                            //La base 64 se guarda en el arreglo de los datos
                            arrayDatos[8] = encodedImage

                        }

                        override fun onFailed(exception: Exception) {
                            //exception
                            Toast.makeText(DisenioTarjeta(), "Ocurrió un error cargando la foto.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }else{
            //No hay imagen
        }
    }
    //Logica para elegir la foto del logo
    val pickMedialogo = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null ){
            if(Build.VERSION.SDK_INT < 28){
                binding.tuLogoiv.setImageURI(uri)
                binding.tuLogoTv.visibility = View.GONE
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                arrayDatos[17] = encodedImage
            }else{
                binding.tuLogoiv.setImageURI(uri)
                binding.tuLogoTv.visibility = View.GONE
                val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                arrayDatos[17] = encodedImage
            }
        }else{
            //No hay imagen
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisenioTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = intent.getStringExtra(CreacionDeUsuario.usuario)

        //Listener en Image view del usuario que llama la logica para que elija su imagen
        binding.ivfoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        //LLamada para recibir los colores
        llamadaApi()
        //Llamada para recibir las imagenes de fondo
        llamadaApiImagenes()

        //Listener de la foto de fondo para que el usuario elija su imagen de las que llegaron en llamadaApiImagenes()
        binding.ivfondo.setOnClickListener {
            dialogImagenes.show(supportFragmentManager, "Selecciona una imagen.")
        }

        //Listener del textView para cambiar el nombre
        binding.editaNombreTv.setOnClickListener {

            val view = View.inflate(this@DisenioTarjeta, R.layout.dialog_nombre, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            //Muestra el dialog el nombre, y cambia el text view del nombre a lo que el usuario pone, si lo deja en blanco deja el text view igual
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)
            val et = view.findViewById<EditText>(R.id.nombreEt)

            btnGuardar.setOnClickListener {
                val nombre = et.text
                if(et.text.isEmpty()){
                    binding.editaNombreTv.text = "EDITA TU NOMBRE"
                }else{
                    binding.editaNombreTv.text = nombre
                    val delim = " "
                    val nombre = binding.editaNombreTv.text
                    val nombreCompleto = nombre.split(delim)
                    val primerNombre = nombreCompleto[0]
                    val apellido = nombreCompleto[1]
                    //Se guardan en el areglo de los datos
                    arrayDatos[1] = primerNombre
                    arrayDatos[2] = apellido
                }
                dialog.dismiss()
            }
        }
        //Listener del text view para cambiar el puesto hace lo mismo que el del nombre
        binding.editaPuestotv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.dialog_puesto, null)
            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view )
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.CENTER_HORIZONTAL
            window?.attributes = windowP

            val btnGuardar =view.findViewById<Button>(R.id.guardarPuesto)
            val et = view.findViewById<EditText>(R.id.puestoEt)

            btnGuardar.setOnClickListener {
                val puesto = et.text
                if(binding.editaPuestotv.text.isEmpty()){
                    binding.editaPuestotv.text = "Edita tu Puesto"

                }else {
                    binding.editaPuestotv.text = puesto
                }
                dialog.dismiss()
            }
        }

        //Listener de image view para poner tu logo
        binding.tuLogoiv.setOnClickListener{
            pickMedialogo.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        //Listener para añadir una red social
        binding.aniadeRed.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.layout_redes, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            val face = view.findViewById<ImageView>(R.id.btnface)
            val ig = view.findViewById<ImageView>(R.id.btnig)
            val twitter = view.findViewById<ImageView>(R.id.btntwitter)
            val linkedin = view.findViewById<ImageView>(R.id.btnlinkedin)
            val site = view.findViewById<ImageView>(R.id.btnSite)
            val linkedinC = view.findViewById<ImageView>(R.id.btnlinkedinC)
            val til = view.findViewById<TextInputLayout>(R.id.usuarioTil)
            val tv = view.findViewById<TextView>(R.id.redTv)
            val guardar = view.findViewById<Button>(R.id.guardarred)
            val et = view.findViewById<EditText>(R.id.Et)

            red = 0
            //Si el usuario da click en face, el text view del dialog cambia a ingressa tu usuario de face y le pone al valor de red = 1
            //porque 1 es el valor de facebook
            face.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.face)
                red = 1
            }
            //Si el usuario da click en instagram, el text view del dialog cambia a ingressa tu usuario de instagram y le pone al valor de red = 2
            //porque 2 es el valor de Instagram
            ig.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.ig)
                red = 2

            }
            //Si el usuario da click en twitter, el text view del dialog cambia a ingressa tu usuario de twitter y le pone al valor de red = 3
            //porque 3 es el valor de twitter, asi con todas las redes.
            twitter.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.Twitter)
                red = 3
            }
            linkedin.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.Link)
                red = 4
            }
            linkedinC.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.LinkC)
                red = 5
            }

            site.setOnClickListener{
                til.visibility = View.VISIBLE
                tv.visibility = View.VISIBLE
                tv.text = getString(R.string.site)
                red = 6
            }

            guardar.setOnClickListener{
                if (et.text.isEmpty()){
                    Toast.makeText(this, "Tienes que ingresar tu usuario.", Toast.LENGTH_SHORT).show()
                }else{
                    //Cuando se da click en el btn de guardar, se checa el valor de red y dependiando de eso es como guarda el usuario
                    //en el arreglo de redes y llama la funcion de addred con el valor 1 que es el de face. Asi con los casos de las demas redes.
                    when (red) {
                        1 ->{
                            dialog.dismiss()
                            //primero revisa si el valor de face esta ocupado y si no, lo llena con el usuario de face, se hace igual con los
                            //casos de los demas valores
                            if (redes[0] == "0"){
                                redes[0] = et.text.toString()
                                val face = et.text.toString()
                                //Manda a llamar añadir red con el valor 1 que es el da face
                                addRed(1)
                                //Se guardan los datos en su slot del arreglo de datos correspondiente
                                arrayDatos[9] = "https://www.facebook.com/$face"
                                dialog.dismiss()
                            }else{
                                Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                            }
                        }2->{
                        //Se hace lo mismo que con face pero en el caso que el usuario añada instagram
                        if (redes[1] == "0"){
                            redes[1] = et.text.toString()
                            val ig = et.text.toString()
                            addRed(2)
                            arrayDatos[12] = "https://www.instagram.com/$ig"
                            dialog.dismiss()
                        }else{
                            Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                        }

                    }3->{
                        //Se hace lo mismo que con face pero en el caso de que el usuario añada twitter
                        if (redes[2] == "0"){
                            redes[2] = et.text.toString()
                            val twitter = et.text.toString()
                            addRed(3)
                            arrayDatos[11] = "https://twitter.com/$twitter"
                            dialog.dismiss()
                        }else{
                            Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                        }

                    }4->{
                        //Se hace lo mismo que con face pero en el caso que el usuario añada linkedin
                        if (redes[3] == "0"){
                            dialog.dismiss()
                            val link = et.text.toString()
                            redes[3] = et.text.toString()
                            arrayDatos[10] = "https://www.linkedin.com/in/$link"
                            addRed(4)
                        }else{
                            Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                        }

                    }5->{
                        //Se hace lo mismo que con face pero en el caso que el usuario añada linkedin de compañia

                        if (redes[4] == "0"){
                            dialog.dismiss()
                            val linkc = et.text.toString()
                            redes[4] = et.text.toString()
                            arrayDatos[13] = "https://www.linkedin.com/company/$linkc"
                            addRed(5)
                        }else{
                            Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                        }
                    }6->{
                        //Se hace lo mismo que con face pero en el caso que el usuario añada

                        if (redes[5] == "0"){
                            dialog.dismiss()
                            redes[5] = et.text.toString()
                            addRed(6)
                        }else{
                            Toast.makeText(this, "Ya has añadido esa red.", Toast.LENGTH_SHORT).show()
                        }
                    }else->{
                    }
                    }
                }
            }
        }
        //listener para cambiar el email funciona igual que el del nombre y el del puesto
        binding.emailTv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.mail_dialog, null)
            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP
            val et = view.findViewById<EditText>(R.id.mailEt)
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)
            val btnMismo = view.findViewById<Button>(R.id.mismo)
            //El boton guardar checa que no este vacio el Et y si el mail es valido, si se cumplen las condiciones se cambia el
            // Textview de mail con lo que escribio el usuario
            btnGuardar.setOnClickListener {
                val email = et.text
                if(et.text.isEmpty()){
                    binding.emailTv.text = "Edita tu Email"
                }else {
                    if (Patterns.EMAIL_ADDRESS.matcher(et.text.toString()).matches()){
                        binding.emailTv.text = email
                        dialog.dismiss()
                    }else{
                        Toast.makeText(this, "La direccion de correo que ingresaste no es valida.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            //El boton mismo es para que el usuario ponga el mismo usuario que con el que hizo la cuenta
            btnMismo.setOnClickListener {
                binding.emailTv.text = usuario
                dialog.dismiss()
            }
        }
        //listener para cambiar el site, funciona igual que el de nombre y titulo
        binding.siteTv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.site_dialog, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            val et = view.findViewById<EditText>(R.id.siteEt)
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)

            btnGuardar.setOnClickListener {
                val site = et.text
                if(et.text.isEmpty()){
                    binding.siteTv.text = "Edita tu sitio web"

                }else {
                    binding.siteTv.text = site
                }
                dialog.dismiss()
            }
        }
        //listener para cambiar el celular, funciona igual que el de nombre y titulo
        binding.celularTv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.celular_dialog, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            val et = view.findViewById<EditText>(R.id.celularEt)
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)

            et.transformationMethod = null

            btnGuardar.setOnClickListener {
                val celular = et.text
                var celular2 = ""
                if(et.text.isEmpty()){
                    binding.celularTv.text = "Edita tu celular"
                }else {
                    val arr = celular.split("")
                    var count = 1
                    for (i in (arr.indices)){
                        if(count % 2 == 0){
                            celular2 = celular2.plus(" ").plus(arr[i])

                        }else{
                            celular2 = celular2.plus(arr[i])
                        }
                        count += 1
                    }
                }
                binding.celularTv.text = celular2
                arrayDatos[5] = celular2

                dialog.dismiss()
            }
        }
        //listener para cambiar el telefono, funciona igual que el del nombre y titulo
        binding.telefonoTv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.telefono_dialog, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            val et = view.findViewById<EditText>(R.id.telefonoEt)
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)

            et.transformationMethod = null

            btnGuardar.setOnClickListener {
                val telefono = et.text
                var telefono2 = ""
                if(et.text.isEmpty()){
                    binding.telefonoTv.text = "Edita tu telefono"

                }else {
                    val arr = telefono.split("")
                    var count = 1
                    for (i in (arr.indices)){
                        if(count % 2 == 0){
                            telefono2 = telefono2.plus(" ").plus(arr[i])
                            println(telefono2)
                            println(count)

                        }else{
                            telefono2 = telefono2.plus(arr[i])
                            println(telefono2)
                            println(count)
                        }
                        count += 1
                    }
                }
                binding.telefonoTv.text = telefono2
                arrayDatos[4] = telefono2
                dialog.dismiss()
            }
        }
        //listener para cambiar la dirección, funciona igual que el del nombre y titulo
        binding.direccionTv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.direccion_dialog, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.TOP
            window?.attributes = windowP

            val et = view.findViewById<EditText>(R.id.direccionEt)
            val btnGuardar = view.findViewById<Button>(R.id.guardarnombre)

            btnGuardar.setOnClickListener {
                val direccion = et.text
                if(et.text.isEmpty()){
                    binding.direccionTv.text = "Edita tu dirección"

                }else {
                    binding.direccionTv.text = direccion
                    arrayDatos[6] = direccion.toString()
                }
                dialog.dismiss()
            }
        }
        binding.backgroundAcTarjeta.setOnClickListener {
            dialogColores.show(supportFragmentManager, "Selecciona una combinación de colores.")
        }
        //Listener de la descripcion de la empresa, funciona igual que el del nombre y titulo
        binding.infoEmpresatv.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.dialog_descripcion_empresa, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.CENTER_HORIZONTAL
            window?.attributes = windowP

            val btnGuardar =view.findViewById<Button>(R.id.guardarinfo)
            val et = view.findViewById<EditText>(R.id.infoEt)

            btnGuardar.setOnClickListener {
                val puesto = et.text
                if (et.text.isEmpty()){
                    binding.infoEmpresatv.text = getString(R.string.descripcion)
                }else{
                    binding.infoEmpresatv.text = puesto
                }
                dialog.dismiss()
            }
        }
        //boton para añadir una certificacion, abre una instanca del dialog donde se ven las certificaciones disponibles
        binding.aniadeCert.setOnClickListener {
            dialogCertificaciones.show(supportFragmentManager, "Selecciona una certificación.")
        }
        //para quitar certificaciones una vez que fueron añadidas
        binding.layoutCertificaciones.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.quitar_cert_dialog, null)

            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.CENTER_HORIZONTAL
            window?.attributes = windowP
            val btnSi = view.findViewById<Button>(R.id.si)
            val btnNo = view.findViewById<Button>(R.id.no)

            btnSi.setOnClickListener {
                binding.layoutCertificaciones.removeAllViews()
                binding.layoutCertificaciones2.removeAllViews()
                dialog.dismiss()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
        }
        //para quitar certificaciones una vez que fueron añadidas
        binding.layoutCertificaciones2.setOnClickListener {
            val view = View.inflate(this@DisenioTarjeta, R.layout.quitar_cert_dialog, null)
            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.CENTER_HORIZONTAL
            window?.attributes = windowP
            val btnSi = view.findViewById<Button>(R.id.si)
            val btnNo = view.findViewById<Button>(R.id.no)

            btnSi.setOnClickListener {
                binding.layoutCertificaciones.removeAllViews()
                binding.layoutCertificaciones2.removeAllViews()
                dialog.dismiss()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
        }
        binding.guardarTarjeta.setOnClickListener {
            Toast.makeText(this, "La tarjeta ha sido guardada exitosamente", Toast.LENGTH_SHORT).show()
            for (i in arrayDatos){
                println(i)
            }
        }
    }

    private fun addRed(red: Int){
        //Se suma uno al numero de redes cada vez que se añade una red
        numRdes += 1
        //se crea una vista de tipo redes
        val view = layoutInflater.inflate(R.layout.redes, null, false)
        val iv = view.findViewById<ImageView>(R.id.iv)
        //Se le asigna un click listener para que pueda ser removida.
        iv.setOnClickListener{
            val vista = View.inflate(this@DisenioTarjeta, R.layout.quitar_red, null)
            val builder = AlertDialog.Builder(this@DisenioTarjeta)
            builder.setView(vista)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val windowP = window?.attributes
            windowP?.gravity = Gravity.CENTER_HORIZONTAL
            window?.attributes = windowP

            val si = vista.findViewById<Button>(R.id.si)
            val no = vista.findViewById<Button>(R.id.no)

            no.setOnClickListener {
                dialog.dismiss()
            }
            si.setOnClickListener {
                removeRed(view)
                dialog.dismiss()
            }
        }
        //Se agrega la vista de tipo red al layout, y dependiendo del numero recibido,
        // y se le añade una descripcion para tener un indicador de cual es
        binding.layoutRedes.addView(view)
        when(red){
            1-> {
                iv.setImageResource(R.drawable.face)
                iv.contentDescription = getString(R.string.face)
            }2->{
            iv.setImageResource(R.drawable.ig)
            iv.contentDescription = getString(R.string.ig)
        }3->{
            iv.setImageResource(R.drawable.twitter)
            iv.contentDescription = getString(R.string.Twitter)

        }4->{
            iv.setImageResource(R.drawable.linkedin)
            iv.contentDescription = getString(R.string.Link)
        }5->{
            iv.setImageResource(R.drawable.linkedin_corporativo)
            iv.contentDescription = getString(R.string.LinkC)
        }6->{
            iv.setImageResource(R.drawable.web)
            iv.contentDescription = getString(R.string.site)
        }else->{

        }
        }
        for(i in redes){
            println(i)
        }
    }
    //Funcioon que se llama cuando le das click a una red y le pones eliminar
    private fun removeRed(view: View){
        val iv = view.findViewById<ImageView>(R.id.iv)
        //Checa cual es la descripcion y basandose en eso quita la red del slot del arreglo de redes
        if(iv.contentDescription == getString(R.string.face)){
            redes[0] = "0"
            arrayDatos[9] = ""
        }else if (iv.contentDescription == getString(R.string.ig)){
            redes[1] = "0"
            arrayDatos[12] = ""
        }else if(iv.contentDescription == getString(R.string.Twitter)){
            redes[2] = "0"
            arrayDatos[11] = ""
        }else if(iv.contentDescription == getString(R.string.Link)){
            redes[3] = "0"
            arrayDatos[10] = ""
        }else if(iv.contentDescription== getString(R.string.LinkC)){
            redes[4] = "0"
            arrayDatos[13] = ""
        }else if(iv.contentDescription== getString(R.string.site)){
            redes[5] = "0"
        }
        binding.layoutRedes.removeView(view)
        numRdes -= 1
        for(i in redes){
            println(i)
        }
    }

    private fun llamadaApi() {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://www.activecontrol.mx/kontaktoApi/home/ListaTemas")

            val conn = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpsURLConnection
            conn.doInput = true
            conn.setRequestProperty("Content-Type" , "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val datos = StringBuffer()
            BufferedReader(InputStreamReader(withContext(Dispatchers.IO) {
                conn.getInputStream()
            })).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    println(line)
                    datos.append(line)
                }
            }
            val respuesta = datos.toString()
            println(respuesta)
            val array = JSONArray(respuesta)
            for(i in (0 until array.length() )){
                val objeto = array.getJSONObject(i)
                val nombre = objeto.getString("Name")
                val color1 = objeto.getString("Color1")
                val color2 = objeto.getString("Color2")
                val color3 = objeto.getString("Color3")
                val color4 = objeto.getString("Color4")
                val color5 = objeto.getString("Color5")
                val color6 = objeto.getString("Color6")
                listaColores.add(i, colores(nombre, color1, color2, color3, color4, color5,color6))
            }
        }
        //runBlocking { job.join() }
    }
    private fun llamadaApiImagenes(){
        val job = CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://www.activecontrol.mx/kontaktoApi/home/ListaFondos")

            val conn = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpsURLConnection
            conn.doInput = true
            conn.setRequestProperty("Content-Type" , "application/json")
            conn.setRequestProperty("Accept", "application/json")
            val datos = StringBuffer()
            BufferedReader(InputStreamReader(withContext(Dispatchers.IO) {
                conn.getInputStream()
            })).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    println(line)
                    datos.append(line)
                }
            }
            val respuesta = datos.toString()
            println(respuesta)
            val array = JSONArray(respuesta)
            println(array)
            for (i in (0 until array.length())){
                val imagen = Glide.with(this@DisenioTarjeta).asBitmap().load(array[i].toString()).submit().get()
                listaImagenes.add(i, imagenes(imagen))
            }
        }
        runBlocking { job.join() }
    }


    private fun guardarTarjeta(){
        val job = CoroutineScope(Dispatchers.IO).launch {
            val objeto = JSONObject()
            println(objeto)
            val url = URL("http://actinseguro.com/booking/abkcom007.aspx")
            val postData = objeto.toString()

            val conn = url.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Content-Type" , "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Content-Length", postData.length.toString())

            val outputStreamWriter = OutputStreamWriter(conn.getOutputStream())
            outputStreamWriter.write(postData)
            outputStreamWriter.flush()

            val datos = StringBuffer()
            BufferedReader(InputStreamReader(conn.getInputStream())).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    println(line)
                    datos.append(line)
                }
            }
            val respuesta = datos.toString()
            val jsonRespuesta = JSONObject(respuesta)
            val respuestaArray = jsonRespuesta.getJSONArray("RESPONSE")
            val objetoArray = respuestaArray.getJSONObject(0)
        }
        runBlocking { job.join() }
    }
}