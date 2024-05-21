package com.example.hamburguerclicker.ui.mejoras

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
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

class MejorasFragment : Fragment(){
    private var _binding: FragmentMejorasBinding? = null

    private lateinit var tituloMejoras: TextView

    private lateinit var layoutMejoras: LinearLayout

    lateinit var mejoras: ArrayList<Mejora>

    lateinit var tiendas: ArrayList<Tienda>


    private lateinit var contexto: Context

    private var pesoTotal = 0.0
    private var pesoPorClick = 0.0


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


        tituloMejoras.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.navigation_tienda)
        }

        var value: Partida?

        valueListener = mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value = snapshot.getValue<Partida>()

                mejoras = value?.mejoras as ArrayList<Mejora>
                tiendas = value?.tiendas as ArrayList<Tienda>

                layoutMejoras.removeAllViews()

                mostrarMejoras()

                pesoTotal = value?.pesoTotal as Double
                pesoPorClick = value?.pesoPorClick as Double
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        return root
    }

    private fun cambioUnidad(cantidad: Double): String {
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
        val precioVista: TextView = layoutMejora.findViewById(R.id.txtPrecio)
        val imagenVista: ImageView = layoutMejora.findViewById(R.id.imgMejora)
        val botonComprarVista: Button = layoutMejora.findViewById(R.id.btnCompraMejora)
        val descripcionMejora: TextView = layoutMejora.findViewById(R.id.txtDescripcion)
        val nombreMejora: TextView = layoutMejora.findViewById(R.id.txtNombre)

        //Si la mejora no ha sido aun obtenida se pone la imagen por defecto
        if (mejora.obtenida) {
            imagenVista.setImageResource(mejora.imagenId)
            botonComprarVista.isVisible=false
            precioVista.isVisible=false
        }else{
            botonComprarVista.setOnClickListener {
                comprarMejora(mejora)
            }
            imagenVista.setImageResource(R.drawable.logrooculto)
        }

        //Añadir valores correspondientes a los elementos de la vista
        precioVista.text = cambioUnidad(mejora.precio)
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

    private fun comprarMejora(mejora: Mejora){
        val precioMejora=mejora.precio

        if(hayDinero(precioMejora)){
            mejora.obtenida =true

            aplicarMejora(mejora)

            escribirDatos(precioMejora)

        }else{
            mensajeNoHayDinero(requireContext())
        }
    }

    private fun aplicarMejora(mejora: Mejora){
        //Si el tiendaId es igual a -1 significa que es una mejora para aumentar las ganancias por clicks
        if(mejora.tiendaId == -1){
            pesoPorClick *= mejora.incremento
        }
        //Si es una mejora de una tienda se busca en el arraylist de tiendas mediante el tiendaId, y se aplica
        else{
            tiendas[mejora.tiendaId].aportePasivo *= mejora.incremento
        }
    }

    private fun mensajeNoHayDinero(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cuidado")
        builder.setMessage("No tienes suficiente peso")
        builder.setPositiveButton("Aceptar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private  fun hayDinero(coste:Double):Boolean{
        return pesoTotal>=coste
    }

    private fun escribirDatos(coste :Double){
        val pesoBase = database.getReference(MainActivity.partidaActual + "/pesoTotal")
        pesoBase.setValue(pesoTotal-coste)

        val mejorasBase =database.getReference(MainActivity.partidaActual + "/mejoras")
        mejorasBase.setValue(mejoras)

        val pesoPorClickBase = database.getReference(MainActivity.partidaActual + "/pesoPorClick")
        pesoPorClickBase.setValue(pesoPorClick)

        val tiendasBase =database.getReference(MainActivity.partidaActual + "/tiendas")
        tiendasBase.setValue(tiendas)
    }
}