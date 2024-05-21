package com.example.hamburguerclicker.ui.menu

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentMenuBinding
import com.example.hamburguerclicker.ui.home.HomeFragment


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    //variables para los botones de la vista Menu
    private lateinit var btnVolverPartidas: Button
    private lateinit var btnPausarMusica: Button

    // variable para obtener el contexto del fragment
    private lateinit var contexto: Context

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
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

        return root
    }

    private var sonidoEnReproduccion=false
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
    private fun musicaFondo(){
        if(HomeFragment.ponerSonido){
            HomeFragment.sonidoFondo.pause()
            btnPausarMusica.text = "Reanudar la música"
        }else{
            HomeFragment.sonidoFondo.start()
            btnPausarMusica.text = "Pausar la música"
        }

        //Esto es para determinar si se esta escuchando se ejecuta el else si no es que no se escucha y se ejecuta el if
        HomeFragment.ponerSonido = !HomeFragment.ponerSonido
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}