package com.zachklipp.intentsendertool

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.content.pm.ServiceInfo
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.bindView

class IntentTargetView(context: Context, attrs: AttributeSet? = null)
: FrameLayout(context, attrs) {

  private val mResolvedIcon: ImageView by bindView(R.id.resolved_icon)
  private val mResolvedLabel: TextView by bindView(R.id.resolved_label_text)
  private val mResolvedName: TextView by bindView(R.id.resolved_name_text)

  private var mInfo: ResolveInfo? = null

  init {
    LayoutInflater.from(getContext()).inflate(R.layout.view_resolve_info, this)

    setOnClickListener { onCopyNameClick() }

    setResolveInfo(null)
  }

  fun setResolveInfo(info: ResolveInfo?) {
    mInfo = info
    updateView(info)
  }

  private fun updateView(info: ResolveInfo?) {
    if (info != null) {
      mResolvedIcon.setImageDrawable(info.loadIcon(context.packageManager))
      mResolvedIcon.contentDescription = getString(
          R.string.resolved_icon_description, getComponentDescription(), info.componentInfo.name)
      mResolvedLabel.text = info.loadLabel(context.packageManager)
      mResolvedName.text = info.componentInfo.name
      visibility = View.VISIBLE
    } else {
      visibility = View.GONE
      mResolvedIcon.setImageDrawable(null)
      mResolvedIcon.contentDescription = null
      mResolvedLabel.text = null
      mResolvedName.text = null
    }
  }

  private fun getComponentDescription() = when (mInfo) {
    is ActivityInfo -> getString(R.string.activity)
    is ServiceInfo -> getString(R.string.service)
    null -> null
    else -> javaClass.name
  }

  private fun onCopyNameClick() {
    val name = mInfo?.componentInfo?.name

    if (name != null) {
      copyText(getString(R.string.resolved_name_label), name)
      showToast(getString(R.string.copied_name_toast, name), Toast.LENGTH_SHORT)
    }
  }
}
