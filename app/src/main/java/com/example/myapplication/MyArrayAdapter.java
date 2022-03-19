package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<NhanVien> {
    Activity context = null;
    ArrayList<NhanVien> myArray = null;
    int layoutId;
    int itemSelected;

    public MyArrayAdapter(Activity context, int resource, ArrayList<NhanVien> objects, int itemSelected) {
        super(context, resource, objects);
        this.context = context;
        this.myArray = objects;
        this.layoutId = resource;
        this.itemSelected = itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() > 0 && position >= 0) {
            final TextView txtDisplay = (TextView) convertView.findViewById(R.id.tv_item);
            final NhanVien nv = myArray.get(position);
            txtDisplay.setText(nv.toString());
            final ImageView img = convertView.findViewById(R.id.img_item);
            byte[] byteArray = nv.getImgView();
            if (itemSelected == position) {
                final LinearLayout linearLayout = convertView.findViewById(R.id.myLinearLayout);
                linearLayout.setBackgroundColor(R.color.cardview_dark_background);
                txtDisplay.setTextColor(R.color.white);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            img.setImageBitmap(bitmap);
        }
        return convertView;
    }
}
