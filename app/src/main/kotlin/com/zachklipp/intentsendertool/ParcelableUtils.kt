package com.zachklipp.intentsendertool

import android.os.Parcelable
import android.os.Parcel
import java.util.ArrayList

inline fun <reified T : Parcelable> getCreatorFor(): Parcelable.Creator<T> =
    T::class.java.getCreator()

fun <T : Parcelable> Class<T>.getCreator(): Parcelable.Creator<T> =
    getField("CREATOR").get(null) as Parcelable.Creator<T>

fun <T : Parcelable> Parcel.readParcelable(): T = readParcelable(null)

inline fun <T : Parcelable, reified E : Parcelable> Parcel.readTypedList(): List<E> {
  val list = ArrayList<E>()
  readTypedList(list, getCreatorFor())
  return list
}
