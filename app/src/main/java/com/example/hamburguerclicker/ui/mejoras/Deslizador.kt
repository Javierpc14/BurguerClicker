package com.example.hamburguerclicker.ui.mejoras

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.navigation.Navigation
import com.example.hamburguerclicker.R

class Deslizador (var vista:View ):GestureDetector.OnGestureListener {
    lateinit var gestureDetector: GestureDetector
    var x2:Float =0.0f
    var x1:Float =0.0f
    var y2:Float =0.0f
    var y1:Float =0.0f



    companion object{
        const val MIN_DISTANCE =150
    }


    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }



    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // Calcular la diferencia en las coordenadas X
        val deltaX = e2.x - e1!!.x
        // Calcular la diferencia en las coordenadas Y
        val deltaY = e2.y - e1.y

        // Verificar si el movimiento es horizontal y hacia la izquierda
        if (Math.abs(deltaX) > Math.abs(deltaY) &&
            Math.abs(deltaX) > MIN_DISTANCE &&
            deltaX < 0 &&
            Math.abs(velocityX) > 200
        ) {
            // Se detectó un deslizamiento hacia la izquierda, realiza la acción correspondiente
            Navigation.findNavController(vista).navigate(R.id.navigation_tienda)
            return true
        }
        return false
    }
}