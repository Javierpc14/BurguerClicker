package com.example.hamburguerclicker.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentHomeBinding
import com.example.hamburguerclicker.modelo.Logro
import com.example.hamburguerclicker.modelo.Tienda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    // Variables para controlar la vista
    private var _binding: FragmentHomeBinding? = null
    private lateinit var txtValorPeso: TextView
    private lateinit var unidad: TextView

    // variable para gestionar la imagen de la hamburguesa
    private lateinit var imgHamburguesa: ImageView

    // Variables para controlar el peso
    var pesoTotal: Double = 0.0
    private var pesoPantalla: Double = 0.0
    var pesoPorClick :Double = 0.0

    // variable para obtener el contexto del fragment
    private lateinit var contexto: Context

    // variables para controlar las tiendas
    lateinit var tiendas: ArrayList<Tienda>

    // variables de los logros
    lateinit var logros: ArrayList<Logro>

    //Variable para controlar reproduccion del sonido
    private var sonidoEnReproduccion=false

    companion object {
        // Variable estatica para contral el temporizador de ingresos pasivos
        var timer: Timer? = null

        //Esta variable me ayuda a saver si se tiene que poner o no el sonido
        var ponerSonido = true
        //Estas variables son para la reproduccion del sonido
        var reproduccionSonido: MediaPlayer? =null
        //Este es para el sonido del fondo
        lateinit var sonidoFondo: MediaPlayer
    }

    // lista que contiene todos los sonidos de masticar para cuando se pulsa la hamburguesa
    private val sonidosMasticar = listOf("morder1", "morder2", "morder3", "morder4", "morder5", "morder6", "morder7")

    // variable que contiene el peso inicial de la partida
    private var unidadPeso = "Mili Gramos"


    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // variable para obtener el contexto
        contexto = requireContext()

        // hago que al cargar la clase suene este sonido de la clase Home como si se le hubiera dado al boton del menu de navegacion
        reproducirSonido("morderhamburguesa")

        txtValorPeso = root.findViewById(R.id.txtValorPeso)

        // variable para gestionar la imagen de la hamburguesa
        imgHamburguesa = root.findViewById(R.id.hamburguesa)

        unidad = root.findViewById(R.id.unidadPeso)
        txtValorPeso.text = String.format("%.2f", pesoTotal)

        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()

                pesoTotal = value?.pesoTotal as Double
                comprobarUnidad()

                pesoPorClick= value?.pesoPorClick as Double

                //Array para las tiendas
                tiendas =value?.tiendas as ArrayList<Tienda>

                //Array donde se guardan los logros
                logros=value?.logros as ArrayList<Logro>

                cambiarImagenHamburguesa()

                if (timer == null) {
                    iniciarIncrementoPasivo()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        //Evento pulsar hamburguesa

        imgHamburguesa.setOnClickListener() {
            pulsarHamburguesa()
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    private fun pulsarHamburguesa(){
        //Incrementamos el peso total añadiendole las mejoras que poseamos
        pesoTotal+= pesoPorClick

        //Reproducimos un sonido de la lista
        sonidosMasticarAleatorios()

        //Cambiamos valor del peso en la base de datos
        escribirDatos()
    }

    // esta funcion se encarga de mezclar los sonidos de masticar para que sean aleatorios
    private fun sonidosMasticarAleatorios(){
        val i = sonidosMasticar.indices.random()
        val sonidoAleatorio = sonidosMasticar[i]
        reproducirSonido(sonidoAleatorio)
    }


    private fun reproducirSonido(nombreAudio: String) {
        // variable para obtener el nombre del paquete
        val nombrePaquete = requireContext().packageName
        //Esto me identifica el recurso donde este ubicado
        val recurso = resources.getIdentifier(
            nombreAudio, "raw", nombrePaquete
        )

        if (sonidoEnReproduccion) {
            return
        }
        reproduccionSonido = MediaPlayer.create(contexto,recurso)

        reproduccionSonido?.setOnCompletionListener { mediaPlayer ->
            // Liberar el MediaPlayer después de que termine el sonido
            mediaPlayer.release()
            // Actualizar el estado de reproducción
            sonidoEnReproduccion = false
        }
        reproduccionSonido?.start()
        sonidoEnReproduccion=true
    }

    private fun escribirDatos() {
        val pesoBase = database.getReference(MainActivity.partidaActual + "/pesoTotal")
        pesoBase.setValue(pesoTotal)

        val logrosBase =database.getReference(MainActivity.partidaActual + "/logros")
        logrosBase.setValue(logros)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun comprobarUnidad() {
        when (pesoTotal) {
            in 0.0..999.9 -> {
                pesoPantalla = pesoTotal
                unidadPeso = getString(R.string.pesoMg)
            }

            in 1000.0..999999.9 -> {
                pesoPantalla = pesoTotal / 1000
                unidadPeso = getString(R.string.pesoGramos)
            }

            in 1000000.0..999999999.9 -> {
                pesoPantalla = pesoTotal / 1000000
                unidadPeso = getString(R.string.pesoKilos)
            }

            in 1000000000.0..Double.MAX_VALUE -> {
                pesoPantalla = pesoTotal / 1000000000
                unidadPeso = getString(R.string.pesoTon)
            }
        }
        txtValorPeso.text = String.format("%.2f", pesoPantalla)
        unidad.text = unidadPeso
    }

    private fun iniciarIncrementoPasivo() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                incrementoPasivo()
            }
        }, 0, 1000)
    }

    private fun mostrarMensaje(mensaje: String) {
        //Verificamos que la llamada se haga desde el hilo principal, porque sino al intentar llamarla desde el timer da error
        activity?.runOnUiThread {
            Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    private fun logroDesbloqueado(idLogro:Int,mensaje: String){
        logros[idLogro].conseguido = true

        //mostrar alerta y registrarla en la base, para que no vuelva a aparecer
        mostrarMensaje(mensaje)

        //reproducir el sonido de el logro
        reproducirSonido("logros")

    }

    private fun desbloqueoLogros() {
        if (!logros[0].conseguido && pesoTotal >= 100) {
            // desbloquear el logro cuando se consiguen 100 mg
            logroDesbloqueado(0,"Logro desbloqueado \n Alcanza los 100 mg")
        }
        if (!logros[1].conseguido && tiendas[0]!!.total >= 20) {
            // desbloquear el logro 2 cuando se compran 20 panaderias
            logroDesbloqueado(1,"Logro desbloqueado \n Panadero maestro")
        }
        if (!logros[2].conseguido && pesoTotal >= 10000) {
            // desbloquear el logro 3 cuando se alcanzan los 10 gramos
            logroDesbloqueado(2,"Logro desbloqueado \n Alcanza los 10 g")
        }
        if (!logros[3].conseguido &&tiendas[1]!!.total >= 20) {
            // desbloquear el logro 4 cuando se compran 20 carnicerias
            logroDesbloqueado(3,"Logro desbloqueado \n Carnicero maestro")
        }
        if (!logros[4].conseguido &&pesoTotal >= 700000) {
            // desbloquear el logro 5 cuando se alcanzan los 700 gramos
            logroDesbloqueado(4,"Logro desbloqueado \n Alcanza los 700 gramos")
        }
        if (!logros[5].conseguido &&tiendas[2]!!.total >= 20) {
            // desbloquear el logro 6 cuando se compran 20 queserias
            logroDesbloqueado(5,"Logro desbloqueado \n Quesero maestro")
        }
        if (!logros[6].conseguido &&pesoTotal >= 20000000) {
            // desbloquear el logro 7 cuando se alcanzan los 20 kg
            logroDesbloqueado(6, "Logro desbloqueado \n ALcanza los 20 Kg")
        }
        if (!logros[7].conseguido &&tiendas[3]!!.total >= 20) {
            // desbloquear el logro 8 cuando se compran 20 lechugas
            logroDesbloqueado(7,"Logro desbloqueado \n Lechuga maestra")
        }
        if (!logros[8].conseguido &&pesoTotal >= 800000000) {
            // desbloquear el logro 9 cuando se alcanzan los 800 kg
            logroDesbloqueado(8,"Logro desbloqueado \n Alcanza los 800 Kg")
        }
        if (!logros[9].conseguido &&tiendas[4]!!.total >= 20) {
            // desbloquear el logro 10 cuando se compran 20 huertos
            logroDesbloqueado(9,"Logro desbloqueado \n Huerto maestro")
        }
        if (!logros[10].conseguido &&pesoTotal >= 140000000000) {
            // desbloquear el logro 11 cuando se alcanzan las 140T
            logroDesbloqueado(10,"Logro desbloqueado \n Alcanza las 140 T")
        }
        if (!logros[11].conseguido &&tiendas[5]!!.total >= 20) {
            // desbloquear el logro 12 cuando se compran 20 beicones
            logroDesbloqueado(11,"Logro desbloqueado \n Beicon maestro")
        }
//        if (!logros[12].conseguido &&pesoTotal > 0) {
//            // desbloquear el logro 12 cuando se compran 20 beicones
//            logroDesbloqueado(12,"Logro desbloqueado \n Primer miligramo")
//        }
    }

    fun incrementoPasivo() {
        tiendas.forEach{tienda->
            pesoTotal+= tienda.total* tienda.aportePasivo
        }
        desbloqueoLogros()
        escribirDatos()
    }

    private fun cambiarImagenHamburguesa() {
        if (tiendas[5]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa6)
        } else if (tiendas[4]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa5)
        } else if (tiendas[3]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa4)
        } else if (tiendas[2]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa3)
        } else if (tiendas[1]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa2)
        } else if (tiendas[0]!!.total >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa1)
        }
    }
}