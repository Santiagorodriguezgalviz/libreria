from django.contrib import admin
from .models import libro
from .models import usuario
from .models import prestamo
from .models import multa

admin.site.register(libro)
admin.site.register(usuario)
admin.site.register(prestamo)
admin.site.register(multa)


# Register your models here.
