package com.sena.libreria

import android.os.Bundle
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
import com.sena.libreria.entity.fine
import org.json.JSONObject

private const val ARG_FINE = "fine"

class MultaFormulario : Fragment() {

    private var listUsers = mutableListOf<String>()
    private var userIdMap = HashMap<String, Int>()
    private var listLoans = mutableListOf<String>()
    private var loanIdMap = HashMap<String, Int>()
    private var userId: Int? = null
    private var loanId: Int? = null

    private var fineToEdit: fine? = null

    private lateinit var etfechamulta: EditText
    private lateinit var valormulta: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fineToEdit = it.getParcelable(ARG_FINE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_multa_formulario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnVolver: Button = view.findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        etfechamulta = view.findViewById(R.id.etfecha_multa)
        valormulta = view.findViewById(R.id.etvalor_multa)
        val estado = view.findViewById<Spinner>(R.id.etEstado)
        val etusuario = view.findViewById<AutoCompleteTextView>(R.id.etusuario_multa)
        val etprestamo = view.findViewById<AutoCompleteTextView>(R.id.etprestamo)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        if (fineToEdit != null) {
            etfechamulta.setText(fineToEdit?.fecha_multa)
            valormulta.setText(fineToEdit?.valor_multa.toString())
            estado.setSelection(fineToEdit!!.estado - 1)
            btnGuardar.text = "Actualizar"
        }

        btnGuardar.setOnClickListener {
            val fecha_multa = etfechamulta.text.toString()
            val valor_multa = valormulta.text.toString().toIntOrNull()
            val estadoId = estado.selectedItemId.toInt() + 1

            if (userId == null || loanId == null || valor_multa == null) {
                Toast.makeText(context, "Seleccione un usuario, préstamo y valor válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newFine = fineToEdit?.copy(
                fecha_multa = fecha_multa,
                valor_multa = valor_multa,
                estado = estadoId,
                usuario_id = userId!!,
                prestamo_id = loanId!!
            ) ?: fine(0, fecha_multa, valor_multa, estadoId, loanId!!, userId!!)

            if (fineToEdit != null) {
                actualizarMulta(newFine)
            } else {
                guardarMulta(newFine)
            }
        }

        etfechamulta.setOnClickListener {
            showDatePickerDialog(etfechamulta)
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

        cargar_prestamos { loanList, loanMap ->
            val loanAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                loanList
            )
            etprestamo.setAdapter(loanAdapter)

            etprestamo.setOnItemClickListener { parent, _, position, _ ->
                val selectedLoan = parent.getItemAtPosition(position) as String
                loanId = loanMap[selectedLoan]
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

    private fun cargar_prestamos(onComplete: (List<String>, HashMap<String, Int>) -> Unit) {
        try {
            val request = JsonArrayRequest(
                Request.Method.GET,
                urls.urlPrestamo,
                null,
                { response ->
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getInt("id")
                        val fecha_prestamo = item.getString("fecha_prestamo")
                        listLoans.add(fecha_prestamo)
                        loanIdMap[fecha_prestamo] = id
                    }
                    onComplete(listLoans, loanIdMap)
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error al cargar préstamos: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarMulta(fine: fine) {
        try {
            val parametros = JSONObject()
            parametros.put("fecha_multa", fine.fecha_multa)
            parametros.put("valor_multa", fine.valor_multa)
            parametros.put("estado_multa", fine.estado)
            parametros.put("usuario_multado", fine.usuario_id)
            parametros.put("prestamo", fine.prestamo_id)

            val request = JsonObjectRequest(
                Request.Method.POST,
                urls.urlMulta,
                parametros,
                { response ->
                    Toast.makeText(context, "Multa Guardada Exitosamente", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        } catch (error: Exception) {
            Toast.makeText(context, "Error al guardar la multa: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarMulta(fine: fine) {
        try {
            val parametros = JSONObject()
            parametros.put("fecha_multa", fine.fecha_multa)
            parametros.put("valor_multa", fine.valor_multa)
            parametros.put("estado_multa", fine.estado)
            parametros.put("usuario_multado", fine.usuario_id)
            parametros.put("prestamo", fine.prestamo_id)

            val request = JsonObjectRequest(
                Request.Method.PUT,
                urls.urlMulta + "${fine.id}/",
                parametros,
                { response ->
                    Toast.makeText(context, "Multa Actualizada Exitosamente", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    error.networkResponse?.let {
                        val statusCode = it.statusCode
                        val data = it.data?.let { data -> String(data) } ?: "Sin datos adicionales"
                        Toast.makeText(context, "Error $statusCode: $data", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        } catch (error: Exception) {
            Toast.makeText(context, "Error al actualizar la multa: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fineToEdit: fine?) =
            MultaFormulario().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FINE, fineToEdit)
                }
            }
    }
}
