package com.zachklipp.intentsendertool

import java.util.Locale

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Modifier
import java.lang.reflect.Modifier.isPublic
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.Field
import android.content.pm.ResolveInfo
import android.content.pm.ComponentInfo
import android.widget.Toast
import android.support.annotation.StringRes


fun String.asNormalizedUri(): Uri = normalizeUri(Uri.parse(this))

/**
 * @see android.net.Uri#normalizeScheme()
 */
fun normalizeUri(uri: Uri): Uri {
  val normalized: Uri

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    normalized = uri.normalizeScheme()
  } else {
    normalized = Uri.fromParts(uri.getScheme().toLowerCase(Locale.US), uri.getSchemeSpecificPart(), uri.getFragment())
  }

  return normalized
}

/**
 * @see android.content.Intent#normalizeMimeType(String)
 */
fun String.normalizeMimeType(): String {
  val normalized: String

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    normalized = Intent.normalizeMimeType(this)
  } else {
    normalized = this.toLowerCase(Locale.US)
  }

  return normalized
}

val ResolveInfo.componentInfo: ComponentInfo
  get() = array(activityInfo, serviceInfo, providerInfo).filterNotNull().single() as ComponentInfo

/**
 * Create a new ClipData holding data of the type MIMETYPE_TEXT_PLAIN and post it on the clipboard.
 */
fun View.copyText(label: String, text: String) = getContext().copyText(label, text)

/**
 * Create a new ClipData holding data of the type MIMETYPE_TEXT_PLAIN and post it on the clipboard.
 */
fun Context.copyText(label: String, text: String) {
  val clipboardService = getSystemService(Context.CLIPBOARD_SERVICE)
  val clipboard = clipboardService as android.content.ClipboardManager
  val clip = android.content.ClipData.newPlainText(label, text)
  clipboard.setPrimaryClip(clip)
}

fun View.hideSoftKeyboardFromView() {
  val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(getWindowToken(), 0)
}

fun View.getString(StringRes resId: Int, vararg formatArgs: Any?) =
    getResources().getString(resId, *formatArgs)

fun View.showToast(text: CharSequence, duration: Int) = getContext().showToast(text, duration)

fun Context.showToast(text: CharSequence, duration: Int) =
  Toast.makeText(this, text, duration).show()
