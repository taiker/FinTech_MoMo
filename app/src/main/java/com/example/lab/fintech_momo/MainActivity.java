package com.example.lab.fintech_momo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolboar;
    private Button open_camera;
    private LinearLayout home_page;
    private WebView best_page;
    private RecyclerView history_page;
    private List<History> historyList;
    private RecyclerView.LayoutManager mLayoutManager;
    private HistoryAdapter historyAdapter;
    private FloatingActionMenu fam;
    private FloatingActionButton fab_image, fab_info;
    private final String PERMISSIN_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String PERMISSIN_READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PERMISSIN_INTERNET = "android.permission.INTERNET";
    private final String PERMISSIN_NETWORK_STATE = "android.permission.INTERNET";
    private final String PERMISSIN_WIFI_STATE = "android.permission.INTERNET";

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int PICK_IMAGE_REQUEST = 1222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!hasPermission()){
            if(!needCheckPremission()){
                return;
            }
        }

        mToolboar=(Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolboar);//Toolbar取代原本的ActionBar

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);//必須用字串資源檔
        mDrawerLayout.addDrawerListener(mToggle);//工具欄監聽事件

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//隱藏顯示箭頭返回
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//清單觸發監聽事件

        open_camera = (Button) findViewById(R.id.btn_camera);
        home_page = (LinearLayout) findViewById(R.id.home_page);
        best_page = (WebView) findViewById(R.id.best_page);
        history_page = (RecyclerView) findViewById(R.id.history_page);

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
            }
        });

        best_page.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        best_page.loadUrl("https://m.momoshop.com.tw/ranking.momo");
        best_page.getSettings().setJavaScriptEnabled(true);
        best_page.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        set_history_adapter();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        fab_image = (FloatingActionButton) findViewById(R.id.fab1);
        fab_info = (FloatingActionButton) findViewById(R.id.fab2);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                } else {
                }
            }
        });

        //handling each floating action button clicked
        fab_image.setOnClickListener(onButtonClick());
        fab_info.setOnClickListener(onButtonClick());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){//當按下左上三條線或顯示工具列
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this,"主頁", Toast.LENGTH_SHORT).show();
            System.out.println("change to home page");
            home_page.setVisibility(View.VISIBLE);
            best_page.setVisibility(View.GONE);
            history_page.setVisibility(View.GONE);
        }
        else if (id == R.id.nav_best) {
            Toast.makeText(this,"熱門商品", Toast.LENGTH_SHORT).show();
            System.out.println("change to best page");
            home_page.setVisibility(View.GONE);
            best_page.setVisibility(View.VISIBLE);
            history_page.setVisibility(View.GONE);
        }
        else if (id == R.id.nav_history)
        {
            Toast.makeText(this,"搜尋紀錄",Toast.LENGTH_SHORT).show();
            System.out.println("change to history page");
            home_page.setVisibility(View.GONE);
            best_page.setVisibility(View.GONE);
            history_page.setVisibility(View.VISIBLE);
            set_history_adapter();
        }
        else if (id == R.id.nav_help)
        {
            Toast.makeText(this,"小幫手",Toast.LENGTH_SHORT).show();

        }
        mDrawerLayout.closeDrawers();
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                imageBitmap.recycle();

                //Pop intent
                Intent intent = new Intent(MainActivity.this, SetSearch.class);
                intent.putExtra("image", filename);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            try {
                Uri imageUri = data.getData();
                System.out.println("imageuri->"+imageUri);
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                selectedImage.recycle();

                //Pop intent
                Intent intent = new Intent(MainActivity.this, SetSearch.class);
                intent.putExtra("image", filename);
                intent.putExtra("uri",imageUri.getPath());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void set_history_adapter() {
        try{
            historyList = new ArrayList<>();
            historyAdapter = new HistoryAdapter(historyList);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            history_page.setLayoutManager(mLayoutManager);
            history_page.setItemAnimator(new DefaultItemAnimator());
            historyAdapter.setOnItemClickListener(new RecycleViewItmeClickListener() {
                public void onItemClick(History item) {
//                    Toast.makeText(MainActivity.this, item.getKeyword(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SearchResultByKeyword.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyword",item.getKeyword());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            History movie;
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("HISTORY", 0);
            System.out.println("pre --->" + preferences);
            String history = preferences.getString("historyList", "");
            System.out.println("share preference --> " + history);
            JSONArray json_array;
            if(history == ""){
                json_array = new JSONArray();
            }else{
                json_array = new JSONArray(history);
            }
            System.out.println(json_array.toString());
            for(int i = 0; i < json_array.length(); i++){
                JSONObject json_obj = json_array.getJSONObject(i);
                System.out.println(json_obj.toString());
                movie = new History(json_obj.get("keyword").toString(),json_obj.get("date").toString());
                historyList.add(movie);
            }
            historyAdapter.notifyDataSetChanged();
            history_page.setAdapter(historyAdapter);
            System.out.println("GOT HISTORY !!");
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fab_image) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    }
                } else if (view == fab_info) {

                }
                fam.close(true);
            }
        };
    }
    public void onBackPressed() {
        if (best_page!= null) {
            if (best_page.canGoBack()) {
                best_page.goBack();
            } else {
                // do when mWebView cant go back anymore
                this.finish();
            }
        }
    }



    private  boolean needCheckPremission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] perms ={PERMISSIN_WRITE_STORAGE,PERMISSIN_READ_STORAGE,PERMISSIN_INTERNET,PERMISSIN_NETWORK_STATE};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }

    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return (ActivityCompat.checkSelfPermission(this,PERMISSIN_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,PERMISSIN_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,PERMISSIN_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,PERMISSIN_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
            );
        }
        return true;
    }

    @Override
    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==200){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(">>>","get access");
                }
            }
        }
    }
}
