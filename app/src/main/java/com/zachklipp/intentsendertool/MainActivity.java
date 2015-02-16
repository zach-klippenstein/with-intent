package com.zachklipp.intentsendertool;

import com.zachklipp.intentsendertool.IntentLauncher.LaunchAction;
import com.zachklipp.intentsendertool.IntentLauncher.ResolveResults;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
  private static final IntentAttributeCatalog sIntentCatalog = new IntentAttributeCatalog();

  private AutoCompleteTextView mActionText;
  private EditText mDataText;
  private AutoCompleteTextView mCategoryText;
  private AutoCompleteTextView mTypeText;
  private Spinner mActionSpinner;
  private ImageButton mResolveButton;
  private ImageButton mLaunchButton;
  private View mResolvedHeader;
  private ResolveInfoView mResolveView;
  private TextView mNoResultsText;
  private ArrayAdapter<LaunchAction> mLaunchAdapter;
  private IntentLauncher mLauncher;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mActionText = (AutoCompleteTextView) findViewById(R.id.action_text);
    mDataText = (EditText) findViewById(R.id.data_text);
    mCategoryText = (AutoCompleteTextView) findViewById(R.id.category_text);
    mTypeText = (AutoCompleteTextView) findViewById(R.id.mime_type_text);
    mActionSpinner = (Spinner) findViewById(R.id.action_spinner);
    mResolveButton = (ImageButton) findViewById(R.id.resolve_button);
    mLaunchButton = (ImageButton) findViewById(R.id.launch_button);
    mResolvedHeader = findViewById(R.id.resolved_to_header);
    mResolveView = (ResolveInfoView) findViewById(R.id.resolve_view);
    mNoResultsText = (TextView) findViewById(R.id.no_results_text);
    
    mLauncher = new IntentLauncher(this);
    mLaunchAdapter = new ArrayAdapter<LaunchAction>(
        this, android.R.layout.simple_spinner_item, mLauncher.getLaunchActions());
    mLaunchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mActionSpinner.setAdapter(mLaunchAdapter);
    
    OnLongClickListener buttonLongClickListener = new OnLongClickListener()
    {
      @Override
      public boolean onLongClick(View view)
      {
        Toast.makeText(MainActivity.this, view.getContentDescription(), Toast.LENGTH_SHORT).show();
        return true;
      }
    };
    mResolveButton.setOnLongClickListener(buttonLongClickListener);
    mLaunchButton.setOnLongClickListener(buttonLongClickListener);
		
		mActionText.setAdapter(new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_dropdown_item, sIntentCatalog.getActions()));
		mCategoryText.setAdapter(new ArrayAdapter<String>(this,
			android.R.layout.simple_dropdown_item_1line, sIntentCatalog.getCategories()));
    
    mActionText.setText(Intent.ACTION_VIEW);
    mActionText.selectAll();
  }
  
  public void onResolveButtonClick(View view)
  {
    LaunchAction action = getSelectedLaunchAction();
    Intent intent = buildIntent();
    
    displayResolveResults(action.resolve(intent));
    Util.hideSoftKeyboardFromView(getCurrentFocus());
  }
  
  public void onLaunchButtonClick(View view)
  {
    LaunchAction action = getSelectedLaunchAction();
    Intent intent = buildIntent();
    
    displayResolveResults(action.resolve(intent));
    Util.hideSoftKeyboardFromView(getCurrentFocus());
    
    try
    {
      action.launch(intent);
    }
    catch (ActivityNotFoundException e)
    {
      Toast.makeText(this, getString(R.string.activity_not_found), Toast.LENGTH_SHORT).show();
    }
  }
  
  private LaunchAction getSelectedLaunchAction()
  {
    return mLaunchAdapter.getItem(mActionSpinner.getSelectedItemPosition());
  }

  private Intent buildIntent()
  {
    String data = mDataText.getText().toString();
    String category = mCategoryText.getText().toString();
    String mimeType = mTypeText.getText().toString();
    boolean setData = false, setType = false;
    Intent intent = new Intent();
    Uri dataUri = null;
    
    intent.setAction(mActionText.getText().toString());
    
    if (data != null && data.length() > 0)
    {
      dataUri = Util.normalizeUri(Uri.parse(data));
      setData = true;
    }
    
    if (category != null && category.length() > 0)
    {
      intent.addCategory(category);
    }
    
    if (mimeType != null && mimeType.length() > 0)
    {
      mimeType = Util.normalizeMimeType(mimeType);
      setType = true;
    }
    
    if (setData && setType)
    {
      intent.setDataAndType(dataUri, mimeType);
    }
    else if (setData)
    {
      intent.setData(dataUri);
    }
    else if (setType)
    {
      intent.setType(mimeType);
    }
    
    return intent;
  }
  
  private void displayResolveResults(ResolveResults results)
  {
    mResolvedHeader.setVisibility(View.VISIBLE);
    
    if (results.mainResult != null)
    {
      mNoResultsText.setVisibility(View.GONE);
      mResolveView.setResolveInfo(results.mainResult);
    }
    else
    {
      mResolveView.setVisibility(View.GONE);
      mNoResultsText.setVisibility(View.VISIBLE);
    }
  }
}
