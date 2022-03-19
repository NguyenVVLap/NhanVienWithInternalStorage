package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<NhanVien> nv_list = new ArrayList<NhanVien>();
    String[] dv_list;
    String donvi;
    MyArrayAdapter adater = null;
    ImageView img_Anh;
    Button btn_Luu;
    String fileName = "NhanVien.txt";
    ListView lv_NhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sp_DonVi = findViewById(R.id.spinner_DonVi);
        EditText et_MaSo = findViewById(R.id.editText_NhapMa);
        EditText et_HoTen = findViewById(R.id.editText_NhapTen);
        lv_NhanVien = findViewById(R.id.listView_NhanVien);
        RadioGroup rg_GioiTinh = findViewById(R.id.radioGroup);
        RadioButton rb_Nam = findViewById(R.id.radioButton_Nam);
        RadioButton rb_Nu = findViewById(R.id.radioButton_Nu);
        btn_Luu = findViewById(R.id.button_Luu);
        dv_list = getResources().getStringArray(R.array.donvi_list);
        ArrayAdapter<String> adapterDV = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , dv_list);
        sp_DonVi.setAdapter(adapterDV);
        sp_DonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                donvi = dv_list[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        readDataFromInternalStorageToListView();
        Button btn_Them = findViewById(R.id.button_Them);
        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maso = Integer.parseInt(et_MaSo.getText().toString());
                String hoten = et_HoTen.getText().toString();
                String gioitinh = ((RadioButton)findViewById(rg_GioiTinh.getCheckedRadioButtonId())).getText().toString();

                byte[] imgByte = imgToByte(img_Anh);

                NhanVien nv = new NhanVien(maso, hoten, gioitinh, donvi, imgByte);
                nv_list.add(nv);
                writeToInternalStorage();
                readDataFromInternalStorageToListView();
            }
        });

        lv_NhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NhanVien nv = nv_list.get(i);
                et_MaSo.setText(nv.getMaso() + "");
                et_HoTen.setText(nv.getHoten());
                if (nv.getGioitinh().equals("Nam")) {
                    rb_Nam.setChecked(true);
                } else {
                    rb_Nu.setChecked(true);
                }
                for(int j = 0; j < dv_list.length; j++) {
                    if (dv_list[j] == nv.getDonvi()) {
                        sp_DonVi.setSelection(j);
                    }
                }
                byte[] imgByte = nv.getImgView();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                img_Anh.setImageBitmap(bitmap);
                adater.setItemSelected(i);
                adater.notifyDataSetChanged();
            }
        });

        Button btn_ChupAnh = findViewById(R.id.button_ChupAnh);
        img_Anh = findViewById(R.id.imageView_Anh);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.CAMERA}, 100);
        }

        btn_ChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        Button btn_ChonAnh = findViewById(R.id.button_ChonAnh);
        btn_ChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


    }

    public void writeToInternalStorage() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput(fileName, MODE_PRIVATE));
            for (NhanVien nv : nv_list) {
                objectOutputStream.writeObject(nv);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] imgToByte(ImageView img) {
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return  bytes;
    }

    public void readDataFromInternalStorageToListView() {
        try {
            ObjectInputStream input =  new ObjectInputStream(openFileInput(fileName));
            nv_list = new ArrayList<NhanVien>();
            while (true) {
                NhanVien nv = (NhanVien) input.readObject();
                nv_list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adater = new MyArrayAdapter(this, R.layout.my_item_layout, nv_list, -1);
        lv_NhanVien.setAdapter(adater);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            img_Anh.setImageBitmap(captureImage);
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            img_Anh.setImageURI(selectedImg);

        }
    }
}