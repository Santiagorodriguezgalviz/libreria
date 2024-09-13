

function listarMulta() {
  var busqueda = document.getElementById("buscar").value;
  var urlBusqueda = urlMulta;
  if (busqueda != "") {
    urlBusqueda += "?search=" + busqueda;
  }
    $.ajax({
        url: urlBusqueda,
        type: "GET",
        success: function(result) {
            console.log(result);
            let cuerpoTablaMulta = document.getElementById("cuerpoTablaMulta");
            cuerpoTablaMulta.innerHTML = "";
            for (let i = 0; i < result.length; i++) {
                let trRegistro = document.createElement("tr");
                let celdaId = document.createElement("td");
                let celdaValorMulta = document.createElement("td");
                let celdaFechaMulta = document.createElement("td");
                let celdaUsuarioMultado = document.createElement("td");
                let celdaEstadoPrestamo = document.createElement("td");
                let celdaEstadoMulta = document.createElement("td");
                

                celdaId.innerText = result[i]["id"];
                celdaValorMulta.innerText = result[i]["valor_multa"];
                celdaFechaMulta.innerText = result[i]["fecha_multa"];
                celdaUsuarioMultado.innerText = result[i]["usuario_multado"];
                celdaEstadoPrestamo.innerText = result[i]["prestamo"];
                celdaEstadoMulta.innerText = result[i]["estado_multa"];
                // obtenerNombreUsuario(result[i]["usuario_multado"], celdaUsuarioMultado);
                // obtenerEstadoPrestamo(result[i]["prestamo"], celdaEstadoPrestamo);
                

                trRegistro.appendChild(celdaId);
                trRegistro.appendChild(celdaValorMulta);
                trRegistro.appendChild(celdaFechaMulta);
                trRegistro.appendChild(celdaUsuarioMultado);
                trRegistro.appendChild(celdaEstadoPrestamo);
                trRegistro.appendChild(celdaEstadoMulta);

                let celdaOpcion = document.createElement("td");
                let botonEditarMulta = document.createElement("button");
                botonEditarMulta.value = result[i]["id"];
                botonEditarMulta.innerHTML ="<i class='fa-solid fa-pencil'></i>";

                botonEditarMulta.onclick = function(e) {
                    $('#exampleModal').modal('show');
                    CargarFormulario();
                    consultarMultaID(this.value);
                }
                botonEditarMulta.className = "btn btn-success";

                celdaOpcion.appendChild(botonEditarMulta);
                trRegistro.appendChild(celdaOpcion);

                let botonEliminarMulta = document.createElement("button");
                botonEliminarMulta.innerHTML =  "<i class='fas fa-trash-alt eliminar'></i>"; 
                botonEliminarMulta.className = "btn btn-danger";

                let multaIdParaEliminar = result[i]["id"];
                botonEliminarMulta.onclick = function() {
                    eliminarMulta(multaIdParaEliminar);
                }
                celdaOpcion.appendChild(botonEliminarMulta);
                trRegistro.appendChild(celdaOpcion);

                cuerpoTablaMulta.appendChild(trRegistro);
            }
        },
        error: function(error) {
            alert("Error en la petición: " + error);
        }
    });
}





function RegistrarMulta() {
    let valor_multa = document.getElementById("valor_multa").value;
    let fecha_multa = document.getElementById("fecha_multa").value;
    let usuario_multado = document.getElementById("usuario_multado").value;
    let prestamo = document.getElementById("prestamo").value;
    let estado_multa = document.getElementById("estado_multa").value;

    let formData = {
        "valor_multa": valor_multa,
        "fecha_multa": fecha_multa,
        "usuario_multado": usuario_multado,
        "prestamo": prestamo,
        "estado_multa": estado_multa
        
    };
    // console.log(formData);
    if (validarCampos()) {
        $.ajax({
            url: urlMulta,
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
    }
        else{
         // alert("llena los campos correctamente")
          Swal.fire({
            title: "Error!",
            text: "complete los campos correctamente",
            icon: "error"
          });
        }
  }


  function validarCampos() {
   
    var valor_multa = document.getElementById("valor_multa");
    var fecha_multa = document.getElementById("fecha_multa");
    var usuario_multado = document.getElementById("usuario_multado"); 
    var prestamo = document.getElementById("prestamo"); 
    var estado_multa = document.getElementById("estado_multa"); 
 
 
   
    

    return validarValorMulta(valor_multa) && validarFechaMulta(fecha_multa) && validarUsuarioMulta(usuario_multado) 
    && validarPrestamoMulta(prestamo) && validarEstadoMulta(estado_multa);
}

function validarValorMulta(valorMulta) {
    if (!valorMulta || !valorMulta.value) {
        return false;
    }

    let valor = valorMulta.value;
    let valido = true;
    if (valor.length < 1 || valor.length > 6) {
        valido = false;
    }

    if (valido) {
        valorMulta.className = "form-control is-valid";
    } else {
        valorMulta.className = "form-control is-invalid";
    }
    return valido;
}


function validarFechaMulta(fechaMulta) {
    if (!fechaMulta || !fechaMulta.value) {
        return false;
    }

    let valor = fechaMulta.value;
    let valido = true;
    if (valor.length < 1 || valor.length > 60) {
        valido = false;
    }

    if (valido) {
        fechaMulta.className = "form-control is-valid";
    } else {
        fechaMulta.className = "form-control is-invalid";
    }
    return valido;
}


function validarUsuarioMulta(usuarioMulta){
    var valido=true;
    if(usuarioMulta.value.length <=0 || usuarioMulta.value.length > 45){
        valido=false;
    }

    if (valido) {
        usuarioMulta.className = "form-control is-valid"
    }
    else{
        usuarioMulta.className = "form-control is-invalid"
    }
    return valido;
}

function validarPrestamoMulta(prestamoMulta){
    var valido=true;
    if(prestamoMulta.value.length <=0 || prestamoMulta.value.length > 20){
        valido=false;
    }

    if (valido) {
        prestamoMulta.className = "form-control is-valid"
    }
    else{
        prestamoMulta.className = "form-control is-invalid"
    }
    return valido;
}

function validarEstadoMulta(estado){
    var valido=true;
    if(estado.value.length <= 0 || estado.value.length > 20){
        valido=false;
    }

    if (valido) {
        estado.className = "form-control is-valid"
    }
    else{
        estado.className = "form-control is-invalid"
    }
    return valido;
}



/* metodo para obtener los datos en el modal de actualizar*/ 
//1.Crear petición que traiga la información del medico por id
function consultarMultaID(id){
    //alert(id);
    $.ajax({
        url:urlMulta+id,
        type:"GET",
        success: function(result){
            document.getElementById("id").value=result["id"];
            document.getElementById("valor_multa").value = result["valor_multa"];
            document.getElementById("fecha_multa").value=result["fecha_multa"];
            document.getElementById("usuario_multado").value=result[ "usuario_multado"];
            document.getElementById("prestamo").value=result[ "prestamo"];
            document.getElementById("estado_multa").value=result[ "estado_multa"];
  
        }
    });
  }

function LimpiarMulta(){
    document.getElementById("valor_multa").className="form-control";
    document.getElementById("fecha_multa").className="form-control";
    document.getElementById("usuario_multado").className="form-control";
    document.getElementById("prestamo").className="form-control";
    document.getElementById("estado_multa").className="form-control";
    

    document.getElementById("valor_multa").value = "";
    document.getElementById("fecha_multa").value = "";
    document.getElementById("usuario_multado").value = "";
    document.getElementById("prestamo").value = "";
    document.getElementById("estado_multa").value = "";
 
 
}


// funcion  de deshabilitar prestamo
function eliminarMulta(id){
    swal.fire({
      title: '¿Estás seguro?',
      text: "Esta opción no tiene marcha atrás",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor:'#3085d6',
      cancelButtonText:'Cancelar',
      cancelButtonColor:'#d33',
      confirmButtonText:'Sí, !Eliminar!',
  
    }).then((result)=>{
      if (result.isConfirmed){
        $.ajax({
          url: urlMulta +id,
          type: "DELETE",
          success: function(result){
            swal.fire(
              'Eliminado',
              'La multa ha sido eliminada',
              'success'
            );
            listarMulta();//recarga la lista de usuarios
          },
          error: function(error){
            Swal.fire(
              'Error',
              'No se puede eliminar el registro ',
              'Error',
            );
          }
        });
    }
   });
}

// function cargarPrestamo(id, celdaEstadoPrestamo) {
//     // Hacer una petición AJAX para obtener el título del libro
//     $.ajax({
       
//         url: 'http://192.168.1.43:8000/libreria/api/v1/prestamo'+ '/' + id + '/',  // Ajusta la URL según tu configuración
//         type: 'GET',
//         success: function (prestamo) {
//             celdaEstadoPrestamo.innerText = prestamo.estado_prestamo;
            
//         },
//         error: function (error) {
//             console.error('Error obteniendo estado del préstamo: ', error);
//         }
//     });
// }


function CargarFormulario() {
    cargarUsuario();
    //cargarPrestamo();
}
//Función para traer los libros
// function cargarPrestamo() {
//     let urlPrestamo = "http://192.168.1.43:8000/libreria/api/v1/prestamo/";

//     $.ajax({
//         url: urlPrestamo,
//         type: "GET",
//         success: function (result) {
//             let prestamo = document.getElementById("prestamo");
//             prestamo.innerHTML ='<option selected disabled value="">Estado préstamo...</option>'; // Añadir opción por defecto
            
//             for (let i = 0; i < result.length; i++) {
//                 let estadoPrestamo = document.createElement("option");
//                 estadoPrestamo.value = result[i]["id"];
//                 estadoPrestamo.innerText = result[i]["Estado"];
//                 prestamo.appendChild(estadoPrestamo);
//             }
//         },
//     });
// }

function cargarUsuario() {
    let urlusuario = urlUsuario;
  
    $.ajax({
      url: urlusuario,
      type: "GET",
      success: function (result) {
        let usuario_multado = document.getElementById("usuario_multado");
        usuario_multado.innerHTML ="";
  
        // se crea un option para que aparesca el mensaje de seleccionae su usuario
        let texto = document.createElement("option");
        texto.value = "";
        texto.innerText ="Seleccione una opción";
        texto.disabled=true;
        texto.selected=true;
        usuario_multado.appendChild(texto);
  
        for (let i = 0; i < result.length; i++) {
          let usuario = document.createElement("option");
          usuario.value = result[i]["id"];
          usuario.innerText = nombre_completo_usuario =
            result[i]["nombre"]
            usuario_multado.appendChild(usuario);
  
        }
      },
    });
  }
  


//Cuando le damos click al boton de guardar, este llamara a la function UpdateProducto por medio del onclick******

function updateMulta() {
    var id = document.getElementById("id").value;
    consultarMultaID(id);
    let formData = {
        "valor_multa": document.getElementById("valor_multa").value,
        "fecha_multa": document.getElementById("fecha_multa").value,
        "usuario_multado": document.getElementById("usuario_multado").value,
        "prestamo": document.getElementById("prestamo").value,
        "estado_multa": document.getElementById("estado_multa").value
    };


    //Cuando estamos actualizando los datos, y lo hacemos correctamente Aparecerá la Alerta EXCELENTE ***
    if(validarCampos()){
        $.ajax({
            url: urlMulta + id+"/",
            type: "PUT",
            data: formData,
            success: function(result) {
                Swal.fire({
                    title: "Excelente",
                    text: "Su registro se actualizó correctamente",
                    icon: "success"
                });
                
                var modal = document.getElementById("exampleModal"); 
                modal.style.display = "hide";
    
                listarMulta(); //Lista los médicos después de actualizar
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