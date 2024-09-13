package com.sena.libreria

import GenericAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreria.config.urls
import com.sena.libreria.entity.book

/**
 * A simple [Fragment] subclass.
 * Use the [Libro.newInstance] factory method to
 * create an instance of this fragment.
 */
class Libro : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GenericAdapter<book>
    private var libros = mutableListOf<book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_libro, container, false)
        val txtFiltro: EditText = view.findViewById(R.id.txtFiltro)
        val btnBsucar: Button = view.findViewById(R.id.btnBuscar)

        btnBsucar.setOnClickListener{
            val filtro = txtFiltro.text.toString()
            if(filtro.isNullOrEmpty()){
                cargar_lista_libros()
            }else{
                cargar_lista_libros(filtro)
            }
        }
        val btnCambiarFragmento: Button = view.findViewById(R.id.btnCambiarFragmento)
        btnCambiarFragmento.setOnClickListener {
            val otroFragmento = LibroFormulario.newInstance(null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, otroFragmento)
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.listLibros)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = GenericAdapter(
            items = libros,
            layoutResId = R.layout.item,
            bindView = { view, libro ->
                val item1: TextView = view.findViewById(R.id.fecha_multa)
                val item2: TextView = view.findViewById(R.id.prestamo)
                val item3: TextView = view.findViewById(R.id.estado_multa)

                item1.text = "Titulo: " + libro.titulo
                item2.text = "Autor: " + libro.autor
                item3.text = "Genero: " + libro.genero

                val btnEdit: Button = view.findViewById(R.id.btnEdit)
                val btnDelete: Button = view.findViewById(R.id.btnClear)

                btnEdit.setOnClickListener {
                    Detalle_libro(libro.id)
                }
                btnDelete.setOnClickListener {
                    eliminar_libro(libro.id)
                }
            }
        )
        recyclerView.adapter = adapter
        cargar_lista_libros()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Libro().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun cargar_lista_libros(filtro: String? = null) {
        try {
            val url = if (filtro != null) {
                "${urls.urlLibro}?search=$filtro"
            } else {
                urls.urlLibro
            }
            val request = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    libros.clear()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val libro = book(
                            id = item.getInt("id"),
                            titulo = item.getString("titulo"),
                            autor = item.getString("autor"),
                            isbn = item.getString("isbn"),
                            genero = item.getString("genero"),
                            num_ejem_disponible = item.getInt("num_ejem_disponible"),
                            num_ejem_ocupados = item.getInt("num_ejem_ocupados")
                        )
                        libros.add(libro)
                    }
                    adapter.updateData(libros)
                    adapter.notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error al cargar libros: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminar_libro(id: Int) {
        try {
            val request = JsonObjectRequest(
                Request.Method.DELETE,
                "${urls.urlLibro}$id/",
                null,
                { response ->
                    cargar_lista_libros()
                    Toast.makeText(context, "Libro eliminado: $id", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    cargar_lista_libros()
                    Toast.makeText(context, "Libro eliminado con exito.", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error al eliminar libro: ${error.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun Detalle_libro(id: Int){
        try{
            val request = JsonObjectRequest(
                Request.Method.GET,
                "${urls.urlLibro}$id/",
                null,
                { response ->
                    val libro = book(
                        id = response.getInt("id"),
                        titulo = response.getString("titulo"),
                        autor = response.getString("autor"),
                        isbn = response.getString("isbn"),
                        genero = response.getString("genero"),
                        num_ejem_disponible = response.getInt("num_ejem_disponible"),
                        num_ejem_ocupados = response.getInt("num_ejem_ocupados")
                    )
                    val otroFragmento = LibroFormulario.newInstance(libro)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, otroFragmento)
                        .addToBackStack(null)
                        .commit()
                }, { error ->
                    Toast.makeText(
                        context,
                        "Error al cargar el detalle del libro: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }catch (error: Exception){
            Toast.makeText(
                context,
                "Error al cargar el detalle del libro: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}