package com.zachklipp.intentsendertool

import android.content.Context
import android.content.Intent
import android.content.pm.ComponentInfo
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.support.annotation.StringRes
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.Locale


fun String.asNormalizedUri(): Uri = normalizeUri(Uri.parse(this))

/**
 * @see android.net.Uri#normalizeScheme()
 */
fun normalizeUri(uri: Uri): Uri {
  val normalized: Uri

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    normalized = uri.normalizeScheme()
  } else {
    normalized = Uri.fromParts(uri.scheme.toLowerCase(Locale.US), uri.schemeSpecificPart, uri.fragment)
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
  get() = arrayOf(activityInfo, serviceInfo /*, providerInfo */)
      .filterNotNull()
      .single() as ComponentInfo

/**
 * Create a new ClipData holding data of the type MIMETYPE_TEXT_PLAIN and post it on the clipboard.
 */
fun View.copyText(label: String, text: String) = context.copyText(label, text)

/**
 * Create a new ClipData holding data of the type MIMETYPE_TEXT_PLAIN and post it on the clipboard.
 */
fun Context.copyText(label: String, text: String) {
  val clipboardService = getSystemService(Context.CLIPBOARD_SERVICE)
  val clipboard = clipboardService as android.content.ClipboardManager
  val clip = android.content.ClipData.newPlainText(label, text)
  clipboard.primaryClip = clip
}

fun View.hideSoftKeyboardFromView() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.getString(@StringRes resId: Int, vararg formatArgs: Any?) =
    resources.getString(resId, *formatArgs)

fun View.showToast(text: CharSequence, duration: Int) = context.showToast(text, duration)

fun Context.showToast(text: CharSequence, duration: Int) =
    Toast.makeText(this, text, duration).show()
