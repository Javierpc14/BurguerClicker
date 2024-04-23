package com.example.hamburguerclicker.ui.logros

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentLogrosBinding
import com.example.hamburguerclicker.modelo.Logro
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.ui.home.HomeFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database

class LogrosFragment : Fragment() {

    private var _binding: FragmentLogrosBinding? = null

    lateinit var layoutLogros: LinearLayout

    // variable para obtener el contexto del fragment
    lateinit var contexto:Context

    // Variable para el contador de los logros
    lateinit var contador:TextView
    // variable que va sumando 1 cuando se desbloquea un logro
    var contadorLogros: Int = 0
    // Variable para lamacenar todos los logros
    lateinit var logros: ArrayList<Logro>

    // Variables para ir controlando el peso
    var pesoTotal: Double = 0.0

    // Variables para controlar el total de tiendas
    var totalPan: Int = 0
    var totalCarne: Int = 0
    var totalQueso: Int = 0
    var totalLechuga: Int = 0
    var totalTomate: Int = 0
    var totalBacon: Int = 0




    private val binding get() = _binding!!

    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val logrosViewModel =
            ViewModelProvider(this).get(LogrosViewModel::class.java)

        _binding = FragmentLogrosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // variable para obtener el contexto
        contexto = requireContext()

        // hago que al cargar la clase suene este sonido de la tclase logros como si se le hubiera dado al boton del menu de navegacion
        reproducirSonido("logroboton")

        layoutLogros = root.findViewById<LinearLayout>(R.id.layoutLogros)

        contexto=requireContext()

        // Variable para el contador de los logros
        contador = root.findViewById(R.id.txtcontador)

        //Leer el peso de la base de datos
        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()
                pesoTotal = value?.pesoTotal as Double

                totalPan = value?.tiendas?.panaderias as Int
                totalCarne = value?.tiendas?.carnicerias as Int
                totalQueso = value?.tiendas?.queserias as Int
                totalLechuga = value?.tiendas?.lechugas as Int
                totalTomate = value?.tiendas?.huertos as Int
                totalBacon = value?.tiendas?.beicones as Int

                logros = value?.logros as ArrayList<Logro>

                comprobarLogros()

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
        val textView: TextView = binding.textDashboard
        logrosViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var sonidoEnReproduccion=false
    private fun reproducirSonido(nombreAudio: String, repetirse: Boolean = false) {
        // variable para obtener el nombre del paquete
        val nombrePaquete = requireContext().packageName
        //Esto me identifica el recurso donde este ubicado
        val recurso = resources.getIdentifier(
            nombreAudio, "raw", nombrePaquete
        )

        if (sonidoEnReproduccion) {
            return
        }
        HomeFragment.reproduccionSonido = MediaPlayer.create(contexto,recurso)

        HomeFragment.reproduccionSonido?.setOnCompletionListener { mediaPlayer ->
            // Liberar el MediaPlayer después de que termine el sonido
            mediaPlayer.release()
            // Actualizar el estado de reproducción
            sonidoEnReproduccion = false
        }
        HomeFragment.reproduccionSonido?.start()
        sonidoEnReproduccion=true
    }

    fun  comprobarLogros() {
        //Reseteamos el layout de logros
        layoutLogros.removeAllViews()

        contadorLogros = 0
        //Variable para añadir logros que aun no esten obtenidos
        val logroNoObtenido=
            Logro( "logro1"
                , R.drawable.logrooculto,
                "???",
                "?????")

        logros.forEach{logro->
            if(logro.conseguido){
                layoutLogros.addView(LogroLayout(contexto,logro))
                contadorLogros++
            }
            else{
                layoutLogros.addView(LogroLayout(contexto,logroNoObtenido))
            }
        }
        contador.setText("$contadorLogros / 12")
    }
}
