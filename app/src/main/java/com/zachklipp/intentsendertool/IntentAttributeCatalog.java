package com.zachklipp.intentsendertool;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Intent;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class IntentAttributeCatalog
{
  private static final String ACTION_FIELD_PATTERN = "^ACTION_.*";
	private static final String CATEGORY_FIELD_PATTERN = "^CATEGORY_.*";

  private final SortedSet<String> mActions = new TreeSet<String>();
	private final SortedSet<String> mCategories = new TreeSet<String>();

	public IntentAttributeCatalog()
	{
		loadFrameworkActions();
		loadFrameworkCategories();
	}
	
	public String[] getActions()
	{
	  String[] actions = new String[mActions.size()];
		return mActions.toArray(actions);
	}
	
	public String[] getCategories()
	{
	  String[] categories = new String[mCategories.size()];
		return mCategories.toArray(categories);
	}
	
	public void loadActionsFromClass(Class<?> c)
	{
		loadActionsFromClass(c, ACTION_FIELD_PATTERN);
	}
	
	public void loadActionsFromClass(Class<?> c, String pattern)
	{
		mActions.addAll(getStaticStringFieldValues(c, pattern));
	}
	
	public void loadCategoriesFromClass(Class<?> c, String pattern)
	{
	  mCategories.addAll(getStaticStringFieldValues(c, pattern));
	}
  
  private void loadFrameworkActions()
  {
    loadActionsFromClass(Intent.class);
  }
  
  private void loadFrameworkCategories()
  {
    loadCategoriesFromClass(Intent.class, CATEGORY_FIELD_PATTERN);
  }
	
	private static Collection<String> getStaticStringFieldValues(Class<?> c, String pattern)
	{
		Field[] fields = c.getFields();
		SortedSet<String> vals = new TreeSet<String>();
		int modifiers;
		String val;
		
		for (Field field : fields)
		{
			modifiers = field.getModifiers();
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getType() == String.class
			  && Pattern.matches(pattern, field.getName()))
			{
				try
				{
					val = (String)field.get(null);
					vals.add(val);
				}
				catch (Exception e)
				{}
			}
		}
		
		return vals;
	}
}
