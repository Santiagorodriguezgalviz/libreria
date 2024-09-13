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
import com.sena.libreria.entity.User

/**
 * A simple [Fragment] subclass.
 * Use the [User.newInstance] factory method to
 * create an instance of this fragment.
 */
class User : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GenericAdapter<User>
    private var usuarios = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val txtFiltro: EditText = view.findViewById(R.id.txtFiltro)
        val btnBsucar: Button = view.findViewById(R.id.btnBuscar)

        btnBsucar.setOnClickListener{
            val filtro = txtFiltro.text.toString()
            if(filtro.isNullOrEmpty()){
                cargar_lista_usuarios()
            }else{
                cargar_lista_usuarios(filtro)
            }
        }

        val btnCambiarFragmento: Button = view.findViewById(R.id.btnCambiarFragmento)
        btnCambiarFragmento.setOnClickListener {
            val otroFragmento = UserFormulario.newInstance(null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, otroFragmento)
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.listUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = GenericAdapter(
            items = usuarios,
            layoutResId = R.layout.item,
            bindView = { view, user ->
                val item1: TextView = view.findViewById(R.id.fecha_multa)
                val item2: TextView = view.findViewById(R.id.prestamo)

                item1.text = "Nombre: " + user.nombre
                item2.text = "Direccion: " + user.direccion

                val btnEdit: Button = view.findViewById(R.id.btnEdit)
                val btnDelete: Button = view.findViewById(R.id.btnClear)

                btnEdit.setOnClickListener {
                    Detalle_Usuario(user.id)
                }
                btnDelete.setOnClickListener {
                    eliminar_usuario(user.id)
                }
            }
        )
        recyclerView.adapter = adapter
        cargar_lista_usuarios()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaUsuarios.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            User().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun cargar_lista_usuarios(filtro: String? = null) {
        try {
            val url = if (filtro != null) {
                "${urls.urlUsuario}?search=$filtro"
            } else {
                urls.urlUsuario
            }
            val request = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    usuarios.clear()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val user = User(
                            id = item.getInt("id"),
                            nombre = item.getString("nombre"),
                            direccion = item.getString("direccion"),
                            correo = item.getString("correo"),
                            tipoUser = item.getInt("tipoUsuario")
                        )
                        usuarios.add(user)
                    }
                    adapter.updateData(usuarios)
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

    private fun eliminar_usuario(id: Int) {
        try {
            val request = JsonObjectRequest(
                Request.Method.DELETE,
                "${urls.urlUsuario}$id/",
                null,
                { response ->
                    cargar_lista_usuarios()
                    Toast.makeText(context, "Usuario eliminado: $id", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    cargar_lista_usuarios()
                    Toast.makeText(context, "Usuario eliminado: ${error}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(
                context,
                "Error al eliminar Usuario: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun Detalle_Usuario(id: Int){
        try{
            val request = JsonObjectRequest(
                Request.Method.GET,
                "${urls.urlUsuario}$id/",
                null,
                { response ->
                    val user = User(
                        id = response.getInt("id"),
                        nombre = response.getString("nombre"),
                        direccion = response.getString("direccion"),
                        correo = response.getString("correo"),
                        tipoUser = response.getInt("tipoUsuario")
                    )
                    val otroFragmento = UserFormulario.newInstance(user)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, otroFragmento)
                        .addToBackStack(null)
                        .commit()
                }, { error ->
                    Toast.makeText(
                        context,
                        "Error al cargar el detalle del Usuario: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }catch (error: Exception){
            Toast.makeText(
                context,
                "Error al cargar el detalle del Usuario: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}