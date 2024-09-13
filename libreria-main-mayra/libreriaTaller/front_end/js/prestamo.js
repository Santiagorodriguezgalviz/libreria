//se almacena la url de la api
//let url ="http://10.192.89.92:8000/libreria/api/v1/prestamo/";
function listarPrestamo() {
  cargarUsuario();
  cargarLibro();
  var busqueda = document.getElementById("buscar").value;
  var urlBusqueda = urlPrestamo;
  if (busqueda != "") {
    urlBusqueda += "?search=" + busqueda;
  }
  $.ajax({
    url: urlBusqueda,
    type: "GET",
    success: function (result) {//success: funcion que se ejecuta cusndo la peticion tiene exito
      console.log(result);
      let curpoTablaPrestamo = document.getElementById("curpoTablaPrestamo");
      curpoTablaPrestamo.innerHTML = "";
      for (let i = 0; i < result.length; i++) {
        //se crea una etiqueta tr por cada registro
        let trRegistro = document.createElement("tr");//fila por cada registro de la tabla
        let celdaId = document.createElement("td");
        let celdaFecha_prestamo = document.createElement("td");
        let celdaFecha_devolucion = document.createElement("td");
        let celdaEstado = document.createElement("td");
        let celdaUsuario_prestamo = document.createElement("td");
        let celdaLibro_prestamo = document.createElement("td");

        // let celdaEditar = document.createElement("td");

        //almacenamos en valor

        celdaId.innerText = result[i]["id"];
        celdaFecha_prestamo.innerText = result[i]["fecha_prestamo"];
        celdaFecha_devolucion.innerText = result[i]["fecha_devolucion"];
        celdaEstado.innerText = result[i]["Estado"];
        celdaUsuario_prestamo.innerText = result[i]["usuario_prestamo"];
        celdaLibro_prestamo.innerText = result[i]["libro_prestamo"];


        //agregando a los td a su respectivo th y agregandolos a la fila

        trRegistro.appendChild(celdaId);
        trRegistro.appendChild(celdaFecha_prestamo);
        trRegistro.appendChild(celdaFecha_devolucion);
        trRegistro.appendChild(celdaEstado);
        trRegistro.appendChild(celdaUsuario_prestamo);
        trRegistro.appendChild(celdaLibro_prestamo);

        //boton editar 
        let celdaOpcion = document.createElement("td");
        let botonEditarPrestamo = document.createElement("button");
        botonEditarPrestamo.value = result[i]["id"];
        botonEditarPrestamo.innerHTML = "<i class='fa-solid fa-pencil'></i>";


        botonEditarPrestamo.onclick = function (e) {
          $('#exampleModal').modal('show');
          consultarPrestamoID(this.value);
        }
        botonEditarPrestamo.className = "btn btn-success"

        celdaOpcion.appendChild(botonEditarPrestamo);
        trRegistro.appendChild(celdaOpcion);

        curpoTablaPrestamo.appendChild(trRegistro);//se traen todos los registros

        //boton Eliminar
        let botonEliminarPrestamo = document.createElement("button");
        botonEliminarPrestamo.innerHTML = "<i class='fas fa-trash-alt eliminar'></i>";
        botonEliminarPrestamo.className = "btn btn-danger";

        let prestamoIdParaEliminar = result[i]["id"];
        botonEliminarPrestamo.onclick = function () {
          eliminarPrestamo(prestamoIdParaEliminar); // Llama a la función eliminarProducto con el ID del producto
        };

        celdaOpcion.appendChild(botonEliminarPrestamo);
        trRegistro.appendChild(celdaOpcion);

      }
    },
    error: function (error) {
      alert("Error en la peticion ${error}");
    }
  })

}

//Paso para crear el registro de un cliente ****
function registrarPrestamo() {

  let fecha_prestamo = document.getElementById("fecha_prestamo").value;
  let fecha_devolucion = document.getElementById("fecha_devolucion").value;
  let Estado = document.getElementById("Estado").value;
  let usuario_prestamo = document.getElementById("usuario_prestamo").value;
  let libro_prestamo = document.getElementById("libro_prestamo").value;

  let formData = {

    "fecha_prestamo": fecha_prestamo,
    "fecha_devolucion": fecha_devolucion,
    "Estado": Estado,
    "usuario_prestamo": usuario_prestamo,
    "libro_prestamo": libro_prestamo
  };

  if(validarCampos()){

    $.ajax({
      url: urlPrestamo,
      type: "POST",
      data: formData,
      success: function(result){
        Swal.fire({
          title: "Excelente",
          text: "Su registro se guardó correctamente",
          icon: "success"
        });
        // window.location.href= "http://127.0.0.1:5500/front_end/clienteRegistro.html";
      },
      error: function(error){
        Swal.fire("Error", "Error al guardar ", "error");
      }
    });
  }else{
   // alert("llena los campos correctamente")
    Swal.fire({
      title: "Error!",
      text: "complete los campos correctamente",
      icon: "error"
    });
  }
}

function validarCampos() {
   
  var fecha_prestamo = document.getElementById("fecha_prestamo");
  var fecha_devolucion = document.getElementById("fecha_devolucion");
  var usuario_prestamo = document.getElementById("usuario_prestamo"); 
  var libro_prestamo = document.getElementById("libro_prestamo"); 
  

  return validarFechaPrestamo(fecha_prestamo) && validarFechaDevolucion(fecha_devolucion) && validarUsuarioPrestamo(usuario_prestamo) 
  && validarLibroPrestado(libro_prestamo);
}

function validarFechaPrestamo(fecha_prestamo) {
  if (!fecha_prestamo || !fecha_prestamo.value) {
      return false;
  }

  let valor = fecha_prestamo.value;
  let valido = true;
  if (valor.length < 1 || valor.length > 60) {
      valido = false;
  }

  if (valido) {
    fecha_prestamo.className = "form-control is-valid";
  } else {
    fecha_prestamo.className = "form-control is-invalid";
  }
  return valido;
}


function validarFechaDevolucion(fechaDevolucion) {
  if (!fechaDevolucion || !fechaDevolucion.value) {
      return false;
  }

  let valor = fechaDevolucion.value;
  let valido = true;
  if (valor.length < 1 || valor.length > 60) {
      valido = false;
  }

  if (valido) {
      fechaDevolucion.className = "form-control is-valid";
  } else {
      fechaDevolucion.className = "form-control is-invalid";
  }
  return valido;
}


function validarUsuarioPrestamo(usuarioPrestamo){
  var valido=true;
  if(usuarioPrestamo.value.length <=0 || usuarioPrestamo.value.length > 45){
      valido=false;
  }

  if (valido) {
      usuarioPrestamo.className = "form-control is-valid"
  }
  else{
      usuarioPrestamo.className = "form-control is-invalid"
  }
  return valido;
}

function validarLibroPrestado(libroPrestado){
  var valido=true;
  if(libroPrestado.value.length <=0 || libroPrestado.value.length > 100){
      valido=false;
  }

  if (valido) {
      libroPrestado.className = "form-control is-valid"
  }
  else{
      libroPrestado.className = "form-control is-invalid"
  }
  return valido;
}



function updatePrestamo(){
  var id = document.getElementById("id").value;

  let formData = {
    "fecha_prestamo": document.getElementById("fecha_prestamo").value,
    "fecha_devolucion": document.getElementById("fecha_devolucion").value,
    "Estado": document.getElementById("Estado").value,
    "usuario_prestamo": document.getElementById("usuario_prestamo").value,
    "libro_prestamo": document.getElementById("libro_prestamo").value,
     
  };


  //Cuando estamos actualizando los datos, y lo hacemos correctamente Aparecerá la Alerta EXCELENTE *****
  if(validarCampos()){
  $.ajax({
      url: urlPrestamo + id+"/",
      type: "PUT",
      data: formData,
      success: function(result) {
          Swal.fire({
              title: "Excelente",
              text: "Su registro se actualizó correctamente",
              icon: "success"
          });
          
          var modal = document.getElementById("exampleModal"); 
          modal.style.display = "hidde";

          listarPrestamo(); //Lista los médicos después de actualizar
      },
      error: function(error) {
          Swal.fire("Error", "Error al guardar", "error");
      }  
  });
  }else{
      Swal.fire({
          title: "Error!",
          text: "Complete los campos correctamente",
          icon: "error"
      });
  }
}

/* metodo para obtener los datos en el modal de actualizar*/
//1.Crear petición que traiga la información del cliente por id
function consultarPrestamoID(id) {
  //alert(id);
  $.ajax({
    url: urlPrestamo + id,
    type: "GET",
    success: function (result) {

      document.getElementById("id").value = result["id"];
      document.getElementById("fecha_prestamo").value = result["fecha_prestamo"];
      document.getElementById("fecha_devolucion").value = result["fecha_devolucion"];
      document.getElementById("Estado").value = result["Estado"];
      document.getElementById("usuario_prestamo").value = result["usuario_prestamo"];
      document.getElementById("libro_prestamo").value = result["libro_prestamo"];
    }
  });
}
function limpiar() {

  document.getElementById("id").className = "form-control";
  document.getElementById("fecha_prestamo").className = "form-control";
  document.getElementById("fecha_devolucion").className = "form-control";
  document.getElementById("Estado").className = "form-control";
  document.getElementById("usuario_prestamo").className = "form-control";
  document.getElementById("libro_prestamo").className = "form-control";


  // document.getElementById("tipo_Identificacion").value = "";
  document.getElementById("fecha_prestamo").value = "";
  document.getElementById("fecha_devolucion").value = "";
  document.getElementById("Estado").value = "";
  document.getElementById("usuario_prestamo").value = "";
  document.getElementById("libro_prestamo").value = "";

}


// funcion de  elimado permanente  
function eliminarPrestamo(id) {
  // Mostrar un cuadro de diálogo para confirmar la eliminación
  Swal.fire({
    title: '¿Estás seguro de eliminar este registro ?',
    text: "Esta opción no tiene marcha atrás",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, Eliminar!'
  }).then((result) => {
    // Si el usuario confirma la eliminación, proceder con la solicitud AJAX
    if (result.isConfirmed) {
      $.ajax({
        url: urlPrestamo + id + "/",
        type: "DELETE",
        success: function (eliminarPermanente) {
          // Mostrar un mensaje de éxito
          Swal.fire({
            position: "top-end",
            icon: "success",
            title: "Registro Eliminado",
            showConfirmButton: false,
            timer: 1500
          });
          // Actualizar la lista de cliente después de eliminar
          listarPrestamo();
        },
        error: function (xhr, status, error) {
          // Manejo de errores
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El registro tiene un ingreso.'
          });
        }
      });
    }
  });
};

//cargra usuarios y libros 
function CargarFormulario() {
  cargarUsuario();
  cargarLibro();
  // cargarEstado();
}
//funcion para traer los usuarios
function cargarUsuario() {
  let urlusuario = urlUsuario;

  $.ajax({
    url: urlusuario,
    type: "GET",
    success: function (result) {
      let usuario_prestamo = document.getElementById("usuario_prestamo");
      usuario_prestamo.innerHTML = "";

      // se crea un option para que aparesca el mensaje de seleccionae su usuario
      let texto = document.createElement("option");
      texto.value = "";
      texto.innerText ="Seleccione una opción";
      texto.disabled=true;
      texto.selected=true;
      usuario_prestamo.appendChild(texto);

      for (let i = 0; i < result.length; i++) {
        let usuario = document.createElement("option");
        usuario.value = result[i]["id"];
        usuario.innerText = nombre_completo_usuario =
          result[i]["nombre"]
        usuario_prestamo.appendChild(usuario);

      }
    },
  });
}

function cargarLibro() {
  let urllibro = urlLibro;

  $.ajax({
    url: urllibro,
    type: "GET",
    success: function (result) {
      let libro_prestamo = document.getElementById("libro_prestamo");
      libro_prestamo.innerHTML = "";

      // se crea un option para que aparesca el mensaje de seleccionae su usuario
      let texto = document.createElement("option");
      texto.value = "";
      texto.innerText ="Seleccione una opción";
      texto.disabled=true;
      texto.selected=true;
      libro_prestamo.appendChild(texto);

      for (let i = 0; i < result.length; i++) {
        let libro = document.createElement("option");
        libro.value = result[i]["id"];
        libro.innerText = libro_titulo = result[i]["titulo"]
        libro_prestamo.appendChild(libro);

      }
    },
  });
}

