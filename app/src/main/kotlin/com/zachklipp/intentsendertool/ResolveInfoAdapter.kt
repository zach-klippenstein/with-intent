package com.zachklipp.intentsendertool

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class ResolveInfoAdapter(private val mContext: Context) : BaseAdapter() {
  private var mInfos: List<ResolveInfo>? = null

  init {
    setInfos(null)
  }

  fun setInfos(infos: List<ResolveInfo>?) {
    mInfos = infos

    if (mInfos != null) {
      notifyDataSetChanged()
    } else {
      notifyDataSetInvalidated()
    }
  }

  override fun getCount(): Int {
    return if (mInfos != null) mInfos!!.size else 0
  }

  override fun getItem(pos: Int): Any? {
    return if (mInfos != null) mInfos!![pos] else null
  }

  override fun getItemId(pos: Int): Long {
    return pos.toLong()
  }

  override fun getView(pos: Int, convertView: View, parent: ViewGroup): View {
    val view = convertView as IntentTargetView

    view.setResolveInfo(mInfos!![pos])

    return view
  }
}
