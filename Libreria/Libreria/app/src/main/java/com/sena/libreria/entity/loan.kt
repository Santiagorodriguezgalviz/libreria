package com.sena.libreria.entity

import android.os.Parcel
import android.os.Parcelable

data class loan(
    var id: Int,
    var fecha_prestamo: String,
    var fecha_devolucion: String,
    var estado: Int,
    var libro_id: Int,
    var usuario_id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fecha_prestamo)
        parcel.writeString(fecha_devolucion)
        parcel.writeInt(estado)
        parcel.writeInt(libro_id)
        parcel.writeInt(usuario_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<loan> {
        override fun createFromParcel(parcel: Parcel): loan {
            return loan(parcel)
        }

        override fun newArray(size: Int): Array<loan?> {
            return arrayOfNulls(size)
        }
    }
}
