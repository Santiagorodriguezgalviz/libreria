from django.db import models

# Create your models here.

# 1) models.CharField = Para un capo de texto
# 2) max_length = Indica la longitud mazima del campo
# 3) blank=true = Indica que el campo acepta valores nulos
# 4) default = indica el valor por defecto del campo
# 5) IntegerField = para numeros enteros 

# integerChoices: para valores fijos nuemricos ejemplo 0 signifique activado 1 desactivasdo 

#models.Model este decirle que es una entidad 

class libro(models.Model): 
    titulo= models.CharField(max_length=200)
    autor=models.CharField(max_length=60)
    isbn= models.CharField(max_length=13)
    genero=models.CharField(max_length=25)
    num_ejem_disponible=models.IntegerField(max_length=3) #max_length= digitos que va a tener
    num_ejem_ocupados= models.IntegerField(max_length=3) 
    
    def __str__(self):
        return self.autor
    
class usuario(models.Model):
    nombre=models.CharField(max_length=60)
    direccion=models.CharField(max_length=50)
    correo=models.CharField(max_length=150)
    tipo_usuario = [
        (1, 'lector'),
        (2, 'bibliotecario'),
        (3, 'administrador') 
    ]
    tipoUsuario=models.IntegerField(choices=tipo_usuario)    
    
    def __str__(self):
        return self.nombre
    
class prestamo(models.Model):
    fecha_prestamo=models.DateField()
    fecha_devolucion=models.DateField()
    estado = [
        (1, 'Préstamo'),
        (2, 'Entregado'),
        (3, 'Cancelado') 
    ]
    Estado=models.IntegerField(choices=estado)
    usuario_prestamo = models.ForeignKey(usuario, related_name='prestamo', on_delete=models.PROTECT)
    libro_prestamo = models.ForeignKey(libro, related_name='prestamo', on_delete=models.CASCADE)

    
    def _str_(self):
       return self.title
    
class multa(models.Model):
    fecha_multa = models.DateField()
    usuario_multado = models.ForeignKey(usuario,related_name='multa', on_delete=models.CASCADE)
    prestamo = models.ForeignKey(prestamo, related_name='multa', on_delete=models.CASCADE)
    valor_multa = models.IntegerField()
    estado_multa = [
        (1, 'Pendiente'),
        (2, 'Pagado'),
    ]
    estado_multa = models.IntegerField(choices=estado_multa)
    
    def _str_(self):
        return f"Multa de {self.valor_multa} para {self.usuario_multado}"
       




        
    
    