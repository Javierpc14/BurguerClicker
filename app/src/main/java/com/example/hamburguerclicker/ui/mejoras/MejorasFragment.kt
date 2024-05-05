package com.example.hamburguerclicker.ui.mejoras

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentMejorasBinding
import com.example.hamburguerclicker.modelo.Mejora
import com.example.hamburguerclicker.modelo.Tienda

class MejorasFragment : Fragment() {
    private var _binding: FragmentMejorasBinding? = null
    private val binding get() = _binding!!

    private lateinit var tituloMejoras : TextView

    private lateinit var layoutMejoras: LinearLayout

    lateinit var mejoras: ArrayList<Mejora>
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMejorasBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        layoutMejoras = root.findViewById(R.id.layoutMejoras)
        tituloMejoras = root.findViewById(R.id.tituloMejoras)

        tituloMejoras.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.navigation_tienda)
        }

        for(i in 1 .. 20){
            val layoutMejora= layoutInflater.inflate(R.layout.mejora, null)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            layoutMejoras.addView(layoutMejora, params)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        mDatabase.removeEventListener(valueListener)
    }


}