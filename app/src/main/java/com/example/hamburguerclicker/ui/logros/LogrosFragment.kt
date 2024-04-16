package com.example.hamburguerclicker.ui.logros

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentLogrosBinding
import com.example.hamburguerclicker.modelo.Partida
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database

class LogrosFragment : Fragment() {

    private var _binding: FragmentLogrosBinding? = null

    // Declarar variables del logro 1
    lateinit var imagen1: ImageView
    lateinit var text1: TextView
    lateinit var textuno: TextView

    // Variables para ir controlando el peso
    var pesoTotal: Double = 0.0

    // Variables de los logros
    var logro1: Boolean = false

    private val binding get() = _binding!!

    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference(MainActivity.partidaActual)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val logrosViewModel =
            ViewModelProvider(this).get(LogrosViewModel::class.java)

        _binding = FragmentLogrosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // variables logro 1
        imagen1 = root.findViewById(R.id.img1)
        text1 = root.findViewById(R.id.txt1)
        textuno = root.findViewById(R.id.txtuno)


        //Leer el peso de la base de datos
        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()
                pesoTotal = value?.pesoTotal as Double

                logro1 = value?.logros?.logro1 as Boolean
                comprobarLogros()


            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })



        val textView: TextView = binding.textDashboard
        logrosViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //var obtenido = false
    fun  comprobarLogros() {

        if(pesoTotal >= 100 || logro1 == true){
            imagen1.setImageResource(R.drawable.logro1)
            text1.setText("Alcanza los 100 mg")
            textuno.setText("Pesas los mismo que un gusano")
            logro1 = true
            escribirDatos("logro1")
        }
    }

    private fun escribirDatos(dato:String) {
        val database = com.google.firebase.ktx.Firebase.database
        var base = database.getReference(MainActivity.partidaActual + "/logros/" + dato)

        when (dato) {
            "logro1" -> {
                base.setValue(logro1)
            }
        }
    }


}
