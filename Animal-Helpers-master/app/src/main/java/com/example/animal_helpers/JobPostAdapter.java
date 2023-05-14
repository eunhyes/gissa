package com.example.animal_helpers;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class JobPostAdapter extends BaseAdapter {

    ArrayList<JobPost> listViewItemList = new ArrayList<>();
    Context context;
    String[] result = null;
    String mDate = "";

    public JobPostAdapter(){}

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public Object getItem(int position) { return listViewItemList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        JobPost listViewItem = listViewItemList.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
        }

        TextView titleText = convertView.findViewById(R.id.textview_title);
        TextView locationText = convertView.findViewById(R.id.textview_location);
        TextView storeText = convertView.findViewById(R.id.textview_store);
        TextView dateText = convertView.findViewById(R.id.textview_date);

        titleText.setText(listViewItem.getTitle());
        locationText.setText(listViewItem.getLocation());
        storeText.setText(listViewItem.getStore());
        dateText.setText(listViewItem.getDate());

        return convertView;
    }

    public void addItem(String title, String location, String store, String date) {
        JobPost item = new JobPost();


//        result = date.split("-");
//        mDate= result[1]+"/"+result[2];


        item.setTitle(title);
        item.setLocation(location);
        item.setStore(store);
        item.setDate(date);


        listViewItemList.add(item);
        this.notifyDataSetChanged();
    }
}
