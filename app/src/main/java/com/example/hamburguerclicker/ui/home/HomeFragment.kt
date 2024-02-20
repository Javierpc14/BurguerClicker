package com.example.hamburguerclicker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var txtValorPeso: TextView
    lateinit var unidad: TextView
    lateinit var hamburguesa: ImageView
    lateinit var btnRestar:Button
    lateinit var btnCompraCarne:Button
    var a=R.integer.contador

    var pesoTotal=0.00

    var unidadPeso="Mili Gramos"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        txtValorPeso=root.findViewById(R.id.txtValorPeso)
        hamburguesa=root.findViewById(R.id.hamburguesa)
        unidad=root.findViewById(R.id.unidadPeso)
        btnRestar=root.findViewById(R.id.restar)

        txtValorPeso.setText(String.format("%.2f", pesoTotal))

        hamburguesa.setOnClickListener(){
//            pesoTotal+=100
//            txtValorPeso.setText(String.format("%.2f", pesoTotal))
//            unidad()

            pesoTotal += 100

            when {
                pesoTotal >= 1000 && unidadPeso == "Mili Gramos" -> {
                    unidadPeso = "Gramos"
                    pesoTotal /= 1000.0
                }
                pesoTotal >= 1000 && unidadPeso == "Gramos" -> {
                    unidadPeso = "Kilos"
                    pesoTotal /= 1000.0
                }
                pesoTotal >= 1000 && unidadPeso == "Kilos" -> {
                    unidadPeso = "Toneladas"
                    pesoTotal /= 1000.0
                }
            }

            txtValorPeso.setText(String.format("%.2f", pesoTotal))
            unidad.setText(unidadPeso)
        }



        btnRestar.setOnClickListener {
            //para que no pueda restar mas si hay 0 mg y asi no hay numeros negativos
            if (unidadPeso == "Mili Gramos" && pesoTotal <= 0) {
                return@setOnClickListener
            }


            pesoTotal -= 100

            if (pesoTotal < 0) {
                when (unidadPeso) {
                    "Gramos" -> {
                        pesoTotal += 1000
                        unidadPeso  = "Mili Gramos"
                    }
                    "Kilos" -> {
                        pesoTotal += 1000
                        unidadPeso = "Gramos"
                    }
                    "Toneladas" -> {
                        pesoTotal += 1000
                        unidadPeso = "Kilos"
                    }
                }
            }

            txtValorPeso.setText(String.format("%.2f", pesoTotal))
            unidad.setText(unidadPeso)
//            unidad()
        }


        // Ocultar la barra de acción (action bar)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun unidad() {
//        when (unidadPeso) {
//            "Mili Gramos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Gramos"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Gramos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Kilos"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                } else if (pesoTotal < 1) {
//                    unidadPeso = "Mili Gramos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Kilos" -> {
//                if (pesoTotal >= 1000) {
//                    unidadPeso = "Toneladas"
//                    pesoTotal /= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                } else if (pesoTotal < 1) {
//                    unidadPeso = "Gramos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//            "Toneladas" -> {
//                if (pesoTotal < 1) {
//                    unidadPeso = "Kilos"
//                    pesoTotal *= 1000
//                    unidad.setText(unidadPeso)
//                    txtValorPeso.setText(String.format("%.2f", pesoTotal))
//                }
//            }
//        }
//    }

}