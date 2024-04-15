package com.example.hamburguerclicker.ui.logros

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class LogrosFragment : Fragment() {

    private var _binding: FragmentLogrosBinding? = null

    lateinit var btnVolverPartidas: Button
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val logrosViewModel =
            ViewModelProvider(this).get(LogrosViewModel::class.java)

        _binding = FragmentLogrosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnVolverPartidas = root.findViewById(R.id.btnVolverPartidas)

        btnVolverPartidas.setOnClickListener {
            HomeFragment.timer?.cancel()
            HomeFragment.timer = null
            // Iniciar la actividad MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        val textView: TextView = binding.textHome
        logrosViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}