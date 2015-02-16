package com.zachklipp.intentsendertool

import android.content.Intent
import java.util.SortedSet
import kotlin.properties.Delegates
import java.lang.reflect.Field

val androidIntentActions: SortedSet<String> by Delegates.lazy {
  javaClass<Intent>().getValuesOfStaticStringFieldsMatching("^ACTION_.*").toSortedSet()
}

val androidIntentCategories: SortedSet<String> by Delegates.lazy {
  javaClass<Intent>().getValuesOfStaticStringFieldsMatching("^CATEGORY_.*").toSortedSet()
}