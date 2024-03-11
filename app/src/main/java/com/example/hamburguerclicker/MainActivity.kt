package com.example.hamburguerclicker

import android.content.ContentValues
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hamburguerclicker.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {

     lateinit var binding: ActivityMainBinding
     lateinit var txtValorPeso: TextView
     lateinit var unidad: TextView
     lateinit var hamburguesa:ImageView
     lateinit var btnRestar:Button
     lateinit var btnCompraCarne:Button

     lateinit var _this:AppCompatActivity

    val database = FirebaseDatabase.getInstance()
    val mDatabase = database.getReference("partida")

     lateinit var  btnCrear:Button

     var a=R.integer.contador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.fragment_notifications)

        btnCrear = findViewById(R.id.btnCrearPartida)

        _this = this
        var value: HashMap<String, Object>?
        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value = snapshot.getValue<HashMap<String, Object>>()

                for (i in 0..3) {
                    val color = ContextCompat.getColor(_this, R.color.botonCrearPartida)
                    val colorStateList = ColorStateList.valueOf(color)
                    val prueba = Button(_this)

                    prueba.text = "Estegosaurio"
                    prueba.setTextColor(Color.WHITE)
                    prueba.typeface = Typeface.create("sans-serif", Typeface.BOLD)
                    prueba.backgroundTintList = colorStateList
                    prueba.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

                    val layout = findViewById<LinearLayout>(R.id.layoutPartidas)
                    layout.addView(prueba)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })


        // BOTON 1


        //BOTON 2
//        val color2 = ContextCompat.getColor(this, R.color.botonCrearPartida)
//        val colorStateList2 = ColorStateList.valueOf(color)
//        val prueba2 = Button(this)
//
//        prueba2.text = "Estegosaurio"
//        prueba2.setTextColor(Color.WHITE)
//        prueba2.typeface = Typeface.create("sans-serif", Typeface.BOLD)
//        prueba2.backgroundTintList = colorStateList
//        prueba2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//
//        layout.addView(prueba2)

        //Inicializacion variables vistas
//       txtValorPeso=findViewById(R.id.txtValorPeso)
//        hamburguesa=findViewById(R.id.hamburguesa)
//        unidad=findViewById(R.id.unidadPeso)
       // btnRestar=findViewById(R.id.restar)

        btnCrear.setOnClickListener {
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.navView

            val navController = findNavController(R.id.nav_host_fragment_activity_main)

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }


    }

}