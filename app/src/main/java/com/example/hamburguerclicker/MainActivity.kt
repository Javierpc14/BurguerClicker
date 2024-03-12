package com.example.hamburguerclicker

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
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
import androidx.core.view.marginLeft
import androidx.core.view.marginRight

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var _this: AppCompatActivity

    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference("partida")

    lateinit var btnCrear: Button

    lateinit var layout: LinearLayout
    companion object {
        var partidaActual = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_notifications)
        layout = findViewById<LinearLayout>(R.id.layoutPartidas)



        _this = this
        var value: HashMap<String, Object>?
        iniciarPartidas()
    }

    private fun iniciarPartidas() {
        layout.removeAllViews()
        mDatabase.get().addOnSuccessListener { partidas ->
            var contador = 0
            partidas.children.forEach { partida ->
                crearBotones(partida.key.toString(), false)
                contador++;
            }
            for (i in contador..2) {
                crearBotones("Crear nueva partida", true)
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error obteniendo partida", it)
        }
    }

    private fun continuarPartida(nombrePartida: String) {
        partidaActual=nombrePartida
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun crearBotones(nombrePartida: String, nuevaPartida: Boolean) {
        val color = ContextCompat.getColor(_this, R.color.botonCrearPartida)
        val colorStateList = ColorStateList.valueOf(color)
        val colorBtnBorrar = ColorStateList.valueOf(Color.TRANSPARENT)

        (layout as? LinearLayout)?.orientation = LinearLayout.VERTICAL

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

        layout.addView(fila)
    }


    private fun crearpartida() {
        var nuevaPartida = Partida()
        val editText = EditText(_this)
        val dialog = AlertDialog.Builder(_this)
            .setTitle("Nombre partida")
            .setMessage("Introduzca el nombre de la partida:")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, which ->
                val nombrePartida = editText.text.toString()
                mDatabase.child("$nombrePartida").setValue(nuevaPartida)
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