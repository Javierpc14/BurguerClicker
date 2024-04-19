package com.example.hamburguerclicker.ui.home
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.google.firebase.ktx.Firebase
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

    // variables para controlar las tiendas
    var totalPanaderias=0
    var totalCarnicerias=0
    var totalQueserias=0
    var totalLechugas=0
    var totalHuerto=0
    var totalBeicones=0

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
                comprobarLogro()
            }
        }, 0, 1000)
    }

    // funcion que comprueba si se ha conseguido un logro
    private fun comprobarLogro(){

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

}