from django.shortcuts import render
from rest_framework import viewsets, filters, status
from rest_framework.response import Response
from rest_framework.decorators import api_view

from .serializer import libroSerializer
from .models import libro

from .serializer import usuarioSerializer
from .models import usuario

from .serializer import prestamoSerializer
from .models import prestamo

from .serializer import multaSerializer
from .models import multa

# Create your views here.

#se crea la clase view por cada modelo
class libroView(viewsets.ModelViewSet):
    serializer_class=libroSerializer
    queryset=libro.objects.all()
    #para filtro de buscar 
    filter_backends=[filters.SearchFilter]
    search_fields=['$titulo', '$autor', '$genero', '$isbn']
    #modificamos la respuesta del eliminar para que la api responda algo cuando haga la accion 
    @api_view(['DELETE'])
    def eliminarLIbro(request, pk):
        try:
            libro = libro.objects.get(pk=pk)
        except libro.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

        if request.method == 'DELETE':
            libro.delete()
            return Response(status=status.HTTP_204_NO_CONTENT,message="se elimino correctamente")       
    
class usuarioView(viewsets.ModelViewSet):
    serializer_class=usuarioSerializer
    queryset=usuario.objects.all()
    filter_backends=[filters.SearchFilter]
    search_fields=['$nombre', '$correo']
    
class prestamoView(viewsets.ModelViewSet):
    serializer_class=prestamoSerializer
    queryset=prestamo.objects.all()

class multaView(viewsets.ModelViewSet):
    serializer_class=multaSerializer
    queryset=multa.objects.all()