package com.zachklipp.intentsendertool

import android.os.Parcelable
import android.os.Parcel
import java.util.ArrayList

inline fun getCreatorFor<reified T : Parcelable>(): Parcelable.Creator<T> =
    javaClass<T>().getCreator()

fun <T : Parcelable> Class<T>.getCreator(): Parcelable.Creator<T> =
    getField("CREATOR").get(null) as Parcelable.Creator<T>

fun Parcel.readParcelable<T : Parcelable>(): T = readParcelable(null)

inline fun Parcel.readTypedList<T : Parcelable, reified E : Parcelable>(): List<E> {
  val list = ArrayList<E>()
  readTypedList(list, getCreatorFor())
  return list
}
