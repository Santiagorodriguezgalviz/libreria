package com.sena.libreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreria.config.urls
import com.sena.libreria.entity.User
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [guardarUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_USER = "user"

class UserFormulario : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_formulario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val btnVolver: Button = view.findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val txtNombre = view.findViewById<EditText>(R.id.etnombre)
        val txtDireccion = view.findViewById<EditText>(R.id.etdireccion)
        val txtCorreo = view.findViewById<EditText>(R.id.etcorreo)
        val txtTipoUsuario = view.findViewById<Spinner>(R.id.ettipoUsuario)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        user?.let {
            txtNombre.setText(it.nombre)
            txtDireccion.setText(it.direccion)
            txtCorreo.setText(it.correo)
            txtTipoUsuario.setSelection(it.tipoUser - 1)
        }

        btnGuardar.setOnClickListener{
            val nombre = txtNombre.text.toString()
            val direccion = txtDireccion.text.toString()
            val correo = txtCorreo.text.toString()
            val tipoUsuario = txtTipoUsuario.selectedItemId.toInt() +1

            if (user != null) {
                val newUser = User(user!!.id, nombre, direccion, correo, tipoUsuario)
                actualizarUsuario(newUser)
            } else {
                val newUser = User(0, nombre, direccion, correo, tipoUsuario)
                guardarUsuario(newUser)
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
         * @return A new instance of fragment guardarUsuario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User?) =
            UserFormulario().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }

    fun guardarUsuario(user : User){
        try{
            var parametros = JSONObject()
            parametros.put("nombre", user.nombre)
            parametros.put("direccion", user.direccion)
            parametros.put("correo", user.correo)
            parametros.put("tipoUsuario", user.tipoUser)

            var request = JsonObjectRequest(
                Request.Method.POST,
                urls.urlUsuario,
                parametros,
                { response ->
                    Toast.makeText(context, "Usuario guardado correctamente", Toast.LENGTH_SHORT)
                        .show()
                },
                { error ->
                    Toast.makeText(context, "Error al guardar el Usuario", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        }catch(error : Exception){

        }
    }

    fun actualizarUsuario(user : User){
        try{
            var id = user.id;
            // se crean los parametros
            var parametros = JSONObject()
            //parametros.put("nombre-variable", "valor-variable")
            parametros.put("nombre", user.nombre)
            parametros.put("direccion", user.direccion)
            parametros.put("correo", user.correo)
            parametros.put("tipoUsuario", user.tipoUser)

            var request = JsonObjectRequest(
                Request.Method.PUT,
                "${urls.urlUsuario}$id/",
                parametros,
                { response ->
                    Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT)
                        .show()
                },
                { error ->
                    Toast.makeText(context, "Error al actualizar el usuario", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        }catch(error : Exception){
        }
    }
}