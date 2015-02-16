package com.zachklipp.intentsendertool

import android.net.Uri
import android.os.Bundle
import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlin.properties.Delegates
import android.app.Activity
import butterknife.bindView

public class MainActivity : Activity() {

  private val mActionText: AutoCompleteTextView by bindView(R.id.action_text)
  private val mDataText: EditText by bindView(R.id.data_text)
  private val mCategoryText: AutoCompleteTextView by bindView(R.id.category_text)
  private val mTypeText: AutoCompleteTextView by bindView(R.id.mime_type_text)
  private val mActionSpinner: Spinner by bindView(R.id.action_spinner)
  private val mResolveButton: ImageButton by bindView(R.id.resolve_button)
  private val mLaunchButton: ImageButton by bindView(R.id.launch_button)
  private val mResolvedHeader: View by bindView(R.id.resolved_to_header)
  private val mResolveView: IntentTargetView by bindView(R.id.resolve_view)
  private val mNoResultsText: TextView by bindView(R.id.no_results_text)

  private val mLaunchAdapter: ArrayAdapter<LaunchAction> by Delegates.lazy() {
    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, LaunchAction.values())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mLaunchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mActionSpinner.setAdapter(mLaunchAdapter)

    val buttonLongClickListener = {(v: View) ->
      showToast(v.getContentDescription(), Toast.LENGTH_SHORT)
      true
    }

    mResolveButton.setOnLongClickListener(buttonLongClickListener)
    mLaunchButton.setOnLongClickListener(buttonLongClickListener)

    mActionText.setAdapter<ArrayAdapter<String>>(ArrayAdapter(this,
        android.R.layout.simple_spinner_dropdown_item, androidIntentActions.copyToArray()))

    mCategoryText.setAdapter<ArrayAdapter<String>>(ArrayAdapter(this,
        android.R.layout.simple_dropdown_item_1line, androidIntentCategories.copyToArray()))

    mActionText.setText(Intent.ACTION_VIEW)
    mActionText.selectAll()
  }

  public fun onResolveButtonClick(view: View) {
    val action = getSelectedLaunchAction()
    val intent = buildIntent()

    displayResolveResults(action.resolve(intent, getPackageManager()))
    getCurrentFocus().hideSoftKeyboardFromView()
  }

  public fun onLaunchButtonClick(view: View) {
    val action = getSelectedLaunchAction()
    val intent = buildIntent()

    displayResolveResults(action.resolve(intent, getPackageManager()))
    getCurrentFocus().hideSoftKeyboardFromView()

    try {
      action.launch(intent, this)
    } catch (e: ActivityNotFoundException) {
      Toast.makeText(this, getString(R.string.activity_not_found), Toast.LENGTH_SHORT).show()
    }

  }

  private fun getSelectedLaunchAction(): LaunchAction {
    return mLaunchAdapter.getItem(mActionSpinner.getSelectedItemPosition())
  }

  private fun buildIntent(): Intent {
    val data = mDataText.getText().toString()
    val category = mCategoryText.getText().toString()
    var mimeType: String = mTypeText.getText().toString()
    var setData = false
    var setType = false
    val intent = Intent()
    var dataUri: Uri? = null

    intent.setAction(mActionText!!.getText().toString())

    if (data != null && data.length() > 0) {
      dataUri = data.asNormalizedUri()
      setData = true
    }

    if (category != null && category.length() > 0) {
      intent.addCategory(category)
    }

    if (mimeType != null && mimeType.length() > 0) {
      mimeType = mimeType.normalizeMimeType()
      setType = true
    }

    if (setData && setType) {
      intent.setDataAndType(dataUri, mimeType)
    } else if (setData) {
      intent.setData(dataUri)
    } else if (setType) {
      intent.setType(mimeType)
    }

    return intent
  }

  private fun displayResolveResults(results: IntentTargets) {
    mResolvedHeader.setVisibility(View.VISIBLE)

    if (results.primaryResult != null) {
      mNoResultsText.setVisibility(View.GONE)
      mResolveView.setResolveInfo(results.primaryResult)
    } else {
      mResolveView.setVisibility(View.GONE)
      mNoResultsText.setVisibility(View.VISIBLE)
    }
  }
}

