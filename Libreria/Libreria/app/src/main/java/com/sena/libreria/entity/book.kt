package com.sena.libreria.entity

import android.os.Parcel
import android.os.Parcelable

// se crean los atributos del modelo
data class book(
    var id: Int,
    var titulo: String,
    var autor: String,
    var isbn: String,
    var genero: String,
    var num_ejem_disponible: Int,
    var num_ejem_ocupados: Int

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(titulo)
        parcel.writeString(autor)
        parcel.writeString(isbn)
        parcel.writeString(genero)
        parcel.writeInt(num_ejem_disponible)
        parcel.writeInt(num_ejem_ocupados)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<book> {
        override fun createFromParcel(parcel: Parcel): book {
            return book(parcel)
        }

        override fun newArray(size: Int): Array<book?> {
            return arrayOfNulls(size)
        }
    }
}
