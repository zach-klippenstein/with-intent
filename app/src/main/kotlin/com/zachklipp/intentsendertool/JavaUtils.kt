package com.zachklipp.intentsendertool

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.text.Regex


/**
 * Returns the values of all the public static fields of the class that match a regex.
 */
fun Class<*>.getValuesOfStaticStringFieldsMatching(pattern: String): Collection<String> =
    getConstants<String>()
        .filter { it.name.matches(Regex(pattern)) }
        .map { it.getOrNull(null) as String }
        .filterNotNull()

/** Returns all the constants of type T. */
inline fun <reified T : Any> Class<*>.getConstants() =
    fields.filter { it.isConstant() && it.isA<T>() }

/** Returns true if the field is public, static, and final. */
fun Field.isConstant() = modifiers.matchesAll(
    Modifier::isPublic, Modifier::isStatic, Modifier::isFinal)

/** Returns true if the field's type is T. */
inline fun <reified T : Any> Field.isA() = type == T::class.java

/** Like {@link Field#get(Any)} but returns null if an exception is thrown. */
fun Field.getOrNull(target: Any?) = try {
  get(target)
} catch (e: Exception) {
  null
}

fun <T> T.matchesAll(vararg preds: (T) -> Boolean) = preds.all { it(this) }
