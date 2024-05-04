package com.example.hamburguerclicker.ui.dashboard

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.modelo.Partida
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentTiendaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.modelo.Tienda
import com.example.hamburguerclicker.ui.home.HomeFragment


public class TiendaFragment : Fragment() {

    private var _binding: FragmentTiendaBinding? = null

    lateinit var tiendas: HashMap<String,Tienda>

    // variable para obtener el contexto del fragment
    lateinit var contexto: Context

    private var dinTotal= 0.0;

    lateinit var btnCompraPanaderia: Button
    lateinit var txtTotPanaderia:TextView
    private var totalPanaderias=0

    lateinit var btnCompraCarne: Button
    lateinit var txtTotCarne:TextView
    private var totalCarnicerias=0

    lateinit var btnCompraQueseria: Button
    lateinit var txtTotQueseria:TextView
    private var totalQueserias=0

    lateinit var btnCompraLechuga: Button
    lateinit var txtTotLechuga:TextView
    private var totalLechugas=0

    lateinit var btnCompraHuerto: Button
    lateinit var txtTotHuerto:TextView
    private var totalHuertos=0

    lateinit var btnCompraBacon: Button
    lateinit var txtTotBacon:TextView
    private var totalBacon=0


    lateinit var _this:AppCompatActivity
    private val binding get() = _binding!!

    val database = FirebaseDatabase.getInstance()
    val mDatabase = database.getReference(MainActivity.partidaActual)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentTiendaBinding.inflate(inflater, container, false)
        val root: View = binding.root


        init()

        // variable para obtener el contexto
        contexto = requireContext()

        // hago que al cargar la clase suene este sonido de la tienda como si se le hubiera dado al boton del menu de navegacion
        reproducirSonido("tiendaboton")

        // Ocultar la barra de acción (action bar)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    private fun init(){

        var value: Partida?

        // Este método se llama una vez que se lance la aplicacion,
        // y cada vez que se actualicen los valores en la base de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Obtenemos total de las tiendas para actualizarlo si es necesario

                value = snapshot.getValue<Partida>()

                //Array para las tiendas
                tiendas =value?.tiendas as HashMap<String, Tienda>

                dinTotal = value?.pesoTotal as Double
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
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

    fun restarPeso(peso:Double){
        val database = Firebase.database
        val panaderiasBase = database.getReference(MainActivity.partidaActual+"/dinero")
        panaderiasBase.setValue(peso)
    }

    private fun comprarPanaderia(){
        if(hayDinero(150)) {
            totalPanaderias++;
            txtTotPanaderia.setText("" + totalPanaderias.toInt())
            escribirDatos("panaderia")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }

    private fun comprarCarniceria(){
        if(hayDinero(1000)) {
            totalCarnicerias++;
            txtTotCarne.setText("" + totalCarnicerias.toInt())
            escribirDatos("carniceria")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }
    private fun comprarQueseria(){
        if(hayDinero(130000)) {
            totalQueserias++;
            txtTotQueseria.setText("" + totalQueserias.toInt())
            escribirDatos("queseria")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }
    private fun comprarLechuga(){
        if(hayDinero(2300000)) {
            totalLechugas++;
            txtTotLechuga.setText("" + totalLechugas.toInt())
            escribirDatos("lechuga")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }
    private fun comprarHuerto(){
        if(hayDinero(400000000)) {
            totalHuertos++;
            txtTotHuerto.setText("" + totalHuertos.toInt())
            escribirDatos("huerto")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }
    private fun comprarBacon(){
        if(hayDinero(1000000000)) {
            totalBacon++;
            txtTotBacon.setText("" + totalBacon.toInt())
            escribirDatos("bacon")
        }else{
            mensajeNoHayDinero(requireContext())
        }
    }

    private  fun hayDinero(coste:Int):Boolean{
        return dinTotal>=coste
    }

    private fun escribirDatos(dato:String){
        val database = Firebase.database
        var base = database.getReference(MainActivity.partidaActual + "/" +  dato)
        var dinero = database.getReference(MainActivity.partidaActual+"/dinero")

        when(dato){
            "panaderia" -> {
                base.setValue(totalPanaderias)
                dinero.setValue(dinTotal - 150)
            }
            "carniceria" -> {
                base.setValue(totalCarnicerias)
                dinero.setValue(dinTotal - 1000)
            }
            "queseria" -> {
                base.setValue(totalQueserias)
                dinero.setValue(dinTotal - 130000)
            }
            "lechuga" -> {
                base.setValue(totalLechugas)
                dinero.setValue(dinTotal - 2300000)
            }
            "huerto" -> {
                base.setValue(totalHuertos)
                dinero.setValue(dinTotal - 400000000)
            }
            "bacon" -> {
                base.setValue(totalBacon)
                dinero.setValue(dinTotal - 1000000000)
            }
        }
    }
}