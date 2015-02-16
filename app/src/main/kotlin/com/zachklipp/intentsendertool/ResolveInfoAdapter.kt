package com.zachklipp.intentsendertool

import android.content.Context
import android.widget.BaseAdapter
import android.content.pm.ResolveInfo
import android.view.View
import android.view.ViewGroup

class ResolveInfoAdapter(private val mContext: Context) : BaseAdapter() {
  private var mInfos: List<ResolveInfo>? = null

  {
    setInfos(null)
  }

  public fun setInfos(infos: List<ResolveInfo>?) {
    mInfos = infos

    if (mInfos != null) {
      notifyDataSetChanged()
    } else {
      notifyDataSetInvalidated()
    }
  }

  override fun getCount(): Int {
    return if (mInfos != null) mInfos!!.size() else 0
  }

  override fun getItem(pos: Int): Any? {
    return if (mInfos != null) mInfos!!.get(pos) else null
  }

  override fun getItemId(pos: Int): Long {
    return pos.toLong()
  }

  override fun getView(pos: Int, convertView: View, parent: ViewGroup): View {
    val view = convertView as IntentTargetView ?: IntentTargetView(mContext)

    view.setResolveInfo(mInfos!!.get(pos))

    return view
  }
}