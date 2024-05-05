package com.example.hamburguerclicker

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hamburguerclicker.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var _this: AppCompatActivity

    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference("partida")

    lateinit var layoutPartidas: LinearLayout
    companion object {
        var partidaActual = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (this as AppCompatActivity).supportActionBar!!.hide()

        //esto va a hacer que suene la musica de fondo y que se va a estar repitiendo
        reproducirSonido("vaciopruebas", true)
        HomeFragment.sonidoFondo.start()

        setContentView(R.layout.inicio)
        layoutPartidas = findViewById(R.id.layoutPartidas)

        _this = this
        iniciarPartidas()
    }

    fun reproducirSonido(nombreAudio: String, repetirse: Boolean = false) {
        // variable para obtener el nombre del paquete
        val nombrePaquete = packageName
        //Esto me identifica el recurso donde este ubicado
        val recurso = resources.getIdentifier(
            nombreAudio, "raw", nombrePaquete
        )

            //Esto hace una referencia del sonido en memoria
            HomeFragment.sonidoFondo = MediaPlayer.create(this, recurso)

            //Aqui le indico si se va a estar repitiendo o no
            HomeFragment.sonidoFondo.isLooping = repetirse

            //Esto determina el volumen del sonido
            HomeFragment.sonidoFondo.setVolume(1f, 1f)

            //Esto es para que no se repita el sonido y se solape
            if(!HomeFragment.sonidoFondo.isPlaying){
                HomeFragment.sonidoFondo.start()
            }
    }

    private fun iniciarPartidas() {
        //Reseteo del layout de partidas
        layoutPartidas.removeAllViews()
        mDatabase.get()
            .addOnSuccessListener { partidas ->
            var contador = 0
            partidas.children.forEach { partida ->
                crearBotones(partida.key.toString(), false)
                contador++
            }
            for (i in contador..2) {
                crearBotones("Crear nueva partida", true)
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error obteniendo partida", it)
        }
    }

    private fun continuarPartida(nombrePartida: String) {
        //Guardamos el nombre de la partida elegida en una variable
        partidaActual= "partida/$nombrePartida"

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_tienda, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun crearBotones(nombrePartida: String, nuevaPartida: Boolean) {
        val color = ContextCompat.getColor(_this, R.color.botonCrearPartida)
        val colorBasura = ContextCompat.getColor(_this,R.color.botonesBasura)

        val colorStateList = ColorStateList.valueOf(color)
        val colorBtnBorrar = ColorStateList.valueOf(colorBasura)

        (layoutPartidas as? LinearLayout)?.orientation = LinearLayout.VERTICAL

        val fila = LinearLayout(_this)
        fila.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        fila.gravity = Gravity.CENTER

        //Esto es para ponerle paramestros de layout a cada boton
        val parametrosLayout = LinearLayout.LayoutParams(
            0,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )

        // esto es para definirle un margin a los botones
        val marginLayoutParams = parametrosLayout as ViewGroup.MarginLayoutParams

        val btnPartida = Button(_this)
        btnPartida.text = nombrePartida
        btnPartida.setTextColor(Color.WHITE)
        btnPartida.typeface = Typeface.create("sans-serif", Typeface.BOLD)
        btnPartida.backgroundTintList = colorStateList
        btnPartida.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        btnPartida.setPadding(0,50,0,50)
        btnPartida.layoutParams = marginLayoutParams
        btnPartida.layoutParams = parametrosLayout
        btnPartida.setOnClickListener {
            if (!nuevaPartida) {
                continuarPartida(nombrePartida)
            } else {
                crearpartida()
            }
        }
        fila.addView(btnPartida)

        if (!nuevaPartida) {
            val btnEliminar = Button(_this)
            btnEliminar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.basura, 0, 0, 0)
            btnEliminar.gravity = Gravity.CENTER
            btnEliminar.backgroundTintList = colorBtnBorrar
            btnEliminar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            btnEliminar.setOnClickListener {
                borrarPartida(nombrePartida)
            }
            fila.addView(btnEliminar)
        }

        layoutPartidas.addView(fila)
    }


    private fun crearpartida() {
        val nuevaPartida = Partida()
        val editText = EditText(_this)
        val dialog = AlertDialog.Builder(_this)
            .setTitle("Nombre partida")
            .setMessage("Introduzca el nombre de la partida:")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, which ->
                val nombrePartida = editText.text.toString()
                mDatabase.child(nombrePartida).setValue(nuevaPartida)
                iniciarPartidas()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun borrarPartida(nombrePartida: String) {
        mDatabase.child(nombrePartida).removeValue()
        iniciarPartidas()
    }
}