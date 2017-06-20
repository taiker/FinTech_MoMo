package com.example.lab.fintech_momo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SetSearch extends AppCompatActivity {

    private ImageView img;
    private Spinner spinner;
    private Button search;

    public String selected_type;
    public Bitmap bmp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_search);

        img = (ImageView) findViewById(R.id.show_image);
        spinner = (Spinner) findViewById(R.id.spinner);
        search = (Button) findViewById(R.id.btn_search);

        bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream file_input_stream = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(file_input_stream);
            file_input_stream.close();
            img.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String[] type = {"美妝", "保健", "食品", "旅遊", "婦幼", "3C", "家電", "服飾", "內衣", "鞋包錶", "精品/配飾", "生活用品", "傢俱寢飾", "宗教/藝術", "運動休閒"};
        final String[] search_code = {
            "1199900000",
            "1299900000",
            "2099900000",
            "2599900000",
            "2799900000",
            "1999900000",
            "2999900000",
            "1399900000",
            "1499900000",
            "1599900000",
            "3199900000",
            "1799900000",
            "1899900000",
            "2899900000",
            "1699900000"};

        ArrayAdapter<String> titleList = new ArrayAdapter<>(SetSearch.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        spinner.setAdapter(titleList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ViewImage.this, "你選的是" + type[position], Toast.LENGTH_SHORT).show();
                selected_type = search_code[position];
                Toast.makeText(SetSearch.this, "品項：" + type, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetSearch.this, "品項：" + type, Toast.LENGTH_SHORT).show();

                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = SetSearch.this.openFileOutput(filename, Context.MODE_PRIVATE);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bmp.recycle();

                    Bundle bundle = new Bundle();
                    bundle.putString("type", selected_type);
                    bundle.putString("image", filename);
                    //Pop intent
                    Intent intent = new Intent(SetSearch.this, SearchResultByImage.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
}

