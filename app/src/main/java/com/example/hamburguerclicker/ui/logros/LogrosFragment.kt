package com.example.hamburguerclicker.ui.logros

import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.ActivityMainBinding
import com.example.hamburguerclicker.databinding.FragmentLogrosBinding
import com.example.hamburguerclicker.databinding.FragmentNotificationsBinding
import com.example.hamburguerclicker.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class LogrosFragment : Fragment() {

    private var _binding: FragmentLogrosBinding? = null

    // Declarar variables del logro 1
    lateinit var imagen1: ImageView
    lateinit var text1: TextView
    lateinit var textuno: TextView

    // Variables para ir controlando el peso
    var pesoTotal: Double = 0.0
    private val binding get() = _binding!!

    // variables para gestionar la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val mDatabase = database.getReference("partida/"+ MainActivity.partidaActual)

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
        var value: HashMap<String, Double>?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<HashMap<String, Double>>()
                pesoTotal = value?.get("dinero") as Double
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
    var obtenido = false

    fun  comprobarLogros() {


        if(pesoTotal >= 100  && obtenido){
            imagen1.setImageResource(R.drawable.logro1)
            text1.setText("Alcanza los 100 mg")
            textuno.setText("Pesas los mismo que un gusano")
            obtenido = true
        }
    }
}
