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

    var unidadPeso="Mili Gramos"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        partida = Partida()

//        pesoTotal = partida.dinero

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        txtValorPeso=root.findViewById(R.id.txtValorPeso)
        hamburguesa=root.findViewById(R.id.hamburguesa)
        unidad=root.findViewById(R.id.unidadPeso)
        btnRestar=root.findViewById(R.id.restar)

        txtValorPeso.setText(String.format("%.2f", pesoTotal))


        val database = FirebaseDatabase.getInstance()
        val mDatabase = database.getReference("partida/a")

        var value: HashMap<String, Double>?

        // Read from the database
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                println("aaaaaaa")
                value = snapshot.getValue<HashMap<String, Double>>()
                pesoTotal = value?.get("dinero") as Double
                txtValorPeso.setText("" + value?.get("dinero").toString())
               Log.d(TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })





        hamburguesa.setOnClickListener(){
//            pesoTotal+=100
//            txtValorPeso.setText(String.format("%.2f", pesoTotal))
//            unidad()

            pesoTotal += 100

            when {
                pesoTotal >= 1000 && unidadPeso == "Mili Gramos" -> {
                    unidadPeso = "Gramos"
                    pesoTotal /= 1000.0
                }
                pesoTotal >= 1000 && unidadPeso == "Gramos" -> {
                    unidadPeso = "Kilos"
                    pesoTotal /= 1000.0
                }
                pesoTotal >= 1000 && unidadPeso == "Kilos" -> {
                    unidadPeso = "Toneladas"
                    pesoTotal /= 1000.0
                }
            }

            txtValorPeso.setText(String.format("%.2f", pesoTotal))
            unidad.setText(unidadPeso)
        }



        btnRestar.setOnClickListener {
            //para que no pueda restar mas si hay 0 mg y asi no hay numeros negativos
            if (unidadPeso == "Mili Gramos" && pesoTotal <= 0) {
                return@setOnClickListener
            }


            pesoTotal -= 100

            if (pesoTotal < 0) {
                when (unidadPeso) {
                    "Gramos" -> {
                        pesoTotal += 1000
                        unidadPeso  = "Mili Gramos"
                    }
                    "Kilos" -> {
                        pesoTotal += 1000
                        unidadPeso = "Gramos"
                    }
                    "Toneladas" -> {
                        pesoTotal += 1000
                        unidadPeso = "Kilos"
                    }
                }
            }

            txtValorPeso.setText(String.format("%.2f", pesoTotal))
            unidad.setText(unidadPeso)
//            unidad()
        }


        // Ocultar la barra de acción (action bar)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }



        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun leer() {
//        mDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                profesoresCoches.clear()
//                for (snapshot in dataSnapshot.children) {
//                    coche = snapshot.getValue(Coche::class.java)
//                    val cochee: String =
//                        (coche.getId() + " : " + coche.getMarca()).toString() + " , " + coche.getModelo()
//                    profesoresCoches.add(cochee)
//                    if (coche != null) {
//                        Toast.makeText(this@MainActivity, cochee, Toast.LENGTH_SHORT).show()
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//            }
//        })
//    }
//
//    private fun limpiarCampos() {
//        etMarca.setText("")
//        etModelo.setText("")
//    }

//    private fun unidad() {
//        when (unidadPeso) {
//            "Mili Gramos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Gramos"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Gramos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Kilos"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                } else if (pesoTotal < 1) {
//                    unidadPeso = "Mili Gramos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Kilos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Toneladas"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                } else if (pesoTotal < 1) {
//                    unidadPeso = "Gramos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Toneladas" -> {
//                if (pesoTotal < 1) {
//                    unidadPeso = "Kilos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//        }
//    }

}