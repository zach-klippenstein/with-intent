package com.zachklipp.intentsendertool;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class IntentLauncher
{
  private final LaunchAction[] mLaunchActions = new LaunchAction[]
  {
    new LaunchAction("Activity")
    {
      public ResolveResults resolve(Intent intent)
      {
        return new ResolveResults(
            getPackageManager().resolveActivity(intent, 0),
            getPackageManager().queryIntentActivities(intent, 0));
      }
      public void launch(Intent intent)
      {
        getContext().startActivity(intent);
      }
    },
    new LaunchAction("Broadcast")
    {
      public ResolveResults resolve(Intent intent)
      {
        return new ResolveResults(getPackageManager().queryBroadcastReceivers(intent, 0));
      }
      public void launch(Intent intent)
      {
        getContext().sendBroadcast(intent);
      }
    },
    new LaunchAction("Service")
    {
      public ResolveResults resolve(Intent intent)
      {
        return new ResolveResults(
            getPackageManager().resolveService(intent, 0),
            getPackageManager().queryIntentServices(intent, 0));
      }
      public void launch(Intent intent)
      {
        getContext().startService(intent);
      }
    }
  };
  
  private final Context mContext;
  
  public IntentLauncher(Context context)
  {
    mContext = context;
  }
  
  public LaunchAction[] getLaunchActions()
  {
    return mLaunchActions;
  }
  
  public String[] getLaunchTypeNames()
  {
    String[] names = new String[mLaunchActions.length];
    
    for (int i = 0; i < names.length; i++)
    {
      names[i] = mLaunchActions[i].getName();
    }
    
    return names;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public PackageManager getPackageManager()
  {
    return mContext.getPackageManager();
  }
  
  public abstract class LaunchAction
  {
    private final String mName;
    
    protected LaunchAction(String name)
    {
      mName = name;
    }
    
    public abstract ResolveResults resolve(Intent intent);
    public abstract void launch(Intent intent);
    
    @Override
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return mName;
    }
  }
  
  public static final class ResolveResults implements Parcelable
  {
    public final ResolveInfo mainResult;
    public final List<ResolveInfo> results;
    
    public ResolveResults(ResolveInfo mainResult)
    {
      this.mainResult = mainResult;
      this.results = new ArrayList<ResolveInfo>();
    }
    
    public ResolveResults(List<ResolveInfo> results)
    {
      this.mainResult = null;
      this.results = results;
    }
    
    public ResolveResults(ResolveInfo mainResult, List<ResolveInfo> results)
    {
      this.mainResult = mainResult;
      this.results = results;
    }
    
    // Parcelable implementation

    @Override
    public int describeContents()
    {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
      dest.writeParcelable(mainResult, 0);
      dest.writeTypedList(results);
    }

    public static final Parcelable.Creator<ResolveResults> CREATOR = new Parcelable.Creator<ResolveResults>()
    {
      public ResolveResults createFromParcel(Parcel in)
      {
        return new ResolveResults(in);
      }

      public ResolveResults[] newArray(int size)
      {
        return new ResolveResults[size];
      }
    };
    
    private ResolveResults(Parcel in)
    {
      mainResult = in.readParcelable(null);
      results = new ArrayList<ResolveInfo>();
      in.readTypedList(results, ResolveInfo.CREATOR);
    }
  }
}
