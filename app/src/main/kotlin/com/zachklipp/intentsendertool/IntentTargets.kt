package com.zachklipp.intentsendertool

import android.content.pm.ResolveInfo
import android.os.Parcelable
import android.os.Parcel
import kotlin.reflect.KMemberProperty
import java.util.ArrayList

public data class IntentTargets (
    public val primaryResult: ResolveInfo? = null,
    public val otherResults: List<ResolveInfo>? = null) : Parcelable {

  {
    require(primaryResult != null || otherResults != null)
  }

  override fun describeContents(): Int = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeParcelable(primaryResult, 0)
    writeTypedList(otherResults)
  }

  public object CREATOR : Parcelable.Creator<IntentTargets> {
    override fun createFromParcel(source: Parcel): IntentTargets? =
        IntentTargets(
            primaryResult = source.readParcelable(),
            otherResults = source.readTypedList<IntentTargets, ResolveInfo>()
        )

    override fun newArray(size: Int): Array<out IntentTargets?>? = arrayOfNulls(size)
  }
}