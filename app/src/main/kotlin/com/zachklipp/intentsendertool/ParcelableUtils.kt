package com.zachklipp.intentsendertool

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

fun <T : Parcelable> Parcel.readParcelable(): T = readParcelable(null)

inline fun <reified E : Parcelable> Parcel.readTypedList(): List<E> {
  val list = ArrayList<E>()
  readTypedList(list, getCreatorFor())
  return list
}

inline fun <reified T : Parcelable> getCreatorFor(): Parcelable.Creator<T> =
    T::class.java.getCreator()

@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> Class<T>.getCreator(): Parcelable.Creator<T> =
    getField("CREATOR").get(null) as Parcelable.Creator<T>