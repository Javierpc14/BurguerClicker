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
import kotlin.properties.Delegates

class HomeFragment : Fragment() {
    lateinit var partida: Partida


    private var _binding: FragmentHomeBinding? = null
    lateinit var txtValorPeso: TextView
    lateinit var unidad: TextView
    lateinit var hamburguesa: ImageView
    lateinit var btnRestar:Button
    lateinit var btnCompraCarne:Button
    var a=R.integer.contador
    var pesoTotal: Double = 0.0
    var pesoPantalla:Double=0.0
    var totalPanaderias=0.0
    var unidadPeso="Mili Gramos"
    val database = FirebaseDatabase.getInstance()
    val mDatabase = database.getReference("partida/a")
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        partida = Partida()

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        txtValorPeso=root.findViewById(R.id.txtValorPeso)
        hamburguesa=root.findViewById(R.id.hamburguesa)
        unidad=root.findViewById(R.id.unidadPeso)
        btnRestar=root.findViewById(R.id.restar)
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
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        hamburguesa.setOnClickListener(){
            pesoTotal += 100
            escribirDatos(pesoTotal)
        }

        btnRestar.setOnClickListener {
            //para que no pueda restar mas si hay 0 mg y asi no hay numeros negativos
            if (unidadPeso == "Mili Gramos" && pesoTotal <= 0) {
                return@setOnClickListener
            }
            pesoTotal -= 100
            escribirDatos(pesoTotal)
        }

        // Ocultar la barra de acción (action bar)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    fun escribirDatos(peso:Double){
        val database = Firebase.database
        val panaderiasBase = database.getReference("partida/a/dinero")
        panaderiasBase.setValue(peso)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun comprobarUnidad(){
        when (pesoTotal) {
            in 0.0..1000.0 -> {
                pesoPantalla=pesoTotal
                unidadPeso  = "Mili Gramos"
            }
            in 1000.0..1000000.0 -> {
                pesoPantalla=pesoTotal/1000
                unidadPeso = "Gramos"
            }
            in 1000000.0..1000000000.0 -> {
                pesoPantalla=pesoTotal/1000000
                unidadPeso = "Kilos"
            }
        }
        txtValorPeso.setText(String.format("%.2f", pesoPantalla))
        unidad.setText(unidadPeso)
    }

}