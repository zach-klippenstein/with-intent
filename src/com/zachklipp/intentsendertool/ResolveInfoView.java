package com.zachklipp.intentsendertool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResolveInfoView extends FrameLayout
{
  private ResolveInfo mInfo;
  private ImageView mResolvedIcon;
  private TextView mResolvedLabel;
  private TextView mResolvedName;
  
  public ResolveInfoView(Context context)
  {
    super(context);
    init();
  }

  public ResolveInfoView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public ResolveInfoView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    init();
  }
  
  private void init()
  {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.view_resolve_info, this);
    
    mResolvedIcon = (ImageView) findViewById(R.id.resolved_icon);
    mResolvedLabel = (TextView) findViewById(R.id.resolved_label_text);
    mResolvedName = (TextView) findViewById(R.id.resolved_name_text);

    this.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					onCopyNameClick();
				}
			});
    
    setResolveInfo(null);
  }
  
  public void setResolveInfo(ResolveInfo info)
  {
    mInfo = info;
    String name = getName();  
    
    if (mInfo != null)
    {
      mResolvedIcon.setImageDrawable(info.loadIcon(getPackageManager()));
      mResolvedIcon.setContentDescription(getContext().getString(
          R.string.resolved_icon_description,
          getContext().getString(isActivityOrBroadcastReceiver() ? R.string.activity : R.string.service),
          name));
      mResolvedLabel.setText(info.loadLabel(getPackageManager()));
      mResolvedName.setText(name);
      setVisibility(View.VISIBLE);
    }
    else
    {
      setVisibility(View.GONE);
      mResolvedIcon.setImageDrawable(null);
      mResolvedIcon.setContentDescription(null);
      mResolvedLabel.setText(null);
      mResolvedName.setText(null);
    }
  }
  
  private void onCopyNameClick()
  {
    String name = getName();
    
    if (mInfo != null)
    {
      Util.copyText(getContext(), getContext().getString(R.string.resolved_name_label), name);
      Toast.makeText(getContext(),
          getContext().getString(R.string.copied_name_toast, name),
          Toast.LENGTH_SHORT).show();
    }
  }
  
  private PackageManager getPackageManager()
  {
    return getContext().getPackageManager();
  }
  
  private String getName()
  {
    if (mInfo == null)
    {
      return null;
    }
    
    if (isActivityOrBroadcastReceiver())
    {
      return mInfo.activityInfo.name;
    }
    else
    {
      return mInfo.serviceInfo.name;
    }
  }
  
  private boolean isActivityOrBroadcastReceiver()
  {
    return mInfo != null ? mInfo.activityInfo != null : false;
  }

}
