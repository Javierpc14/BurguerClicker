package com.example.hamburguerclicker.ui.mejoras

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentMejorasBinding
import com.example.hamburguerclicker.modelo.Mejora
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.modelo.Tienda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MejorasFragment : Fragment() {
    private var _binding: FragmentMejorasBinding? = null
    private val binding get() = _binding!!

    private lateinit var tituloMejoras: TextView

    private lateinit var layoutMejoras: LinearLayout

    lateinit var mejoras: ArrayList<Mejora>

    lateinit var tiendas: ArrayList<Tienda>


    lateinit var deslizador: Deslizador

    private lateinit var contexto: Context

    private var pesoTotal = 0.0


    private lateinit var valueListener: ValueEventListener

    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMejorasBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        contexto = requireContext()


        layoutMejoras = root.findViewById(R.id.layoutMejoras)
        tituloMejoras = root.findViewById(R.id.tituloMejoras)

        deslizador = Deslizador(root)

        root.setOnTouchListener { _, event ->
            deslizador.gestureDetector.onTouchEvent(event)
        }


        tituloMejoras.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.navigation_tienda)
        }

        var value: Partida?



        valueListener = mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Obtenemos total de las mejoras para actualizarlo si es necesario

                value = snapshot.getValue<Partida>()

                //Array para las mejoras
                mejoras = value?.mejoras as ArrayList<Mejora>
                //Array para las tiendas
                tiendas = value?.tiendas as ArrayList<Tienda>


                layoutMejoras.removeAllViews()

                //Añadimos las mejoras a la vista
                mostrarMejoras()

                pesoTotal = value?.pesoTotal as Double
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        return root
    }

    private fun cambiorUnidad(cantidad: Double): String {
        var cantidadMiligramos = 0.0
        var unidad = ""
        when (cantidad) {
            in 0.0..999.9 -> {
                cantidadMiligramos = cantidad
                unidad = "Mg"
            }

            in 1000.0..999999.9 -> {
                cantidadMiligramos = cantidad / 1000
                unidad = "G"
            }

            in 1000000.0..999999999.9 -> {
                cantidadMiligramos = cantidad / 1000000
                unidad = "Kg"
            }

            in 1000000000.0..Double.MAX_VALUE -> {
                cantidadMiligramos = cantidad / 1000000000
                unidad = "T"
            }
        }
        return "%.1f %s".format(cantidadMiligramos, unidad)
    }

    private fun agregarMejora(mejora: Mejora) {
        //Obtenemos componentes de la vista mejoras mediante sus ids
        val layoutMejora = layoutInflater.inflate(R.layout.mejora, null)
        var precioVista: TextView = layoutMejora.findViewById(R.id.txtPrecio)
        var imagenVista: ImageView = layoutMejora.findViewById(R.id.imgMejora)
        var botonComprarVista: Button = layoutMejora.findViewById(R.id.btnCompraMejora)
        var descripcionMejora: TextView = layoutMejora.findViewById(R.id.txtDescripcion)
        var nombreMejora: TextView = layoutMejora.findViewById(R.id.txtNombre)

        //Si la mejora no ha sido aun obtenida se pone la imagen por defecto
        if (mejora.obtenida) {
            imagenVista.setImageResource(R.drawable.logrooculto)
        }

        //Añadir valores correspondientes a los elementos de la vista
        precioVista.text = cambiorUnidad(mejora.precio)
        botonComprarVista.text = "Comprar " + mejora.nombre
        descripcionMejora.text = mejora.descripcion
        nombreMejora.text = mejora.nombre

        //Creamos parametros para que la mejora se adapte correctamente a la vista
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //Añadimos la mejora a la vista
        layoutMejoras.addView(layoutMejora, params)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDatabase.removeEventListener(valueListener)
    }

    private fun mostrarMejoras() {
        var tiendaId: Int
        var requisitoId: Int
        mejoras.forEach { mejora ->
            //Se comprueba cuantas tiendas hay para ver si se deben mostrar sus mejoras, tambien se comprueba si se tienen las mejoras previas requeridas
            //mediante el id tienda asociado a cada mejora,
            // y en caso que el id sea -1 significa que es una mejora para el peso por click

            tiendaId = mejora.tiendaId
            requisitoId = mejora.requisitoId


            if ((tiendaId == -1 || tiendas[tiendaId].total > 0)
                && (requisitoId == -1 || mejoras[requisitoId].obtenida)
            ) {
                agregarMejora(mejora)
            }
        }
    }

    fun onTouchEvent(event: MotionEvent) {

    }
}