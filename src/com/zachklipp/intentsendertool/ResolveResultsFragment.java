package com.zachklipp.intentsendertool;

import java.util.List;

import com.zachklipp.intentsendertool.IntentLauncher.ResolveResults;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ResolveResultsFragment extends ListFragment
{
  public static final String KEY_RESULTS = "r";
  
  private ResolveResults mResults;
  private ResolveInfoAdapter mAdapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    Bundle args = getArguments();
    mResults = args != null ? (ResolveResults)args.getParcelable(KEY_RESULTS) : null;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    //return super.onCreateView(inflater, container, savedInstanceState);
    
    return inflater.inflate(R.layout.fragment_resolve_results, container);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    mAdapter = new ResolveInfoAdapter(getActivity());
    setListAdapter(mAdapter);
    
    // If results were changed after creation, we should load those instead of what we
    // got as arguments.
    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RESULTS))
    {
      mResults = savedInstanceState.getParcelable(KEY_RESULTS);
    }
    setResults(mResults);
  }
  
  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    outState.putParcelable(KEY_RESULTS, mResults);
  }
  
  public void setResults(ResolveResults results)
  {
    mResults = results;
    
    if (mAdapter != null)
    {
      mAdapter.setInfos(mResults != null ? mResults.results : null);
    }
  }
}

class ResolveInfoAdapter extends BaseAdapter
{
  private final Context mContext;
  private List<ResolveInfo> mInfos;
  
  public ResolveInfoAdapter(Context context)
  {
    mContext = context;
    setInfos(null);
  }
  
  public void setInfos(List<ResolveInfo> infos)
  {
    mInfos = infos;
    
    if (mInfos != null)
    {
      notifyDataSetChanged();
    }
    else
    {
      notifyDataSetInvalidated();
    }
  }

  @Override
  public int getCount()
  {
    return mInfos != null ? mInfos.size() : 0;
  }

  @Override
  public Object getItem(int pos)
  {
    return mInfos != null ? mInfos.get(pos) : null;
  }

  @Override
  public long getItemId(int pos)
  {
    return pos;
  }

  @Override
  public View getView(int pos, View convertView, ViewGroup parent)
  {
    ResolveInfoView view = (ResolveInfoView) convertView;
    
    if (view == null)
    {
      view = new ResolveInfoView(mContext);
    }
    
    view.setResolveInfo(mInfos.get(pos));
    
    return view;
  }
}
