package com.markosopcic.cycler.view.adapters;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

 public class MultiChoiceListAdapter<T> extends BaseAdapter {
  private Context ctx;

  private Collection<T> options;
  private Collection<T> selection;
  private List<T> filteredOptions;

  public MultiChoiceListAdapter(Context context, Collection<T> options,
                                Collection<T> selection) {
    this.ctx = context;

    this.options = options;
    this.selection = selection;

    this.filteredOptions = new ArrayList<T>(options.size());
    setFilter(null);
  }

  @Override
  public int getCount() {
    return filteredOptions.size();
  }

  @Override
  public T getItem(int position) {
    return filteredOptions.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ChoiceView view;
    T item = getItem(position);
    boolean selected = selection.contains(item);
    if (convertView == null) {
      view = new ChoiceView(ctx, item, selected);
    } else {
      view = (ChoiceView) convertView;
      view.setItem(item, selected);
    }
    return view;
  }

  public void setFilter(String filter) {
    if (filter != null)
      filter = filter.toLowerCase();

    filteredOptions.clear();
    for (T item : selection)
      filteredOptions.add(item);
    for (T item : options)
      if (!selection.contains(item)
          && (filter == null || item.toString().toLowerCase()
              .contains(filter)))
        filteredOptions.add(item);
  }

  @SuppressLint("AppCompatCustomView")
  public class ChoiceView extends CheckBox implements OnCheckedChangeListener {
    private T object;

    public ChoiceView(Context context, T object, Boolean selected) {
      super(context);
      this.object = object;
      setOnCheckedChangeListener(this);
      setItem(object, selected);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
        boolean isChecked) {
      if (selection != null) {
        if (isChecked && !selection.contains(object))
          selection.add(object);
        else if (!isChecked)
          selection.remove(object);
      }
      notifyDataSetChanged();
    }

    public void setItem(T object, Boolean selected) {
      this.object = object;
      setChecked(selected);
      setText(object.toString());
    }
  }
}




   