package com.example.omar.neon.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.omar.neon.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sadaka on 20/05/2018.
 */

public class MyExpandapleListAdapter extends BaseExpandableListAdapter {
  private List<String> header_titles;
  private HashMap<String,List<String>> child_titles;
  private Context context;

    public MyExpandapleListAdapter(List<String> header_titles, HashMap<String, List<String>> child_titles, Context context) {
        this.header_titles = header_titles;
        this.child_titles = child_titles;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return header_titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child_titles.get(header_titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header_titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child_titles.get(header_titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public  long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       String title = (String) this.getGroup(groupPosition);
       if(convertView==null){
           LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = layoutInflater.inflate(R.layout.header_layout,null);
       }
        TextView textView = (TextView)convertView.findViewById(R.id.header_item);
       textView.setTypeface(null, Typeface.BOLD);
       textView.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       String title = (String) this.getChild(groupPosition,childPosition);
       if(convertView==null){
           LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = layoutInflater.inflate(R.layout.child_layout,null);
       }
       TextView textView = (TextView)convertView.findViewById(R.id.child_item);
       textView.setText(title);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public static int getposition(int groupPosition, int childPosition){
        return childPosition;
    }
}
