package com.zachklipp.intentsendertool

import android.content.pm.ResolveInfo
import android.os.Parcel
import android.os.Parcelable

data class IntentTargets(
    val primaryResult: ResolveInfo? = null,
    val otherResults: List<ResolveInfo>? = null) : Parcelable {

  init {
    require(primaryResult != null || otherResults != null)
  }

  override fun describeContents(): Int = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeParcelable(primaryResult, 0)
    writeTypedList(otherResults)
  }

  object CREATOR : Parcelable.Creator<IntentTargets> {
    override fun createFromParcel(source: Parcel): IntentTargets? =
        IntentTargets(
            primaryResult = source.readParcelable(),
            otherResults = source.readTypedList<ResolveInfo>()
        )

    override fun newArray(size: Int): Array<out IntentTargets?>? = arrayOfNulls(size)
  }
}
