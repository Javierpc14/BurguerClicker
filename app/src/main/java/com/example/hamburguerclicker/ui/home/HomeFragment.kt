package com.example.hamburguerclicker.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.hamburguerclicker.modelo.Alerta
import com.example.hamburguerclicker.modelo.Logro
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
    lateinit var txtValorPeso: TextView
    lateinit var unidad: TextView

    // variable para gestionar la imagen de la hamburguesa
    lateinit var imgHamburguesa: ImageView

    // Variables para controlar el peso
    var pesoTotal: Double = 0.0
    var pesoPantalla: Double = 0.0

    // variable para obtener el contexto del fragment
    lateinit var contexto: Context

    // variables para controlar las tiendas
    var totalPanaderias = 0
    var totalCarnicerias = 0
    var totalQueserias = 0
    var totalLechugas = 0
    var totalHuertos = 0
    var totalBeicones = 0

    // variables de los logros
    lateinit var logros: ArrayList<Logro>

    // variables de las alertas
    lateinit var alertas: ArrayList<Alerta>


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
    val sonidosMasticar = listOf("morder1", "morder2", "morder3", "morder4", "morder5", "morder6", "morder7")

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
        reproducirSonido("morder1")

        txtValorPeso = root.findViewById(R.id.txtValorPeso)

        // variable para gestionar la imagen de la hamburguesa
        imgHamburguesa = root.findViewById(R.id.hamburguesa)

        unidad = root.findViewById(R.id.unidadPeso)
        txtValorPeso.setText(String.format("%.2f", pesoTotal))

        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()

                pesoTotal = value?.pesoTotal as Double
                comprobarUnidad()

                totalPanaderias = value?.tiendas?.panaderias as Int
                totalCarnicerias = value?.tiendas?.carnicerias as Int
                totalQueserias = value?.tiendas?.queserias as Int
                totalLechugas = value?.tiendas?.lechugas as Int
                totalHuertos = value?.tiendas?.huertos as Int
                totalBeicones = value?.tiendas?.beicones as Int

                //
                logros=value?.logros as ArrayList<Logro>


                // variables de las alertas para obtener el valor de ellas de la base de datos
                alertas = value?.alertas as ArrayList<Alerta>

                cambiarImagenHamburguesa()


                if (timer == null) {
                    iniciarIncrementoPasivo()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        imgHamburguesa.setOnClickListener() {
            pesoTotal++
            sonidosMasticarAleatorios()
            escribirDatos(pesoTotal)
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    // esta funcion se encarga de mezclar los sonidos de masticar para que sean aleatorios
    fun sonidosMasticarAleatorios(){
        val i = sonidosMasticar.indices.random()
        val sonidoAleatorio = sonidosMasticar[i]
        reproducirSonido(sonidoAleatorio)
    }

    //Este metodo permite habilitar o inhabilitar el sonido de fondo
//    fun musicaFondo(){
//        if(ponerSonido){
//            sonidoFondo.pause()
//            //btnSonido.setImageResource(android.R.drawable.ic_lock_silent_mode)
//        }else{
//            sonidoFondo.start()
//            //btnSonido.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
//        }
//
//        //Esto es para determinar si se esta escuchando se ejecuta el else si no es que no se escucha y se ejecuta el if
//        ponerSonido = !ponerSonido
//    }
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

    private fun escribirDatos(peso: Double) {
        val pesoBase = database.getReference(MainActivity.partidaActual + "/pesoTotal")
        pesoBase.setValue(peso)

        val logrosBase =database.getReference(MainActivity.partidaActual + "/logros")
        logrosBase.setValue(logros)

        val alertasBase =database.getReference(MainActivity.partidaActual + "/alertas")
        alertasBase.setValue(alertas)

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
        txtValorPeso.setText(String.format("%.2f", pesoPantalla))
        unidad.setText(unidadPeso)
    }

    private fun iniciarIncrementoPasivo() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                incrementoPasivo()
                desbloqueoLogros()
            }
        }, 0, 1000)
    }

    private var toast: Toast? = null
    private val handler = Handler(Looper.getMainLooper())
    private fun mostrarMensaje(mensaje: String) {
        toast?.cancel()
        // requireActivity().runOnUiThread, esto se encarga de mostrar el Toast
        // en el hilo principal para que no salte un error de que el hilo en el que se esta
        // intentando mostrar el Toast no tiene configurado un bucle de mensajes.
        //requireActivity().runOnUiThread {

            handler.post {
                toast = Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT)
            }

       //}
        toast?.show()
    }

    private fun desbloqueoLogros() {
        if (!logros[0].conseguido && pesoTotal >= 100) {
            // desbloquear el logro cuando se consiguen 100 mg
            logros[0].conseguido = true

            //mostrar alerta y registrarla en la base, para que no vuelva a aparecer
            mostrarMensaje("Logro desbloqueado \n Alcanza los 100 mg")
            alertas[0].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[1].conseguido && totalPanaderias >= 20) {
            // desbloquear el logro 2 cuando se compran 20 panaderias
            logros[1].conseguido = true
            mostrarMensaje("Logro desbloqueado \n Panadero maestro")
            alertas[1].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[2].conseguido && pesoTotal >= 10000) {
            // desbloquear el logro 3 cuando se alcanzan los 10 gramos
            logros[2].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Alcanza los 10 g")
            alertas[2].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[3].conseguido &&totalCarnicerias >= 20) {
            // desbloquear el logro 4 cuando se compran 20 carnicerias
            logros[3].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Carnicero maestro")
            alertas[3].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[4].conseguido &&pesoTotal >= 700000) {
            // desbloquear el logro 5 cuando se alcanzan los 700 gramos
            logros[4].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Alcanza los 700 gramos")
            alertas[4].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[5].conseguido &&totalQueserias >= 20) {
            // desbloquear el logro 6 cuando se compran 20 queserias
            logros[5].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Quesero maestro")
            alertas[5].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[6].conseguido &&pesoTotal >= 20000000) {
            // desbloquear el logro 7 cuando se alcanzan los 20 kg

            logros[6].conseguido = true
            mostrarMensaje("Logro desbloqueado \n ALcanza los 20 Kg")
            alertas[6].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[7].conseguido &&totalLechugas >= 20) {
            // desbloquear el logro 8 cuando se compran 20 lechugas
            logros[7].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Lechuga maestra")
            alertas[7].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[8].conseguido &&pesoTotal >= 800000000) {
            // desbloquear el logro 9 cuando se alcanzan los 800 kg
            logros[8].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Alcanza los 800 Kg")
            alertas[8].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[9].conseguido &&totalHuertos >= 20) {
            // desbloquear el logro 10 cuando se compran 20 huertos
            logros[9].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Huerto maestro")
            alertas[9].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[10].conseguido &&pesoTotal >= 140000000000) {
            // desbloquear el logro 11 cuando se alcanzan las 140T
            logros[10].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Alcanza las 140 T")
            alertas[10].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")
        }
        if (!logros[11].conseguido &&totalBeicones >= 20) {
            // desbloquear el logro 12 cuando se compran 20 beicones
            logros[11].conseguido = true

            mostrarMensaje("Logro desbloqueado \n Beicon maestro")
            alertas[11].mostrada = true

            //reproducir el sonido de el logro
            reproducirSonido("logros")

        }
    }

    fun incrementoPasivo() {
        pesoTotal += totalPanaderias * 7.5 + totalCarnicerias * 50 + totalQueserias * 6500 + totalLechugas * 115000 + totalHuertos * 20000000 + totalBeicones * 50000000
        escribirDatos(pesoTotal)
    }

    private fun cambiarImagenHamburguesa() {
        if (totalBeicones >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa6)
        } else if (totalHuertos >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa5)
        } else if (totalLechugas >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa4)
        } else if (totalQueserias >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa3)
        } else if (totalCarnicerias >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa2)
        } else if (totalPanaderias >= 1) {
            imgHamburguesa.setImageResource(R.drawable.hamburguesa1)
        }
    }
}