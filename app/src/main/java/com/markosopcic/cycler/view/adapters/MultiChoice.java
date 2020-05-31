package com.markosopcic.cycler.view.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MultiChoice<T> extends AlertDialog {
  private ListView listView;

  private Map<T, Boolean> optionsWithSelection;
  private Collection<T> options;
  private Collection<T> selection;
  private View.OnClickListener confirmListener;

  public MultiChoice(Context context, Collection<T> options,
                     Collection<T> selection) {
    super(context);
    this.options = options;
    this.selection = selection;
    this.confirmListener = confirmListener;
  }

  public void setOnConfirmListener(View.OnClickListener listener){
    confirmListener = listener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Context ctx = getContext();
    LinearLayout layout = new LinearLayout(ctx);
    layout.setOrientation(LinearLayout.VERTICAL);

    listView = new ListView(ctx);
    final MultiChoiceListAdapter<T> adapter;
    adapter = new MultiChoiceListAdapter<T>(ctx, options, selection);
    listView.setAdapter(adapter);

      EditText search = new EditText(ctx);
      search.setHint("Filter friends...");
      search.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
      search.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start,
            int before, int count) {
          adapter.setFilter(s.toString());
          adapter.notifyDataSetChanged();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
            int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
      });

      layout.addView(search);

    layout.addView(listView);

    Button button = new Button(ctx);
    button.setText("Confirm");
    button.setOnClickListener(confirmListener);
    layout.addView(button);
    setContentView(layout);
  }

  public Map<T, Boolean> getOptionsMap() {
    return optionsWithSelection;
  }

  public Set<T> getSelection() {
    return new HashSet<>(selection);
  }
}