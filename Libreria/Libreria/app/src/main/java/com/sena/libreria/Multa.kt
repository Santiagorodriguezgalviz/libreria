package com.sena.libreria

import GenericAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreria.config.urls
import com.sena.libreria.entity.fine

class Multa : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GenericAdapter<fine>
    private var multas = mutableListOf<fine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_multa, container, false)
        val txtFiltro: EditText = view.findViewById(R.id.txtFiltro)
        val btnBsucar: Button = view.findViewById(R.id.btnBuscar)

        btnBsucar.setOnClickListener{
            val filtro = txtFiltro.text.toString()
            if(filtro.isNullOrEmpty()){
                cargar_lista_multa()
            }else{
                cargar_lista_multa(filtro)
            }
        }
        val btnCambiarFragmento: Button = view.findViewById(R.id.btnCambiarFragmento)
        btnCambiarFragmento.setOnClickListener {
            val otroFragmento = MultaFormulario.newInstance(null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, otroFragmento)
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.listMulta)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = GenericAdapter(
            items = multas,
            layoutResId = R.layout.item,
            bindView = { view, multa ->
                val item1: TextView = view.findViewById(R.id.fecha_multa)
                val item2: TextView = view.findViewById(R.id.valor_multa)
                val item3: TextView = view.findViewById(R.id.prestamo)
                val item4: TextView = view.findViewById(R.id.usuario_multado)

                item1.text = "Fecha Multa: " + multa.fecha_multa
                item2.text = "Valor: " + multa.valor_multa
                item3.text = "Usuario: " + multa.usuario_id
                item4.text = "Estado: " + multa.estado

                val btnEdit: Button = view.findViewById(R.id.btnEdit)
                val btnDelete: Button = view.findViewById(R.id.btnClear)

                btnEdit.setOnClickListener {
                    Detalle_Multa(multa.id)
                }
                btnDelete.setOnClickListener {
                    eliminar_multa(multa.id)
                }
            }
        )
        recyclerView.adapter = adapter
        cargar_lista_multa()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Multa().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun cargar_lista_multa(filtro: String? = null) {
        try {
            val url = if (filtro != null) {
                "${urls.urlMulta}?search=$filtro"
            } else {
                urls.urlMulta
            }
            val request = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    multas.clear()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val fine = fine(
                            id = item.getInt("id"),
                            fecha_multa = item.getString("fecha_multa"),
                            valor_multa = item.getInt("valor_multa"),
                            estado = item.getInt("estado_multa"),
                            usuario_id = item.getInt("usuario_multado"),
                            prestamo_id = item.getInt("prestamo")
                        )
                        multas.add(fine)
                    }
                    adapter.updateData(multas)
                    adapter.notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(
                context,
                "Error al cargar multas: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun eliminar_multa(id: Int) {
        try {
            val request = JsonObjectRequest(
                Request.Method.DELETE,
                "${urls.urlMulta}$id/",
                null,
                { response ->
                    cargar_lista_multa()
                    Toast.makeText(context, "Multa eliminada: $id", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    cargar_lista_multa()
                    Toast.makeText(context, "Error al eliminar la multa: ${error}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(
                context,
                "Error al eliminar multa: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun Detalle_Multa(id: Int){
        try{
            val request = JsonObjectRequest(
                Request.Method.GET,
                "${urls.urlMulta}$id/",
                null,
                { response ->
                    val fine = fine(
                        id = response.getInt("id"),
                        fecha_multa = response.getString("fecha_multa"),
                        valor_multa = response.getInt("valor_multa"),
                        estado = response.getInt("estado_multa"),
                        usuario_id = response.getInt("usuario_multado"),
                        prestamo_id = response.getInt("prestamo")
                    )
                    val otroFragmento = MultaFormulario.newInstance(fine)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, otroFragmento)
                        .addToBackStack(null)
                        .commit()
                }, { error ->
                    Toast.makeText(
                        context,
                        "Error al cargar el detalle de la multa: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception){
            Toast.makeText(
                context,
                "Error al cargar el detalle de la multa: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
