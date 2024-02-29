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
import com.example.hamburguerclicker.Partida
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentDashboardBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database


public class TiendaFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private lateinit var partida: Partida
    private lateinit var listaPartidas: ListView

    private var dinTotal= 0.0;

    lateinit var btnCompraPanaderia: Button
    lateinit var txtTotPanaderia:TextView
    private var totalPanaderias=0.0

    lateinit var btnCompraCarne: Button
    lateinit var txtTotCarne:TextView
    private var totalCarnicerias=0.0

    lateinit var btnCompraQueseria: Button
    lateinit var txtTotQueseria:TextView
    private var totalQueserias=0.0

    lateinit var btnCompraLechuga: Button
    lateinit var txtTotLechuga:TextView
    private var totalLechugas=0.0

    lateinit var btnCompraHuerto: Button
    lateinit var txtTotHuerto:TextView
    private var totalHuertos=0.0

    lateinit var btnCompraBacon: Button
    lateinit var txtTotBacon:TextView
    private var totalBacon=0.0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val database = FirebaseDatabase.getInstance()
    val mDatabase = database.getReference("partida/a")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init(root)

        // Ocultar la barra de acción (action bar)

        // Ocultar la barra de acción (action bar)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private fun init(root:View){

        btnCompraPanaderia=root.findViewById(R.id.btnCompraPanaderia)
        txtTotPanaderia=root.findViewById(R.id.txtTotPan)
        btnCompraPanaderia.setOnClickListener{
            comprarPanaderia()
        }

        btnCompraCarne=root.findViewById(R.id.btnCompraCarne)
        txtTotCarne=root.findViewById(R.id.txtTotCarne)
        btnCompraCarne.setOnClickListener{
            comprarCarniceria()
        }

        btnCompraQueseria=root.findViewById(R.id.btnCompraQueso)
        txtTotQueseria=root.findViewById(R.id.txtTotQueso)
        btnCompraQueseria.setOnClickListener{
            comprarQueseria()
        }

        btnCompraLechuga=root.findViewById(R.id.btnCompraLechuga)
        txtTotLechuga=root.findViewById(R.id.txtTotLechuga)
        btnCompraLechuga.setOnClickListener{
            comprarLechuga()
        }

        btnCompraHuerto=root.findViewById(R.id.btnCompraHuerto)
        txtTotHuerto=root.findViewById(R.id.txtTotHuerto)
        btnCompraHuerto.setOnClickListener{
            comprarHuerto()
        }

        btnCompraBacon=root.findViewById(R.id.btnCompraBacon)
        txtTotBacon=root.findViewById(R.id.txtTotBacon)
        btnCompraBacon.setOnClickListener{
            comprarBacon()
        }

        var value: HashMap<String, Double>?

        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y +cada vez que se actualicen los valores en la base de datos

                value = snapshot.getValue<HashMap<String, Double>>()

                totalPanaderias = value?.get("panaderia") as Double
                txtTotPanaderia.setText("" + value?.get("panaderia")!!.toInt())
                totalCarnicerias = value?.get("carniceria") as Double
                txtTotCarne.setText("" + value?.get("carniceria")!!.toInt())
                totalQueserias = value?.get("queseria") as Double
                txtTotQueseria.setText("" + value?.get("queseria")!!.toInt())
                totalLechugas = value?.get("lechuga") as Double
                txtTotLechuga.setText("" + value?.get("lechuga")!!.toInt())
                totalHuertos = value?.get("huerto") as Double
                txtTotHuerto.setText("" + value?.get("huerto")!!.toInt())
                totalBacon = value?.get("bacon") as Double
                txtTotBacon.setText("" + value?.get("bacon")!!.toInt())

//                pesoTotal = value?.get("dinero") as Double
//                txtValorPeso.setText("" + value?.get("dinero").toString())

                Log.d(ContentValues.TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

    }

    // Tenemos que hacer algo como esto para añadir a la base de datos
//    private fun add() {
//        val pan: Int = 0
//        val carne: String =
//        val queso: String =
//        val lechuga: String =
//        val huerto: String =
//        val bacon: String =
//
////        val marca: String = etMarca.getText().toString().trim()
////        val modelo: String = etModelo.getText().toString().trim()
//        val id = mDatabase.push().key
//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
//        partida = Partida()
//        partida.getPanaderia()
//        partida.getCarniceria()
//        partida.getQueseria()
//        partida.getLechuga()
//        partida.getHuerto()
//        partida.getBacon()
////        partida.setId(id)
////        partida.setMarca(marca)
////        partida.setModelo(modelo)
//        mDatabase.child(id.toString()).setValue(
//            partida
//        ) { error, ref ->
//            if (error == null) {
//                // Éxito
//                Toast.makeText(this@DashboardFragment, "Partida agregada exitosamente", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                // Error
//                Toast.makeText(this@DashboardFragment, "Error al agregar la partida", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
////        limpiarCampos()
////        leer()
//    }

    private fun comprarPanaderia(){
        totalPanaderias++;
        txtTotPanaderia.setText(""+totalPanaderias.toInt())
        escribirDatos("panaderia")
    }

    private fun comprarCarniceria(){
        totalCarnicerias++;
        txtTotCarne.setText(""+totalCarnicerias.toInt())
        escribirDatos("carniceria")
    }
    private fun comprarQueseria(){
        totalQueserias++;
        txtTotQueseria.setText(""+totalQueserias.toInt())
        escribirDatos("queseria")
    }
    private fun comprarLechuga(){
        totalLechugas++;
        txtTotLechuga.setText(""+totalLechugas.toInt())
        escribirDatos("lechuga")
    }
    private fun comprarHuerto(){
        totalHuertos++;
        txtTotHuerto.setText(""+totalHuertos.toInt())
        escribirDatos("huerto")
    }
    private fun comprarBacon(){
        totalBacon++;
        txtTotBacon.setText(""+totalBacon.toInt())
        escribirDatos("bacon")
    }

    private  fun hayDinero(coste:Int):Boolean{
        return dinTotal>=coste
    }

    private fun escribirDatos(dato:String){
        val database = com.google.firebase.ktx.Firebase.database
        val base = database.getReference("partida/a/" + dato)

        when(dato){
            "panaderia" -> {
                base.setValue(totalPanaderias)
            }
            "carniceria" -> {
                base.setValue(totalCarnicerias)
            }
            "queseria" -> {
                base.setValue(totalQueserias)
            }
            "lechuga" -> {
                base.setValue(totalLechugas)
            }
            "huerto" -> {
                base.setValue(totalHuertos)
            }
            "bacon" -> {
                base.setValue(totalBacon)
            }
        }
    }

}