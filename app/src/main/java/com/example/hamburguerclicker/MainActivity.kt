package com.example.hamburguerclicker

import android.os.Bundle
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

    var pesoTotal=900.0
    var unidadPeso="Mili Gramos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtValorPeso=findViewById(R.id.txtValorPeso)
        hamburguesa=findViewById(R.id.hamburguesa)
        unidad=findViewById(R.id.unidadPeso)
        btnRestar=findViewById(R.id.restar)

        hamburguesa.setOnClickListener(){
            pesoTotal+=100
            txtValorPeso.setText(""+pesoTotal)
            unidad()
        }

        btnRestar.setOnClickListener(){
            pesoTotal-=100
            txtValorPeso.setText(""+pesoTotal)
            unidad()
        }

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

    private fun unidad(){

        when(unidadPeso){
            "Mili Gramos" ->{
                if(pesoTotal>=1000){
                    unidadPeso="Gramos"
                    pesoTotal=pesoTotal/1000
                    unidad.setText(unidadPeso)
                    txtValorPeso.setText(""+pesoTotal)
                }
            }
            "Gramos"->{
                if(pesoTotal>=1000){
                    unidadPeso="Kilos"
                    pesoTotal=pesoTotal/1000
                    unidad.setText(unidadPeso)
                    txtValorPeso.setText(""+pesoTotal)
                }else if(pesoTotal<1){
                    unidadPeso="Mili Gramos"
                    pesoTotal=pesoTotal*-10
                    unidad.setText(unidadPeso)
                    txtValorPeso.setText(""+pesoTotal)
                }
            }
        }

    }
}