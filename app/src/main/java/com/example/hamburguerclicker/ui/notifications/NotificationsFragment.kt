package com.example.hamburguerclicker.ui.notifications

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentNotificationsBinding
import com.example.hamburguerclicker.ui.home.HomeFragment


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    //variables para los botones de la vista Menu
    lateinit var btnVolverPartidas: Button
    lateinit var btnPausarMusica: Button

    // variable para obtener el contexto del fragment
    lateinit var contexto: Context

    private val binding get() = _binding!!

    //Esta variable me ayuda a saver si se tiene que poner o no el sonido
//    var ponerSonido = true
//    //Estas variables son para la reproduccion del sonido
//    lateinit var reproduccionSonido: MediaPlayer
//    //Este es para el sonido del fondo
//    lateinit var sonidoFondo: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // variable para obtener el contexto
        contexto = requireContext()

        reproducirSonido("menuboton")

        // variables para los botones de la vista Menu
        btnVolverPartidas = root.findViewById(R.id.btnVolverPartidas)
        btnPausarMusica = root.findViewById(R.id.btnPararMusica)

        // logica para el boton de volver a la vista de partidas
        btnVolverPartidas.setOnClickListener {
            HomeFragment.timer?.cancel()
            HomeFragment.timer = null

            // para quitar el sonido cuando volvamos a la vista de partidas
            HomeFragment.sonidoFondo.pause()

            // Iniciar la actividad MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        // logica para el boton de parar o reanudar la musica de fondo
        btnPausarMusica.setOnClickListener {
            musicaFondo()
        }


        val textView: TextView = binding.textHome
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
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

    //Este metodo permite habilitar o inhabilitar el sonido de fondo
    fun musicaFondo(){
        if(HomeFragment.ponerSonido){
            HomeFragment.sonidoFondo.pause()
            btnPausarMusica.setText("Reanudar la música")
        }else{
            HomeFragment.sonidoFondo.start()
            btnPausarMusica.setText("Pausar la música")
        }

        //Esto es para determinar si se esta escuchando se ejecuta el else si no es que no se escucha y se ejecuta el if
        HomeFragment.ponerSonido = !HomeFragment.ponerSonido
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}