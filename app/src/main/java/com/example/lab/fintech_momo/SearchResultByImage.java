package com.example.lab.fintech_momo;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;



import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import java.io.*;


public class SearchResultByImage extends AppCompatActivity {
    private WebView WebView;
    String search_item="";//接收查詢關鍵字
    private Upload upload; // Upload object containging image and meta data
    private String filename;
    private String selected_title;
    private Bitmap bmp;
    private File chosenFile; //chosen file from intent
    private String link;
    AsyncHttpClient client;
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //與使用者取得權限
        setContentView(R.layout.activity_search_result_by_image);


        /********新增********/
        //用bundle傳值
        Bundle bundle = this.getIntent().getExtras();
        //image用FileInputStream存取 filename為存取key
        filename = bundle.getString("image");
        //type是商品類別
        selected_title = bundle.getString("type");
        try {
            chosenFile = new File(filename);
            chosenFile = new File("/sdcard/","test.jpg");
            FileInputStream file_input_stream = this.openFileInput(filename);
            file_input_stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /********新增********/
        createUpload(chosenFile);
        Log.d("file",chosenFile.toString());
        new UploadService(this).Execute(upload, new UiCallback());

    }

    private void createUpload(File image) {
        upload = new Upload();
        upload.image = image;
        upload.title = "1";
        upload.description = "1";
        Log.d("res","upload");
    }

    private class UiCallback implements Callback<ImageResponse> {
        @Override
        public void success(ImageResponse imageResponse, Response response) {
            Log.d("success","callback");
            Log.d("success",imageResponse.data.link+" kasladkjanadkm");

            link = imageResponse.data.link;
            try {
                client= new AsyncHttpClient();
//                client.post("http://images.google.com/searchbyimage?image_url="+link, new TextHttpResponseHandler() {
                client.get("http://140.118.109.149:3000/google?img="+link, new TextHttpResponseHandler() {

                        @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                        Toast.makeText(SearchResultByImage.this, responseString, Toast.LENGTH_SHORT).show();

                            WebView = (WebView) findViewById(R.id.activity_main_webview);
                            WebView.getSettings().setJavaScriptEnabled(true);
                            WebView.requestFocus();
                            WebView.setVisibility(View.VISIBLE);
                            WebView.setHorizontalScrollBarEnabled(true);
                            WebView.setVerticalScrollBarEnabled(true);
                            WebView.getSettings().setBuiltInZoomControls(true);
                            WebView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent event) {
                                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                                }
                            });
                            WebView.loadUrl("https://m.momoshop.com.tw/search.momo"); //載入網頁
                            WebView.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return true;
                                }
                            });
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        Log.d("google_response",String.valueOf(responseString.length()));
                        Log.d("google_response",responseString);
                        //Log.d("google_response",String.valueOf(responseString.indexOf("\"_gub\"")));
                        Toast.makeText(SearchResultByImage.this, responseString, Toast.LENGTH_SHORT).show();

                        WebView = (WebView) findViewById(R.id.activity_main_webview);
                        WebView.getSettings().setJavaScriptEnabled(true);
                        WebView.requestFocus();
                        WebView.setVisibility(View.VISIBLE);
                        WebView.setHorizontalScrollBarEnabled(true);
                        WebView.setVerticalScrollBarEnabled(true);
                        WebView.getSettings().setBuiltInZoomControls(true);
                        WebView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent event) {
                                return (event.getAction() == MotionEvent.ACTION_MOVE);
                            }
                        });
                        if(responseString!=null)
                            WebView.loadUrl("https://m.momoshop.com.tw/category/search.momo?searchKeyword=" + responseString +
                                    "&searchType=1&cateLevel=0&cateCode="+selected_title); //載入網頁
                        else
                            WebView.loadUrl("https://m.momoshop.com.tw/search.momo?cn="+selected_title); //載入網頁
                        WebView.setWebViewClient(new WebViewClient(){
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }
                        });
                    }

                });

            }catch (Exception ioex){
                ioex.printStackTrace();
                WebView = (WebView) findViewById(R.id.activity_main_webview);
                WebView.getSettings().setJavaScriptEnabled(true);
                WebView.requestFocus();
                WebView.setVisibility(View.VISIBLE);
                WebView.setHorizontalScrollBarEnabled(true);
                WebView.setVerticalScrollBarEnabled(true);
                WebView.getSettings().setBuiltInZoomControls(true);
                WebView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return (event.getAction() == MotionEvent.ACTION_MOVE);
                    }
                });
                WebView.loadUrl("https://m.momoshop.com.tw/search.momo"); //載入網頁
                WebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            }

        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                //Snackbar.make(findViewById(R.id.rootView), "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

}
