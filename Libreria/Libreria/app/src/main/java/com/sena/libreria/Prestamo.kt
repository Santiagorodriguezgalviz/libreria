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
import com.sena.libreria.entity.loan

// TODO: Rename parameter arguments, choose names that match
/**
 * A simple [Fragment] subclass.
 * Use the [Prestamo.newInstance] factory method to
 * create an instance of this fragment.
 */
class Prestamo : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GenericAdapter<loan>
    private var prestamos = mutableListOf<loan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prestamo, container, false)

        val txtFiltro: EditText = view.findViewById(R.id.txtFiltro)
        val btnBsucar: Button = view.findViewById(R.id.btnBuscar)

        btnBsucar.setOnClickListener{
            val filtro = txtFiltro.text.toString()
            if(filtro.isNullOrEmpty()){
                cargar_lista_prestamo()
            }else{
                cargar_lista_prestamo(filtro)
            }
        }

        val btnCambiarFragmento: Button = view.findViewById(R.id.btnCambiarFragmento)
        btnCambiarFragmento.setOnClickListener {
            val otroFragmento = PrestamoFormulario.newInstance(null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, otroFragmento)
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.listPrestamo)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = GenericAdapter(
            items = prestamos,
            layoutResId = R.layout.item,
            bindView = { view, prestamo ->
                val item1: TextView = view.findViewById(R.id.fecha_multa)
                val item2: TextView = view.findViewById(R.id.valor_multa)
                val item3: TextView = view.findViewById(R.id.prestamo)
                val item4: TextView = view.findViewById(R.id.usuario_multado)
                val item5: TextView = view.findViewById(R.id.estado_multa)

                item1.text = "Fecha: " + prestamo.fecha_prestamo
                item2.text = " - " + prestamo.fecha_devolucion
                item3.text = "Usuario: " + prestamo.usuario_id + " "
                item4.text = "Libro: " + prestamo.libro_id
                item5.text = "Estado: " + prestamo.estado

                val btnEdit: Button = view.findViewById(R.id.btnEdit)
                val btnDelete: Button = view.findViewById(R.id.btnClear)

                btnEdit.setOnClickListener {
                    Detalle_Prestamo(prestamo.id)
                }
                btnDelete.setOnClickListener {
                    eliminar_prestamo(prestamo.id)
                }
            }
        )
        recyclerView.adapter = adapter
        cargar_lista_prestamo()

        return view
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            Prestamo().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun cargar_lista_prestamo(filtro: String? = null) {
        try {
            val url = if (filtro != null) {
                "${urls.urlPrestamo}?search=$filtro"
            } else {
                urls.urlPrestamo
            }
            val request = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    prestamos.clear()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val loan = loan(
                            id = item.getInt("id"),
                            fecha_prestamo = item.getString("fecha_prestamo"),
                            fecha_devolucion = item.getString("fecha_devolucion"),
                            estado = item.getInt("Estado"),
                            usuario_id = item.getInt("usuario_prestamo"),
                            libro_id = item.getInt("libro_prestamo")
                        )
                        prestamos.add(loan)
                    }
                    adapter.updateData(prestamos)
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
                "Error al cargar usuarios: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun eliminar_prestamo(id: Int) {
        try {
            val request = JsonObjectRequest(
                Request.Method.DELETE,
                "${urls.urlPrestamo}$id/",
                null,
                { response ->
                    cargar_lista_prestamo()
                    Toast.makeText(context, "Prestamo eliminado: $id", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    cargar_lista_prestamo()
                    Toast.makeText(context, "Prestamo eliminado: ${error}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(
                context,
                "Error al eliminar Prestamo: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun Detalle_Prestamo(id: Int){
        try{
            val request = JsonObjectRequest(
                Request.Method.GET,
                "${urls.urlPrestamo}$id/",
                null,
                { response ->
                    val loan = loan(
                        id = response.getInt("id"),
                        fecha_prestamo = response.getString("fecha_prestamo"),
                        fecha_devolucion = response.getString("fecha_devolucion"),
                        estado = response.getInt("Estado"),
                        usuario_id = response.getInt("usuario_prestamo"),
                        libro_id = response.getInt("libro_prestamo")
                    )
                    val otroFragmento = PrestamoFormulario.newInstance(loan)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, otroFragmento)
                        .addToBackStack(null)
                        .commit()
                }, { error ->
                    Toast.makeText(
                        context,
                        "Error al cargar el detalle del Prestamo: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception){
            Toast.makeText(
                context,
                "Error al cargar el detalle del Usuario: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}