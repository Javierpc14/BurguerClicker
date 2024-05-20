package com.example.hamburguerclicker.ui.tienda


import android.R.attr.fragment
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentTiendaBinding
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.modelo.Tienda
import com.example.hamburguerclicker.ui.home.HomeFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlin.math.pow


public class TiendaFragment : Fragment() {

    private var _binding: FragmentTiendaBinding? = null

    lateinit var tiendas: ArrayList<Tienda>

    // variable para obtener el contexto del fragment
    private lateinit var contexto: Context

    private var pesoTotal= 0.0

    private lateinit var layoutTiendas: LinearLayout

    private val binding get() = _binding!!

    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)

    private lateinit var valueListener :ValueEventListener

    private lateinit var tituloTiendas :TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTiendaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tituloTiendas = root.findViewById(R.id.tituloTiendas)

        layoutTiendas = root.findViewById(R.id.layoutTiendas)

        init()

        // variable para obtener el contexto
        contexto = requireContext()

        // hago que al cargar la clase suene este sonido de la tienda como si se le hubiera dado al boton del menu de navegacion
        reproducirSonido("tiendaboton")

//        // Ocultar la barra de acción (action bar)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDatabase.removeEventListener(valueListener)
    }

    private fun init(){
        //Añadimos onClickListener al titulo para navegar a vista mejoras
            tituloTiendas.setOnClickListener { v ->
                Navigation.findNavController(v).navigate(R.id.navigation_mejoras)
            }

        var value: Partida?

        // Este método se llama una vez que se lance la aplicacion,
        // y cada vez que se actualicen los valores en la base de datos
        valueListener= mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Obtenemos total de las tiendas para actualizarlo si es necesario

                value = snapshot.getValue<Partida>()

                //Array para las tiendas
                tiendas =value?.tiendas as ArrayList<Tienda>

                layoutTiendas.removeAllViews()

                tiendas.forEach{ tienda ->
                    agregarTiendas(tienda)
                }

                pesoTotal = value?.pesoTotal as Double
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun cambiorUnidad(cantidad:Double): String {
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

    private fun agregarTiendas(tienda: Tienda) {
        // Inflar el layout del LinearLayout desde XML
        val layoutTienda= layoutInflater.inflate(R.layout.tienda, null)

        var precioVista: TextView = layoutTienda.findViewById(R.id.txtPrecio)
        var aporteVista: TextView = layoutTienda.findViewById(R.id.txtAportePorSegundo)
        var totalVista: TextView = layoutTienda.findViewById(R.id.txtTotTienda)
        var imagenVista: ImageView = layoutTienda.findViewById(R.id.imgTienda)
        var botonComprarVista: Button = layoutTienda.findViewById(R.id.btnCompraTienda)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        precioVista.text=cambiorUnidad(tienda.precioCompra)
        aporteVista.text = "" + cambiorUnidad(tienda.aportePasivo) + " por segundo"
        totalVista.text = "" + tienda.total
        botonComprarVista.text = "Comprar " + tienda.nombre
        imagenVista.setImageResource(tienda.imagenId)

        botonComprarVista.setOnClickListener {
            comprarTienda(tienda.precioCompra, tienda.nombre)
        }

        // Añadir el nuevo layout al ConstraintLayout principal
        layoutTiendas.addView(layoutTienda, params)
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

    private fun mensajeNoHayDinero(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cuidado")
        builder.setMessage("No tienes suficiente peso")
        builder.setPositiveButton("Aceptar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun comprarTienda(coste: Double,nombre : String){
        if(hayDinero(coste)) {

            var tienda = tiendas.find { it.nombre == nombre }

                tienda?.total = tienda?.total!! + 1

            //Formula para ir incrementando el precio de una tienda en base al total de una tienda
                tienda?.precioCompra =tienda?.precioCompra!! * 1.3.pow(tienda?.total!!)

            escribirDatos(coste)
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }

    private  fun hayDinero(coste:Double):Boolean{
        return pesoTotal>=coste
    }

    private fun escribirDatos(coste :Double){
        val pesoBase = database.getReference(MainActivity.partidaActual + "/pesoTotal")
        pesoBase.setValue(pesoTotal-coste)

        val tiendasBase =database.getReference(MainActivity.partidaActual + "/tiendas")
        tiendasBase.setValue(tiendas)
    }
}