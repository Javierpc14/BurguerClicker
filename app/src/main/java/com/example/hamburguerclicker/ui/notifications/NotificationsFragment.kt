package com.example.hamburguerclicker.ui.notifications

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.MainActivity
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.ActivityMainBinding
import com.example.hamburguerclicker.databinding.FragmentNotificationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // esto es para signarle los colores de la clase colors.xml al boton
        val color = ContextCompat.getColor(requireContext(), R.color.botonCrearPartida)
        val colorStateList = ColorStateList.valueOf(color)
        // aqui le pongo estilo al boton
        val btnVolverPartidas = Button(requireContext())
        btnVolverPartidas.text = "Volver a la lista de partidas"
        btnVolverPartidas.setTextColor(Color.WHITE)
        btnVolverPartidas.typeface = Typeface.create("sans-serif", Typeface.BOLD)
        btnVolverPartidas.backgroundTintList = colorStateList
        btnVolverPartidas.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        // esto es para crear los parametros de margins que tendra el boton
        val parametrosLayout = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        parametrosLayout.bottomToTop = R.id.layoutPartidas
        parametrosLayout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        parametrosLayout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        parametrosLayout.topToBottom = R.id.text_home
        btnVolverPartidas.layoutParams = parametrosLayout

        btnVolverPartidas.setOnClickListener {
            // Iniciar la actividad MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        binding.root.addView(btnVolverPartidas)


        //        val btnVolverPartidas = Button(_this)
//        btnVolverPartidas.text = "Volver a la lista de partidas"
//        btnVolverPartidas.setTextColor(Color.WHITE)
//        btnVolverPartidas.typeface = Typeface.create("sans-serif", Typeface.BOLD)
//       // btnVolverPartidas.backgroundTintList = colorStateList
//        btnVolverPartidas.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//        btnVolverPartidas.setOnClickListener {
//
//        }

//        <Button
//        android:id="@+id/btnVolverPartidas"
//        android:layout_width="336dp"
//        android:layout_height="56dp"
//        android:text="Volver a la lista de partidas"
//        app:layout_constraintBottom_toTopOf="@+id/layoutPartidas"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toBottomOf="@+id/text_home" />


        val textView: TextView = binding.textHome
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}