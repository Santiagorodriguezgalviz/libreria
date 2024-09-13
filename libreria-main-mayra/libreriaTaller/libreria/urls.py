from django.contrib import admin
from django.urls import path,include
from rest_framework.documentation import include_docs_urls
from rest_framework import routers

from libreria import views 
 
router_libro=routers.DefaultRouter()
router_libro.register(r'',views.libroView,'/libro')

router_usuario=routers.DefaultRouter()
router_usuario.register(r'',views.usuarioView,'/usuario')

router_prestamo=routers.DefaultRouter()
router_prestamo.register(r'',views.prestamoView,'/prestamo')

router_multa=routers.DefaultRouter()
router_multa.register(r'',views.multaView,'/multa')


urlpatterns = [
    path("api/v1/libro/", include(router_libro.urls)),
    path("api/v1/usuario/", include(router_usuario.urls)),
    path("api/v1/prestamo/", include(router_prestamo.urls)),
    path("api/v1/multa/", include(router_multa.urls)),
    path("docs/",include_docs_urls(title="libreria API"))
]

