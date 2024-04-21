package com.example.hamburguerclicker.ui.home
import android.content.ContentValues.TAG
import android.content.Context
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
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
    var pesoPantalla:Double=0.0

    // variable para obtener el contexto del fragment
    lateinit var contexto: Context

    // variables para controlar las tiendas
    var totalPanaderias=0
    var totalCarnicerias=0
    var totalQueserias=0
    var totalLechugas=0
    var totalHuerto=0
    var totalBeicones=0

    // variables de los logros
    var logro1: Boolean = false
    var logro2: Boolean = false
    var logro3: Boolean = false
    var logro4: Boolean = false
    var logro5: Boolean = false
    var logro6: Boolean = false
    var logro7: Boolean = false
    var logro8: Boolean = false
    var logro9: Boolean = false
    var logro10: Boolean = false
    var logro11: Boolean = false
    var logro12: Boolean = false

    // variables de las alertas
    var alerta1: Boolean = false
    var alerta2: Boolean = false
    var alerta3: Boolean = false
    var alerta4: Boolean = false
    var alerta5: Boolean = false
    var alerta6: Boolean = false
    var alerta7: Boolean = false
    var alerta8: Boolean = false
    var alerta9: Boolean = false
    var alerta10: Boolean = false
    var alerta11: Boolean = false
    var alerta12: Boolean = false

    // Variable estatica para contral el temporizador de ingresos pasivos
    companion object{
        var timer: Timer? = null
    }

    // variable para gestionar la imagen de la hamburguesa

    // variable que contiene el peso inicial de la partida
    private var unidadPeso="Mili Gramos"

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
        txtValorPeso=root.findViewById(R.id.txtValorPeso)

        // variable para gestionar la imagen de la hamburguesa
        imgHamburguesa=root.findViewById(R.id.hamburguesa)

        unidad=root.findViewById(R.id.unidadPeso)
        txtValorPeso.setText(String.format("%.2f", pesoTotal))

        // variable para obtener el contexto
        contexto = requireContext()

        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()

                pesoTotal = value?.pesoTotal as Double
                comprobarUnidad()

                totalPanaderias=value?.tiendas?.panaderias as Int
                totalCarnicerias=value?.tiendas?.carnicerias as Int
                totalQueserias=value?.tiendas?.queserias as Int
                totalLechugas=value?.tiendas?.lechugas as Int
                totalHuerto=value?.tiendas?.huertos as Int
                totalBeicones=value?.tiendas?.beicones as Int


                // variables de las alertas para obtener el valor de ellas de la base de datos
                alerta1 = value?.alertas?.alerta1 as Boolean
                alerta2 = value?.alertas?.alerta2 as Boolean
                alerta3 = value?.alertas?.alerta3 as Boolean
                alerta4 = value?.alertas?.alerta4 as Boolean
                alerta5 = value?.alertas?.alerta5 as Boolean
                alerta6 = value?.alertas?.alerta6 as Boolean
                alerta7 = value?.alertas?.alerta7 as Boolean
                alerta8 = value?.alertas?.alerta8 as Boolean
                alerta9 = value?.alertas?.alerta9 as Boolean
                alerta10 = value?.alertas?.alerta10 as Boolean
                alerta11 = value?.alertas?.alerta11 as Boolean
                alerta12 = value?.alertas?.alerta12 as Boolean

                comprobarLogro()
                cambiarImagenHamburguesa()

                if (timer == null) {
                    iniciarIncrementoPasivo()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        imgHamburguesa.setOnClickListener(){
            pesoTotal ++
            escribirDatos(pesoTotal)
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }
    private fun escribirDatos(peso:Double){
        val pesoBase = database.getReference(MainActivity.partidaActual+"/pesoTotal")
        pesoBase.setValue(peso)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun comprobarUnidad(){
        when (pesoTotal) {
            in 0.0..999.9 -> {
                pesoPantalla=pesoTotal
                unidadPeso  = getString(R.string.pesoMg)
            }
            in 1000.0..999999.9 -> {
                pesoPantalla=pesoTotal/1000
                unidadPeso = getString(R.string.pesoGramos)
            }
            in 1000000.0..999999999.9 -> {
                pesoPantalla=pesoTotal/1000000
                unidadPeso = getString(R.string.pesoKilos)
            }
            in 1000000000.0 .. Double.MAX_VALUE -> {
                pesoPantalla=pesoTotal/1000000000
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

            }
        }, 0, 1000)
    }

    private fun mostrarMensaje(mensaje: String) {
        // requireActivity().runOnUiThread, esto se encarga de mostrar el Toast
        // en el hilo principal para que no salte un error de que el hilo en el que se esta
        // intentando mostrar el Toast no tiene configurado un bucle de mensajes.
        requireActivity().runOnUiThread {
            Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
        }
    }


    // funcion que comprueba si se ha conseguido un logro
    private fun comprobarLogro(){

        if(logro1 && !alerta1){
            // desbloquear el logro cuando se consiguen 100 mg
            mostrarMensaje("Logro desbloqueado \n Alcanza los 100 mg")
            alerta1 = true
            escribirDatosAlertas("alerta1")
        }
        if(logro2 && !alerta2){
            // desbloquear el logro 2 cuando se compran 20 panaderias
            mostrarMensaje("Logro desbloqueado \n Panadero maestro")
            alerta2 = true
            escribirDatosAlertas("alerta2")
        }
        if(logro3 && !alerta3){
            // desbloquear el logro 3 cuando se alcanzan los 10 gramos
            mostrarMensaje("Logro desbloqueado \n Alcanza los 10 g")
            alerta3 = true
            escribirDatosAlertas("alerta3")
        }
        if(logro4 && !alerta4){
            // desbloquear el logro 4 cuando se compran 20 carnicerias
            mostrarMensaje("Logro desbloqueado \n Carnicero maestro")
            alerta4 = true
            escribirDatosAlertas("alerta4")
        }
        if(logro5 && !alerta5){
            // desbloquear el logro 5 cuando se alcanzan los 700 gramos
            mostrarMensaje("Logro desbloqueado \n Alcanza los 700 gramos")
            alerta5 = true
            escribirDatosAlertas("alerta5")
        }
        if(logro6 && !alerta6){
            // desbloquear el logro 6 cuando se compran 20 queserias
            mostrarMensaje("Logro desbloqueado \n Quesero maestro")
            alerta6 = true
            escribirDatosAlertas("alerta6")
        }
        if(logro7 && !alerta7){
            // desbloquear el logro 7 cuando se alcanzan los 20 kg
            mostrarMensaje("Logro desbloqueado \n ALcanza los 20 Kg")
            alerta7 = true
            escribirDatosAlertas("alerta7")
        }
        if(logro8 && !alerta8){
            // desbloquear el logro 8 cuando se compran 20 lechugas
            mostrarMensaje("Logro desbloqueado \n Lechuga maestra")
            alerta8 = true
            escribirDatosAlertas("alerta8")
        }
        if(logro9 && !alerta9){
            // desbloquear el logro 9 cuando se alcanzan los 800 kg
            mostrarMensaje("Logro desbloqueado \n Alcanza los 800 Kg")
            alerta9 = true
            escribirDatosAlertas("alerta9")
        }
        if(logro10 && !alerta10){
            // desbloquear el logro 10 cuando se compran 20 huertos
            mostrarMensaje("Logro desbloqueado \n Huerto maestro")
            alerta10 = true
            escribirDatosAlertas("alerta10")
        }
        if(logro11 && !alerta11){
            // desbloquear el logro 11 cuando se alcanzan las 140T
            mostrarMensaje("Logro desbloqueado \n Alcanza las 140 T")
            alerta11 = true
            escribirDatosAlertas("alerta11")
        }
        if(logro12 && !alerta12){
            // desbloquear el logro 12 cuando se compran 20 beicones
            mostrarMensaje("Logro desbloqueado \n Beicon maestro")
            alerta12 = true
            escribirDatosAlertas("alerta12")
        }
    }

    fun  incrementoPasivo() {
        pesoTotal+=totalPanaderias * 7.5 +totalCarnicerias * 50 +totalQueserias * 6500 +totalLechugas * 115000 + totalHuerto * 20000000 + totalBeicones * 50000000
       escribirDatos(pesoTotal)
    }

    private fun cambiarImagenHamburguesa(){
        if(totalBeicones >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa6)
        }
        else if(totalHuerto >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa5)
        }
        else if(totalLechugas >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa4)
        }
        else if(totalQueserias >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa3)
        }
        else if(totalCarnicerias >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa2)
        }
        else if(totalPanaderias >= 1){
            imgHamburguesa.setImageResource(R.drawable.hamburguesa1)
        }
    }

    private fun escribirDatosAlertas(dato:String) {
        val database = com.google.firebase.ktx.Firebase.database
        var base = database.getReference(MainActivity.partidaActual + "/alertas/" + dato)

        when (dato) {
            "alerta1" -> {
                base.setValue(alerta1)
            }
            "alerta2" ->{
                base.setValue(alerta2)
            }
            "alerta3" ->{
                base.setValue(alerta3)
            }
            "alerta4" ->{
                base.setValue(alerta4)
            }
            "alerta5" ->{
                base.setValue(alerta5)
            }
            "alerta6" ->{
                base.setValue(alerta6)
            }
            "alerta7" ->{
                base.setValue(alerta7)
            }
            "alerta8" ->{
                base.setValue(alerta8)
            }
            "alerta9" ->{
                base.setValue(alerta9)
            }
            "alerta10" ->{
                base.setValue(alerta10)
            }
            "alerta11" ->{
                base.setValue(alerta11)
            }
            "alerta12" ->{
                base.setValue(alerta12)
            }
        }
    }

}