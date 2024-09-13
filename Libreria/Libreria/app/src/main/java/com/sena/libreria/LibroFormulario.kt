package com.sena.libreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreria.config.urls
import com.sena.libreria.entity.book
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [LibroFormulario.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val ARG_BOOK = "book"

class LibroFormulario : Fragment() {
    // TODO: Rename and change types of parameters
    private var book: book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable(ARG_BOOK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_libro_formulario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnVolver: Button = view.findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val txtTitulo = view.findViewById<EditText>(R.id.txtTitulo)
        val txtAutor = view.findViewById<EditText>(R.id.txtAutor)
        val txtIsbn = view.findViewById<EditText>(R.id.txtIsbn)
        val txtGenero = view.findViewById<EditText>(R.id.txtGenero)
        val txtEjemDisponible = view.findViewById<EditText>(R.id.txtEjemDisponible)
        val txtEjemOcupados = view.findViewById<EditText>(R.id.txtEjemOcupados)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        book?.let {
            txtTitulo.setText(it.titulo)
            txtAutor.setText(it.autor)
            txtIsbn.setText(it.isbn)
            txtGenero.setText(it.genero)
            txtEjemDisponible.setText(it.num_ejem_disponible.toString())
            txtEjemOcupados.setText(it.num_ejem_ocupados.toString())
            btnGuardar.text = "Actualizar"
        }

        btnGuardar.setOnClickListener {
            val titulo = txtTitulo.text.toString()
            val autor = txtAutor.text.toString()
            val isbn = txtIsbn.text.toString()
            val genero = txtGenero.text.toString()
            val num_ejem_disponible = txtEjemDisponible.text.toString().toIntOrNull() ?: 0
            val num_ejem_ocupados = txtEjemOcupados.text.toString().toIntOrNull() ?: 0
            if (book != null) {
                val nuevoLibro = book(
                    book!!.id,
                    titulo,
                    autor,
                    isbn,
                    genero,
                    num_ejem_disponible,
                    num_ejem_ocupados
                )
                actualizarLibro(nuevoLibro)
            } else {
                val nuevoLibro =
                    book(0, titulo, autor, isbn, genero, num_ejem_disponible, num_ejem_ocupados)
                guardarLibro(nuevoLibro)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment guardarLibroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(book: book?) =
            LibroFormulario().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BOOK, book)
                }
            }
    }

    fun guardarLibro(libro : book){
        try{
            var parametros = JSONObject()
            parametros.put("titulo", libro.titulo)
            parametros.put("autor", libro.autor)
            parametros.put("isbn", libro.isbn)
            parametros.put("genero", libro.genero)
            parametros.put("num_ejem_disponible", libro.num_ejem_disponible)
            parametros.put("num_ejem_ocupados", libro.num_ejem_ocupados)

            var request = JsonObjectRequest(
                Request.Method.POST,
                urls.urlLibro,
                parametros,
                { response ->
                    Toast.makeText(context, "Libro guardado correctamente", Toast.LENGTH_SHORT)
                        .show()
                },
                { error ->
                    Toast.makeText(context, "Error al guardar el libro", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        }catch(error : Exception){

        }
    }

    fun actualizarLibro(libro : book){
        try{
            var id = libro.id;
            var parametros = JSONObject()
            parametros.put("titulo", libro.titulo)
            parametros.put("autor", libro.autor)
            parametros.put("isbn", libro.isbn)
            parametros.put("genero", libro.genero)
            parametros.put("num_ejem_disponible", libro.num_ejem_disponible)
            parametros.put("num_ejem_ocupados", libro.num_ejem_ocupados)

            var request = JsonObjectRequest(
                Request.Method.PUT,
                "${urls.urlLibro}$id/",
                parametros,
                { response ->
                    Toast.makeText(context, "Libro actualizado correctamente", Toast.LENGTH_SHORT)
                        .show()
                },
                { error ->
                    Toast.makeText(context, "Error al actualizar el libro", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        }catch(error : Exception){
        }
    }

}