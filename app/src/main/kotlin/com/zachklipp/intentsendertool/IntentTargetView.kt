package com.zachklipp.intentsendertool

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.content.pm.ActivityInfo
import android.content.pm.ServiceInfo
import butterknife.bindView
import android.view.ViewGroup

public class IntentTargetView(context: Context, attrs: AttributeSet? = null)
: FrameLayout(context, attrs) {

  private val mResolvedIcon: ImageView by bindView(R.id.resolved_icon)
  private val mResolvedLabel: TextView by bindView(R.id.resolved_label_text)
  private val mResolvedName: TextView by bindView(R.id.resolved_name_text)

  private var mInfo: ResolveInfo? = null

  {
    LayoutInflater.from(getContext()).inflate(R.layout.view_resolve_info, this)

    setOnClickListener { onCopyNameClick() }

    setResolveInfo(null)
  }

  public fun setResolveInfo(info: ResolveInfo?) {
    mInfo = info
    updateView(info)
  }

  private fun updateView(info: ResolveInfo?) {
    if (info != null) {
      mResolvedIcon.setImageDrawable(info.loadIcon(getPackageManager()))
      mResolvedIcon.setContentDescription(getString(
          R.string.resolved_icon_description, getComponentDescription(), info.componentInfo.name))
      mResolvedLabel.setText(info.loadLabel(getPackageManager()))
      mResolvedName.setText(info.componentInfo.name)
      setVisibility(View.VISIBLE)
    } else {
      setVisibility(View.GONE)
      mResolvedIcon.setImageDrawable(null)
      mResolvedIcon.setContentDescription(null)
      mResolvedLabel.setText(null)
      mResolvedName.setText(null)
    }
  }

  private fun getComponentDescription() = when (mInfo) {
    is ActivityInfo -> getString(R.string.activity)
    is ServiceInfo -> getString(R.string.service)
    null -> null
    else -> javaClass.getName()
  }

  private fun onCopyNameClick() {
    val name = mInfo?.componentInfo?.name

    if (name != null) {
      copyText(getString(R.string.resolved_name_label), name)
      showToast(getString(R.string.copied_name_toast, name), Toast.LENGTH_SHORT)
    }
  }

  private fun getPackageManager(): PackageManager {
    return getContext().getPackageManager()
  }
}
