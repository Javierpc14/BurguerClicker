package com.example.hamburguerclicker.ui.logros

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
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

class LogrosFragment : Fragment() {
    private var _binding: FragmentLogrosBinding? = null

    private lateinit var layoutLogros: LinearLayout

    // variable para obtener el contexto del fragment
    private lateinit var contexto: Context

    // Variable para el contador de los logros
    private lateinit var contador: TextView

    // variable que va sumando 1 cuando se desbloquea un logro
    private var contadorLogros: Int = 0

    // Variable para lamacenar todos los logros
    lateinit var logros: ArrayList<Logro>

    // Variables para ir controlando el peso
    var pesoTotal: Double = 0.0

    private val binding get() = _binding!!

    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)

    private lateinit var valueListener :ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLogrosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // variable para obtener el contexto
        contexto = requireContext()

        // hago que al cargar la clase suene este sonido de la tclase logros como si se le hubiera dado al boton del menu de navegacion
        reproducirSonido("logroboton")

        layoutLogros = root.findViewById(R.id.layoutLogros)

        contexto = requireContext()

        // Variable para el contador de los logros
        contador = root.findViewById(R.id.txtcontador)

        //Leer el peso de la base de datos
        var value: Partida?

        // Leer de la base de de datos
        valueListener= mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()
                pesoTotal = value?.pesoTotal as Double

                logros = value?.logros as ArrayList<Logro>

                comprobarLogros()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDatabase.removeEventListener(valueListener)
    }

    private var sonidoEnReproduccion = false
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
        HomeFragment.reproduccionSonido = MediaPlayer.create(contexto, recurso)

        HomeFragment.reproduccionSonido?.setOnCompletionListener { mediaPlayer ->
            // Liberar el MediaPlayer después de que termine el sonido
            mediaPlayer.release()
            // Actualizar el estado de reproducción
            sonidoEnReproduccion = false
        }
        HomeFragment.reproduccionSonido?.start()
        sonidoEnReproduccion = true
    }

    fun comprobarLogros() {
        //Reseteamos el layout de logros
        layoutLogros.removeAllViews()

        contadorLogros = 0

//Añadir los logros a la vista de forma dinámica
       logros.forEach{logro->
           if(logro.conseguido){
               anyadirLogros(logro.requisito,logro.descripcion,logro.imagenId)
               contadorLogros++
           }
           else{
               anyadirLogros("???","?????")
           }
        }
        contador.text = "$contadorLogros / ${logros.size}"
    }

    private fun anyadirLogros(requisito: String, descripcion : String, idImagen:Int=0) {
        val logroVista: View = layoutInflater.inflate(R.layout.logro, null)

        val requisitoVista: TextView = logroVista.findViewById(R.id.requisitoLogro)
        val descripcionVista: TextView = logroVista.findViewById(R.id.descripcionLogro)
        val imagenVista: ImageView = logroVista.findViewById(R.id.imagenLogro)

        requisitoVista.text = requisito
        descripcionVista.text = descripcion

        if(idImagen != 0){
            imagenVista.setImageResource(idImagen)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.espacio_entre_logros)

        logroVista.layoutParams = params

        layoutLogros.addView(logroVista)
    }
}
