from rest_framework import serializers
#se importa el modulo rerializers

from .models import libro
#se importa la clase del modelo 
from .models import usuario
from .models import prestamo
from .models import multa

#se crea una clase realizer para cada entidad
#la clase meta dentro de un serializador sirve para
#proporcionar metadatos adicionales y configuraciones
#especificas para ese serializador
class libroSerializer(serializers.ModelSerializer):
 #agrega los campos necesarios a mostrar
 #si se desea agregar todos los campos se puede utilizar
 #la funcion__all__
    class Meta: 
        model=libro
        fields='__all__'
        #fields={
        #  'id'  
        #  'dni'
        #  'nombres'
        #  'fecha'
        #  'telefono'
        # }
class usuarioSerializer(serializers.ModelSerializer):
    class Meta:
        model= usuario
        fields='__all__'
        
#class PrestamoSerializer(serializers.ModelSerializer):
    #agregar los campos necesarios para mostrar
    #si de desea agregar todos los campos se puede utilizar la 
    #funcion _all_
    #libro = serializers.SlugRelatedField(slug_field='id', queryset=libro.objects.all())
    #usuario = serializers.SlugRelatedField(slug_field='id', queryset=usuario.objects.all())
    
  
    #class Meta:
    #    model = prestamo
    #    fields='_all_'    
       
class prestamoSerializer(serializers.ModelSerializer):
    class Meta:
        model= prestamo
        fields='__all__'
        
class multaSerializer(serializers.ModelSerializer):
    class Meta: 
        model= multa
        fields='__all__'
        

           