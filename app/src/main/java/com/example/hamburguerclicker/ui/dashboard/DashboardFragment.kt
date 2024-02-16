package com.example.hamburguerclicker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hamburguerclicker.R
import com.example.hamburguerclicker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private var dinTotal=0;

    lateinit var btnCompraPanaderia: Button
    lateinit var txtTotPanaderia:TextView
    private var totalPanaderias=0

    lateinit var btnCompraCarne: Button
    lateinit var txtTotCarne:TextView
    private var totalCarnicerias=0

    lateinit var btnCompraQueseria: Button
    lateinit var txtTotQueseria:TextView
    private var totalQueserias=0

    lateinit var btnCompraLechuga: Button
    lateinit var txtTotLechuga:TextView
    private var totalLechugas=0

    lateinit var btnCompraHuerto: Button
    lateinit var txtTotHuerto:TextView
    private var totalHuertos=0

    lateinit var btnCompraBacon: Button
    lateinit var txtTotBacon:TextView
    private var totalBacon=0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init(root)

        // Ocultar la barra de acción (action bar)

        // Ocultar la barra de acción (action bar)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(root:View){
        btnCompraPanaderia=root.findViewById(R.id.btnCompraPanaderia)
        txtTotPanaderia=root.findViewById(R.id.txtTotPan)
        btnCompraPanaderia.setOnClickListener{
            comprarPanaderia()
        }

        btnCompraCarne=root.findViewById(R.id.btnCompraCarne)
        txtTotCarne=root.findViewById(R.id.txtTotCarne)
        btnCompraCarne.setOnClickListener{
            comprarCarniceria()
        }

        btnCompraQueseria=root.findViewById(R.id.btnCompraQueso)
        txtTotQueseria=root.findViewById(R.id.txtTotQueso)
        btnCompraQueseria.setOnClickListener{
            comprarQueseria()
        }

        btnCompraLechuga=root.findViewById(R.id.btnCompraLechuga)
        txtTotLechuga=root.findViewById(R.id.txtTotLechuga)
        btnCompraLechuga.setOnClickListener{
            comprarLechuga()
        }

        btnCompraHuerto=root.findViewById(R.id.btnCompraHuerto)
        txtTotHuerto=root.findViewById(R.id.txtTotHuerto)
        btnCompraHuerto.setOnClickListener{
            comprarHuerto()
        }

        btnCompraBacon=root.findViewById(R.id.btnCompraBacon)
        txtTotBacon=root.findViewById(R.id.txtTotBacon)
        btnCompraBacon.setOnClickListener{
            comprarBacon()
        }
    }
    private fun comprarPanaderia(){
        totalPanaderias++;
        txtTotPanaderia.setText(""+totalPanaderias)
    }

    private fun comprarCarniceria(){
        totalCarnicerias++;
        txtTotCarne.setText(""+totalCarnicerias)
    }
    private fun comprarQueseria(){
        totalQueserias++;
        txtTotQueseria.setText(""+totalQueserias)
    }
    private fun comprarLechuga(){
        totalLechugas++;
        txtTotLechuga.setText(""+totalLechugas)
    }
    private fun comprarHuerto(){
        totalHuertos++;
        txtTotHuerto.setText(""+totalHuertos)
    }
    private fun comprarBacon(){
        totalBacon++;
        txtTotBacon.setText(""+totalCarnicerias)
    }

    private  fun hayDinero(coste:Int):Boolean{
        return dinTotal>=coste
    }
}