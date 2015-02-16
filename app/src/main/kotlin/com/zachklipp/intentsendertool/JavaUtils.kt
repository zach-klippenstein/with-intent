package com.zachklipp.intentsendertool

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.Modifier.isPublic
import java.lang.reflect.Modifier.isStatic


/**
 * Returns the values of all the public static fields of the class that match a regex.
 */
fun Class<*>.getValuesOfStaticStringFieldsMatching(pattern: String): Collection<String> =
    getConstants<String>()
        .filter { it.getName().matches(pattern) }
        .map { it.getOrNull(null) as String }
        .filterNotNull()

/** Returns all the constants of type T. */
inline fun Class<*>.getConstants<reified T>() =
    getFields().filter { it.isConstant() && it.isA<T>() }

/** Returns true if the field is public, static, and final. */
fun Field.isConstant() = getModifiers().matchesAll(
    Modifier::isPublic, Modifier::isStatic, Modifier::isFinal)

/** Returns true if the field's type is T. */
inline fun Field.isA<reified T>() = getType() == javaClass<T>()

/** Like {@link Field#get(Any)} but returns null if an exception is thrown. */
fun Field.getOrNull(target: Any?) = try {
  get(target)
} catch (e: Exception) {
  null
}

fun <T> T.matchesAll(vararg preds: (T) -> Boolean) = preds.all { it(this) }
