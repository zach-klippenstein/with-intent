package com.zachklipp.intentsendertool;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class Util
{
  @SuppressLint("NewApi")
  public static Uri normalizeUri(Uri uri)
  {
    Uri normalized;
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    {
      normalized = uri.normalizeScheme();
    }
    else
    {
      normalized = Uri.fromParts(
          uri.getScheme().toLowerCase(Locale.US),
          uri.getSchemeSpecificPart(),
          uri.getFragment());
    }
    
    return normalized;
  }
  
  @SuppressLint("NewApi")
  public static String normalizeMimeType(String type)
  {
    String normalized;
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    {
      normalized = Intent.normalizeMimeType(type);
    }
    else
    {
      normalized = type.toLowerCase(Locale.US);
    }
    
    return normalized;
  }
  
  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  public static void copyText(Context context, String label, String text)
  {
    Object clipboardService = context.getSystemService(Context.CLIPBOARD_SERVICE);
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    {
      android.content.ClipboardManager clipboard =
          (android.content.ClipboardManager) clipboardService;
      android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
      clipboard.setPrimaryClip(clip);
    }
    else
    {
      android.text.ClipboardManager clipboard = (android.text.ClipboardManager) clipboardService;
      clipboard.setText(text);
    }
  }
  
  public static void hideSoftKeyboardFromView(View view)
  {
    Context context = view.getContext();
    InputMethodManager imm = (InputMethodManager) context.getSystemService(
        Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  private Util()
  {}
}
