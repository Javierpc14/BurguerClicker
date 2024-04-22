package com.example.hamburguerclicker.ui.logros

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hamburguerclicker.modelo.Logro

class LogroLayout(context: Context,logro: Logro) : LinearLayout(context) {

    //Inicializacion del componente de un logro
    init {
        //Se define un layout horizontal , donde van todos los demas componentes
        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 100, 0, 0)
        }
        setBackgroundColor(Color.parseColor("#41000000"))

        // Imagen del logro
        val imageView = ImageView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(
                507,
                350
            ).apply {
                setMargins(50, 15, 0, 0)
                weight = 1f
            }
            setImageResource(logro.imagenId)
        }
        addView(imageView)

        //Creacion de un layout vertical, para mostrar los dos componentes de texto, el requisito y la descripcion
        val layoutTexto = LinearLayout(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                weight = 1f
            }
            orientation = VERTICAL
        }
        //Variable que representa el requisito
        val requisito = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(
                500,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(180, 10, 0, 0)
                weight = 1f
            }
            text = logro.requisito
            gravity = Gravity.CENTER
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }
        //Añadimos el requisito
        layoutTexto.addView(requisito)

        val descripcion = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(
                500,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(180, 10, 0, 0)
                weight = 1f
            }
            text = logro.descripcion
            gravity = Gravity.CENTER
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }
        //Añadimos la descripcion

        layoutTexto.addView(descripcion)

        //Añadimos el layoutTexto al principal
        addView(layoutTexto)
    }
}

