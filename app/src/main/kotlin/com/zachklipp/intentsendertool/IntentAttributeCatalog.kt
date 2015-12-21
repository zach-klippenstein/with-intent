package com.zachklipp.intentsendertool

import android.content.Intent
import java.util.SortedSet
import kotlin.properties.Delegates
import java.lang.reflect.Field

val androidIntentActions: SortedSet<String> by lazy {
  Intent::class.java.getValuesOfStaticStringFieldsMatching("^ACTION_.*").toSortedSet()
}

val androidIntentCategories: SortedSet<String> by lazy {
  Intent::class.java.getValuesOfStaticStringFieldsMatching("^CATEGORY_.*").toSortedSet()
}
