package com.sena.libreria.entity

import android.os.Parcel
import android.os.Parcelable

data class fine(
    var id: Int,
    var fecha_multa: String,
    var valor_multa: Int,
    var estado: Int,
    var prestamo_id: Int,
    var usuario_id: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fecha_multa)
        parcel.writeInt(valor_multa)
        parcel.writeInt(estado)
        parcel.writeInt(prestamo_id)
        parcel.writeInt(usuario_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<fine> {
        override fun createFromParcel(parcel: Parcel): fine {
            return fine(parcel)
        }

        override fun newArray(size: Int): Array<fine?> {
            return arrayOfNulls(size)
        }
    }
}

