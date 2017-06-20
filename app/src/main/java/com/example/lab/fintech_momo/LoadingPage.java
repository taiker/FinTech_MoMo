package com.example.lab.fintech_momo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(2000);//這邊可以做你想預先載入的資料
                    //接下來轉跳到app的主畫面
                    startActivity(new Intent().setClass(LoadingPage.this, MainActivity.class)); //上列預設時間後切換到登入畫面
                    LoadingPage.this.finish();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
