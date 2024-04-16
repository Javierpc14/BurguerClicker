package com.example.hamburguerclicker.ui.home
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.Partida
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
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    // Variables para controlar la vista
    private var _binding: FragmentHomeBinding? = null
    lateinit var txtValorPeso: TextView
    lateinit var unidad: TextView
    lateinit var hamburguesa: ImageView

    // Variables para controlar el peso
    var pesoTotal: Double = 0.0
    var pesoPantalla:Double=0.0

    // variables para controlar las tiendas
    var totalPanaderias=0.0
    var totalCarnicerias=0.0
    var totalQueserias=0.0
    var totalLechugas=0.0
    var totalHuerto=0.0
    var totalBacon=0.0

    // Variable estatica para contral el temporizador de ingresos pasivos
    companion object{
        var timer: Timer? = null
    }

    // variable que contiene el peso inicial de la partida
    private var unidadPeso="Mili Gramos"

    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference("partida/"+ MainActivity.partidaActual)


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
        hamburguesa=root.findViewById(R.id.hamburguesa)
        unidad=root.findViewById(R.id.unidadPeso)
        txtValorPeso.setText(String.format("%.2f", pesoTotal))


        var value: HashMap<String, Double>?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<HashMap<String, Double>>()
                pesoTotal = value?.get("dinero") as Double
                comprobarUnidad()

                totalPanaderias=value?.get("panaderia") as Double
                totalCarnicerias=value?.get("carniceria") as Double
                totalQueserias=value?.get("queseria") as Double
                totalLechugas=value?.get("lechuga") as Double
                totalHuerto=value?.get("huerto") as Double
                totalBacon=value?.get("bacon") as Double

                if (timer == null) {
                    iniciarIncrementoPasivo()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        hamburguesa.setOnClickListener(){
            pesoTotal ++
            escribirDatos(pesoTotal)
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    fun escribirDatos(peso:Double){
        val database = Firebase.database
        val panaderiasBase = database.getReference("partida/"+MainActivity.partidaActual+"/dinero")
        panaderiasBase.setValue(peso)
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

    fun  incrementoPasivo() {
        pesoTotal+=totalPanaderias * 7.5 +totalCarnicerias * 50 +totalQueserias * 6500 +totalLechugas * 115000 + totalHuerto * 20000000 + totalBacon * 50000000
        escribirDatos(pesoTotal)
    }

}