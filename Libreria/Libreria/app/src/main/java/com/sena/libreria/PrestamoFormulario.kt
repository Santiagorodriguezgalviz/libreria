package com.sena.libreria

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreria.components.DatePickerFragment
import com.sena.libreria.config.urls
import com.sena.libreria.entity.loan
import org.json.JSONObject

private const val ARG_LOAN = "loan"



class PrestamoFormulario : Fragment() {

    private var listUsers = mutableListOf<String>()
    private var userIdMap = HashMap<String, Int>()
    private var listBooks = mutableListOf<String>()
    private var bookIdMap = HashMap<String, Int>()
    private var userId: Int? = null
    private var bookId: Int? = null

    private var loanToEdit: loan? = null

    private lateinit var etfechaprestamo: EditText
    private lateinit var etfechadevolucion: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loanToEdit = it.getParcelable("loan")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prestamo_formulario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnVolver: Button = view.findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        etfechaprestamo = view.findViewById(R.id.etfecha_prestamo)
        etfechadevolucion = view.findViewById(R.id.etfecha_devolucion)
        val estado = view.findViewById<Spinner>(R.id.etEstado)
        val etusuario = view.findViewById<AutoCompleteTextView>(R.id.etusuario_prestamo)
        val etlibro = view.findViewById<AutoCompleteTextView>(R.id.etlibro_prestamo)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        if (loanToEdit != null) {
            etfechaprestamo.setText(loanToEdit?.fecha_prestamo)
            etfechadevolucion.setText(loanToEdit?.fecha_devolucion)
            estado.setSelection(loanToEdit!!.estado - 1)
            btnGuardar.text = "Actualizar"
        }

        btnGuardar.setOnClickListener {
            val fecha_prestamo = etfechaprestamo.text.toString()
            val fecha_devolucion = etfechadevolucion.text.toString()
            val estadoId = estado.selectedItemId.toInt() + 1

            if (userId == null || bookId == null) {
                Toast.makeText(context, "Seleccione un usuario y un libro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newLoan = loanToEdit?.copy(
                fecha_prestamo = fecha_prestamo,
                fecha_devolucion = fecha_devolucion,
                estado = estadoId,
                usuario_id = userId!!,
                libro_id = bookId!!
            ) ?: loan(0, fecha_prestamo, fecha_devolucion, estadoId, bookId!!, userId!!)

            if (loanToEdit != null) {
                actualizarPrestamo(newLoan)
            } else {
                guardarPrestamo(newLoan)
            }
        }

        etfechaprestamo.setOnClickListener {
            showDatePickerDialog(etfechaprestamo)
        }
        etfechadevolucion.setOnClickListener {
            showDatePickerDialog(etfechadevolucion)
        }

        cargar_usuarios { userList, userMap ->
            val userAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                userList
            )
            etusuario.setAdapter(userAdapter)

            etusuario.setOnItemClickListener { parent, _, position, _ ->
                val selectedUser = parent.getItemAtPosition(position) as String
                userId = userMap[selectedUser]
            }
        }

        cargar_libros { bookList, bookMap ->
            val bookAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                bookList
            )
            etlibro.setAdapter(bookAdapter)

            etlibro.setOnItemClickListener { parent, _, position, _ ->
                val selectedBook = parent.getItemAtPosition(position) as String
                bookId = bookMap[selectedBook]
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val datePicker = DatePickerFragment { day, month, year ->
            onDateSelected(day, month, year, editText)
        }
        datePicker.show(parentFragmentManager, "datapicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int, editText: EditText) {
        val formattedDay = if (day < 10) "0$day" else day.toString()
        val formattedMonth = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
        editText.setText("$year-$formattedMonth-$formattedDay")
    }

    private fun cargar_usuarios(onComplete: (List<String>, HashMap<String, Int>) -> Unit) {
        try {
            val request = JsonArrayRequest(
                Request.Method.GET,
                urls.urlUsuario,
                null,
                { response ->
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getInt("id")
                        val nombre = item.getString("nombre")
                        listUsers.add(nombre)
                        userIdMap[nombre] = id
                    }
                    onComplete(listUsers, userIdMap)
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error al cargar usuarios: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargar_libros(onComplete: (List<String>, HashMap<String, Int>) -> Unit) {
        try {
            val request = JsonArrayRequest(
                Request.Method.GET,
                urls.urlLibro,
                null,
                { response ->
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getInt("id")
                        val titulo = item.getString("titulo")
                        listBooks.add(titulo)
                        bookIdMap[titulo] = id
                    }
                    onComplete(listBooks, bookIdMap)
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

    private fun guardarPrestamo(loan: loan) {
        try {
            val parametros = JSONObject()
            parametros.put("fecha_prestamo", loan.fecha_prestamo)
            parametros.put("fecha_devolucion", loan.fecha_devolucion)
            parametros.put("Estado", loan.estado)
            parametros.put("usuario_prestamo", loan.usuario_id)
            parametros.put("libro_prestamo", loan.libro_id)

            val request = JsonObjectRequest(
                Request.Method.POST,
                urls.urlPrestamo,
                parametros,
                { response ->
                    Toast.makeText(context, "Registro Guardado Exitosamente", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        } catch (error: Exception) {
            Toast.makeText(context, "Error al guardar el préstamo: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarPrestamo(loan: loan) {
        try {
            val parametros = JSONObject()
            parametros.put("fecha_prestamo", loan.fecha_prestamo)
            parametros.put("fecha_devolucion", loan.fecha_devolucion)
            parametros.put("Estado", loan.estado)
            parametros.put("usuario_prestamo", loan.usuario_id)
            parametros.put("libro_prestamo", loan.libro_id)

            val request = JsonObjectRequest(
                Request.Method.PUT,
                urls.urlPrestamo + "${loan.id}" +"/",
                parametros,
                { response ->
                    Toast.makeText(context, "Registro Actualizado Exitosamente", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    error.networkResponse?.let {
                        val statusCode = it.statusCode
                        val data = it.data?.let { data -> String(data) } ?: "Sin datos adicionales"
                        Log.e("Error", "Código de estado: $statusCode, Respuesta: $data")
                        Toast.makeText(context, "Error $statusCode: $data", Toast.LENGTH_LONG).show()
                    } ?: run {
                        Log.e("Error", "Error desconocido: ${error.message}")
                        Toast.makeText(context, "Error desconocido: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        } catch (error: Exception) {
            Toast.makeText(context, "Error al actualizar el préstamo: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(loan: loan?) =
            PrestamoFormulario().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LOAN, loan)
                }
            }
    }
}
