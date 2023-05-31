package com.example.animal_helpers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.animal_helpers.models.JobPost;

import java.util.ArrayList;

public class JobPostAdapter extends BaseAdapter {

    ArrayList<JobPost> listViewItemList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    private JobPostAdapter item;

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
        TextView addressText = convertView.findViewById(R.id.textview_address);
        TextView writingDateText = convertView.findViewById(R.id.textview_writingDate);
        ImageButton imageButton = convertView.findViewById(R.id.imageButton_menu);


        titleText.setText(listViewItem.getTitle());
        addressText.setText(listViewItem.getAddress());
        writingDateText.setText(listViewItem.getWritingDate());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        return convertView;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        // editItem();
                        return true;
                    case R.id.menu_delete:
                        //deleteItem();
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.postedit_menu, popupMenu.getMenu());
        popupMenu.show();

    }
    public void addItem(String Uid, String title, String address, String writingDate) {
        JobPost item = new JobPost();


        item.setUid(Uid);
        item.setTitle(title);
        item.setAddress(address);
        item.setWritingDate(writingDate);


        listViewItemList.add(item);
        this.notifyDataSetChanged();
    }
}
