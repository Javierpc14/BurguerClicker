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
import com.example.hamburguerclicker.modelo.Logros
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

    //Declarar variables del logro 2
    lateinit var  imagen2: ImageView
    lateinit var text2: TextView
    lateinit var textdos: TextView

    //Declarar variables del logro 3
    lateinit var  imagen3: ImageView
    lateinit var text3: TextView
    lateinit var texttres: TextView

    //Declarar variables del logro 4
    lateinit var  imagen4: ImageView
    lateinit var text4: TextView
    lateinit var textcuatro: TextView

    //Declarar variables del logro 5
    lateinit var  imagen5: ImageView
    lateinit var text5: TextView
    lateinit var textcinco: TextView

    //Declarar variables del logro 6
    lateinit var  imagen6: ImageView
    lateinit var text6: TextView
    lateinit var textseis: TextView

    //Declarar variables del logro 7
    lateinit var  imagen7: ImageView
    lateinit var text7: TextView
    lateinit var textsiete: TextView

    //Declarar variables del logro 8
    lateinit var  imagen8: ImageView
    lateinit var text8: TextView
    lateinit var textocho: TextView

    //Declarar variables del logro 9
    lateinit var  imagen9: ImageView
    lateinit var text9: TextView
    lateinit var textnueve: TextView

    //Declarar variables del logro 10
    lateinit var  imagen10: ImageView
    lateinit var text10: TextView
    lateinit var textdiez: TextView

    //Declarar variables del logro 11
    lateinit var  imagen11: ImageView
    lateinit var text11: TextView
    lateinit var textonce: TextView

    //Declarar variables del logro 12
    lateinit var  imagen12: ImageView
    lateinit var text12: TextView
    lateinit var textdoce: TextView

    // Variable para el contador de los logros
    lateinit var contador:TextView
    // variable que va sumando 1 cuando se desbloquea un logro
    var contadorLogros: Int = 0
    // Variable para lamacenar todos los logros
    lateinit var logros: Logros

    // Variables para ir controlando el peso
    var pesoTotal: Double = 0.0

    // Variables para controlar el total de tiendas
    var totalPan: Int = 0
    var totalCarne: Int = 0
    var totalQueso: Int = 0
    var totalLechuga: Int = 0
    var totalTomate: Int = 0
    var totalBacon: Int = 0

    // Variables de los logros
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

        // variables logro 2
        imagen2 = root.findViewById(R.id.img2)
        text2 = root.findViewById(R.id.txt2)
        textdos = root.findViewById(R.id.txtdos)

        // variables logro 3
        imagen3 = root.findViewById(R.id.img3)
        text3 = root.findViewById(R.id.txt3)
        texttres = root.findViewById(R.id.txttres)

        // variables logro 4
        imagen4 = root.findViewById(R.id.img4)
        text4 = root.findViewById(R.id.txt4)
        textcuatro = root.findViewById(R.id.txtcuatro)

        // variables logro 5
        imagen5 = root.findViewById(R.id.img5)
        text5 = root.findViewById(R.id.txt5)
        textcinco = root.findViewById(R.id.txtcinco)

        // variables logro 6
        imagen6 = root.findViewById(R.id.img6)
        text6 = root.findViewById(R.id.txt6)
        textseis = root.findViewById(R.id.txtseis)

        // variables logro 7
        imagen7 = root.findViewById(R.id.img7)
        text7 = root.findViewById(R.id.txt7)
        textsiete = root.findViewById(R.id.txtsiete)

        // variables logro 8
        imagen8 = root.findViewById(R.id.img8)
        text8 = root.findViewById(R.id.txt8)
        textocho = root.findViewById(R.id.txtocho)

        // variables logro 9
        imagen9 = root.findViewById(R.id.img9)
        text9 = root.findViewById(R.id.txt9)
        textnueve = root.findViewById(R.id.txtnueve)

        // variables logro 10
        imagen10 = root.findViewById(R.id.img10)
        text10 = root.findViewById(R.id.txt10)
        textdiez = root.findViewById(R.id.txtdiez)

        // variables logro 11
        imagen11 = root.findViewById(R.id.img11)
        text11 = root.findViewById(R.id.txt11)
        textonce = root.findViewById(R.id.txtonce)

        // variables logro 12
        imagen12 = root.findViewById(R.id.img12)
        text12 = root.findViewById(R.id.txt12)
        textdoce = root.findViewById(R.id.txtdoce)

        // Variable para el contador de los logros
        contador = root.findViewById(R.id.txtcontador)

        //Leer el peso de la base de datos
        var value: Partida?
        // Leer de la base de de datos
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método se llama una vez que se lance la aplicacion,
                // y cada vez que se actualicen los valores en la base de datos
                value = snapshot.getValue<Partida>()
                pesoTotal = value?.pesoTotal as Double

                totalPan = value?.tiendas?.panaderias as Int
                totalCarne = value?.tiendas?.carnicerias as Int
                totalQueso = value?.tiendas?.queserias as Int
                totalLechuga = value?.tiendas?.lechugas as Int
                totalTomate = value?.tiendas?.huertos as Int
                totalBacon = value?.tiendas?.beicones as Int

                logro1 = value?.logros?.logro1 as Boolean
                logro2 = value?.logros?.logro2 as Boolean
                logro3 = value?.logros?.logro3 as Boolean
                logro4 = value?.logros?.logro4 as Boolean
                logro5 = value?.logros?.logro5 as Boolean
                logro6 = value?.logros?.logro6 as Boolean
                logro7 = value?.logros?.logro7 as Boolean
                logro8 = value?.logros?.logro8 as Boolean
                logro9 = value?.logros?.logro9 as Boolean
                logro10 = value?.logros?.logro10 as Boolean
                logro11 = value?.logros?.logro11 as Boolean
                logro12 = value?.logros?.logro12 as Boolean

                logros = value?.logros as Logros

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

    fun  comprobarLogros() {
        contadorLogros = 0

        if(pesoTotal >= 100 || logro1 == true){
            // desbloquear el logro cuando se consiguen 100 mg
            imagen1.setImageResource(R.drawable.logro1)
            text1.setText("Alcanza los 100 mg")
            textuno.setText("Pesas los mismo que un gusano")
            logro1 = true
            escribirDatos("logro1")
            contadorLogros ++;
        }
        if(totalPan >= 20 || logro2 == true){
            // desbloquear el logro 2 cuando se compran 20 panaderias
            imagen2.setImageResource(R.drawable.logro2)
            text2.setText("Panadero maestro")
            textdos.setText("Compra 20 panaderias")
            logro2 = true
            escribirDatos("logro2")
            contadorLogros ++;
        }
        if(pesoTotal >= 10000 || logro3 == true){
            // desbloquear el logro 3 cuando se alcanzan los 10 gramos
            imagen3.setImageResource(R.drawable.logro3)
            text3.setText("Alcanza los 10 g")
            texttres.setText("Pesas lo mismo que un lapiz")
            logro3 = true
            escribirDatos("logro3")
            contadorLogros ++;
        }
        if(totalCarne >= 20 || logro4 == true){
            // desbloquear el logro 4 cuando se compran 20 carnicerias
            imagen4.setImageResource(R.drawable.logro4)
            text4.setText("Carnicero maestro")
            textcuatro.setText("Compra 20 carnicerias")
            logro4 = true
            escribirDatos("logro4")
            contadorLogros ++;
        }
        if(pesoTotal >= 700000 || logro5 == true){
            // desbloquear el logro 5 cuando se alcanzan los 700 gramos
            imagen5.setImageResource(R.drawable.logro5)
            text5.setText("Alcanza los 700 g")
            textcinco.setText("Pesas lo mismo que una almohada")
            logro5 = true
            escribirDatos("logro5")
            contadorLogros ++;
        }
        if(totalQueso >= 20 || logro6 == true){
            // desbloquear el logro 6 cuando se compran 20 queserias
            imagen6.setImageResource(R.drawable.logro6)
            text6.setText("Quesero maestro")
            textseis.setText("Compra 20 queserias")
            logro6 = true
            escribirDatos("logro6")
            contadorLogros ++;
        }
        if(pesoTotal >= 20000000 || logro7 == true){
            // desbloquear el logro 7 cuando se alcanzan los 20 kg
            imagen7.setImageResource(R.drawable.logro7)
            text7.setText("Alcanza los 20 Kg")
            textsiete.setText("Pesas lo mismo que un lince")
            logro7 = true
            escribirDatos("logro7")
            contadorLogros ++;
        }
        if(totalLechuga >= 20 || logro8 == true){
            // desbloquear el logro 8 cuando se compran 20 lechugas
            imagen8.setImageResource(R.drawable.logro8)
            text8.setText("Lechuga maestra")
            textocho.setText("Compra 20 lechugas")
            logro8 = true
            escribirDatos("logro8")
            contadorLogros ++;
        }
        if(pesoTotal >= 800000000 || logro9 == true){
            // desbloquear el logro 9 cuando se alcanzan los 800 kg
            imagen9.setImageResource(R.drawable.logro9)
            text9.setText("Alcanza los 800 Kg")
            textnueve.setText("Pesas lo mismo que un twingo")
            logro9 = true
            escribirDatos("logro9")
            contadorLogros ++;
        }
        if(totalTomate >= 20 || logro10 == true){
            // desbloquear el logro 10 cuando se compran 20 huertos
            imagen10.setImageResource(R.drawable.logro10)
            text10.setText("Huerto maestra")
            textdiez.setText("Compra 20 huertos")
            logro10 = true
            escribirDatos("logro10")
            contadorLogros ++
        }
        if(pesoTotal >= 140000000000 || logro11 == true){
            // desbloquear el logro 11 cuando se alcanzan las 140T
            imagen11.setImageResource(R.drawable.logro11)
            text11.setText("Alcanza las 140 T")
            textonce.setText("Pesas lo mismo que una ballena azul")
            logro11 = true
            escribirDatos("logro11")
            contadorLogros ++
        }
        if(totalBacon >= 20 || logro12 == true){
            // desbloquear el logro 12 cuando se compran 20 beicones
            imagen12.setImageResource(R.drawable.logro12)
            text12.setText("Beicon maestro")
            textdoce.setText("Compra 20 beicones")
            logro12 = true
            escribirDatos("logro12")
            contadorLogros ++
        }

        contador.setText("$contadorLogros / 12")
    }

//    private fun comprobarContadorLogros(){
//        contadorLogros = 0
//
//        if(logros.logro1){
//
//        }
//        if(logros.logro2){
//            contadorLogros ++;
//        }
//        if(logros.logro3){
//            contadorLogros ++;
//        }
//        if(logros.logro4){
//            contadorLogros ++;
//        }
//        if(logros.logro5){
//            contadorLogros ++;
//        }
//        if(logros.logro6){
//            contadorLogros ++;
//        }
//        if(logros.logro7){
//            contadorLogros ++;
//        }
//        if(logros.logro8){
//            contadorLogros ++;
//        }
//        if(logros.logro9){
//            contadorLogros ++;
//        }
//        if(logros.logro10){
//            contadorLogros ++;
//        }
//        if(logros.logro11){
//            contadorLogros ++;
//        }
//        if(logros.logro12){
//            contadorLogros ++;
//        }
//
//        contador.setText("$contadorLogros / 12")
//    }

    private fun escribirDatos(dato:String) {
        val database = com.google.firebase.ktx.Firebase.database
        var base = database.getReference(MainActivity.partidaActual + "/logros/" + dato)

        when (dato) {
            "logro1" -> {
                base.setValue(logro1)
            }
            "logro2" ->{
                base.setValue(logro2)
            }
            "logro3" ->{
                base.setValue(logro3)
            }
            "logro4" ->{
                base.setValue(logro4)
            }
            "logro5" ->{
                base.setValue(logro5)
            }
            "logro6" ->{
                base.setValue(logro6)
            }
            "logro7" ->{
                base.setValue(logro7)
            }
            "logro8" ->{
                base.setValue(logro8)
            }
            "logro9" ->{
                base.setValue(logro9)
            }
            "logro10" ->{
                base.setValue(logro10)
            }
            "logro11" ->{
                base.setValue(logro11)
            }
            "logro12" ->{
                base.setValue(logro12)
            }
        }
    }
}
