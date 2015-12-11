package com.zachklipp.intentsendertool


import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Parcelable

public class IntentTargetsFragment : ListFragment() {

  private var mResults: IntentTargets? = null
  private var mAdapter: ResolveInfoAdapter? = null

  override fun onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mResults = arguments?.getParcelable<Parcelable>(KEY_RESULTS) as IntentTargets
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_intent_targets, container)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    mAdapter = ResolveInfoAdapter(activity)
    listAdapter = mAdapter

    // If results were changed after creation, we should load those instead of what we
    // got as arguments.
    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RESULTS)) {
      mResults = savedInstanceState.getParcelable<IntentTargets>(KEY_RESULTS)
    }
    setResults(mResults)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelable(KEY_RESULTS, mResults)
  }

  public fun setResults(results: IntentTargets?) {
    mResults = results

    if (mAdapter != null) {
      mAdapter!!.setInfos(mResults?.otherResults)
    }
  }

  companion object {
    public val KEY_RESULTS: String = "r"
  }
}
