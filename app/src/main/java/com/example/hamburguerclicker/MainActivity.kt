package com.example.hamburguerclicker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hamburguerclicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

     lateinit var binding: ActivityMainBinding
     lateinit var txtValorPeso: TextView
     lateinit var unidad: TextView
     lateinit var hamburguesa:ImageView
     lateinit var btnRestar:Button
     lateinit var btnCompraCarne:Button
     var a=R.integer.contador

//aaa
//    var pesoTotal=0.0
//    var unidadPeso="Mili Gramos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion variables vistas
        txtValorPeso=findViewById(R.id.txtValorPeso)
        hamburguesa=findViewById(R.id.hamburguesa)
        unidad=findViewById(R.id.unidadPeso)
        btnRestar=findViewById(R.id.restar)


//        hamburguesa.setOnClickListener(){
//////            pesoTotal+=100
//////            txtValorPeso.setText(String.format("%.2f", pesoTotal))
//////            unidad()
////
////            pesoTotal += 100
////
////            when {
////                pesoTotal >= 1000 && unidadPeso == "Mili Gramos" -> {
////                    unidadPeso = "Gramos"
////                    pesoTotal /= 1000.0
////                }
////                pesoTotal >= 1000 && unidadPeso == "Gramos" -> {
////                    unidadPeso = "Kilos"
////                    pesoTotal /= 1000.0
////                }
////                pesoTotal >= 1000 && unidadPeso == "Kilos" -> {
////                    unidadPeso = "Toneladas"
////                    pesoTotal /= 1000.0
////                }
////            }
////
////            txtValorPeso.setText(String.format("%.2f", pesoTotal))
////            unidad.setText(unidadPeso)
////        }
////
////
////
////        btnRestar.setOnClickListener {
////            //para que no pueda restar mas si hay 0 mg y asi no hay numeros negativos
////            if (unidadPeso == "Mili Gramos" && pesoTotal <= 0) {
////                return@setOnClickListener
////            }
////
////
////            pesoTotal -= 100
////
////            if (pesoTotal < 0) {
////                when (unidadPeso) {
////                    "Gramos" -> {
////                        pesoTotal += 1000
////                        unidadPeso  = "Mili Gramos"
////                    }
////                    "Kilos" -> {
////                        pesoTotal += 1000
////                        unidadPeso = "Gramos"
////                    }
////                    "Toneladas" -> {
////                        pesoTotal += 1000
////                        unidadPeso = "Kilos"
////                    }
////                }
////            }
////
////            txtValorPeso.setText(String.format("%.2f", pesoTotal))
////            unidad.setText(unidadPeso)
//////            unidad()
////        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//    private fun unidad(){
//        when(unidadPeso){
//            "Mili Gramos" ->{
//                if(pesoTotal>=1000){
//                    unidadPeso="Gramos"
//                    pesoTotal=pesoTotal/1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(""+pesoTotal)
//                }
//            }
//            "Gramos"->{
//
//                if(pesoTotal>=1000){
//                    unidadPeso="Kilos"
//                    pesoTotal=pesoTotal/1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(""+pesoTotal)
//                }else if(pesoTotal<1){
//                    unidadPeso="Mili Gramos"
//                    pesoTotal=pesoTotal*-10
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(""+pesoTotal)
//                }
//            }
//            "Kilos"->{
//                if(pesoTotal>=1000){
//                    unidadPeso="Toneladas"
//                    pesoTotal=pesoTotal/1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(""+pesoTotal)
//                }else if(pesoTotal<1){
//                    unidadPeso="Gramos"
//                    pesoTotal=pesoTotal*-10
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(""+pesoTotal)
//                }
//            }
//            "Toneladas"-> {
//                if (pesoTotal < 1) {
//                    unidadPeso = "Kilos"
//                    pesoTotal = pesoTotal * -10
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText("" + pesoTotal)
//                }
//            }
//        }
//    }


}