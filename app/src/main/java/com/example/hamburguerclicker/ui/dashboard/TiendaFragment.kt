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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Context
import com.example.hamburguerclicker.MainActivity


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


    lateinit var _this:AppCompatActivity
    private val binding get() = _binding!!

    val database = FirebaseDatabase.getInstance()
    val mDatabase = database.getReference("partida/"+MainActivity.partidaActual)

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

                dinTotal = value?.get("dinero") as Double

//                pesoTotal = value?.get("dinero") as Double
//                txtValorPeso.setText("" + value?.get("dinero").toString())

                Log.d(ContentValues.TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

    }

    fun mensajeNoHayDinero(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cuidado")
        builder.setMessage("No tienes suficiente peso")
        builder.setPositiveButton("Aceptar", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun restarPeso(peso:Double){
        val database = Firebase.database
        val panaderiasBase = database.getReference("partida/"+MainActivity.partidaActual+"/dinero")
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

    private fun dinero(){
//        dinTotal++;
        //txtTotBacon.setText(""+totalBacon.toInt())
        escribirDatos("bacon")
    }

    private  fun hayDinero(coste:Int):Boolean{
        return dinTotal>=coste
    }

    private fun escribirDatos(dato:String){
        val database = com.google.firebase.ktx.Firebase.database
        var base = database.getReference("partida/"+MainActivity.partidaActual + "/" +  dato)
        var dinero = database.getReference("partida/"+MainActivity.partidaActual+"/dinero")

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