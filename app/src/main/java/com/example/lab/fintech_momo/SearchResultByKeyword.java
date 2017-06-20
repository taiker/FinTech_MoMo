package com.example.lab.fintech_momo;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchResultByKeyword extends AppCompatActivity {

    private WebView result_page;
    //    private ProgressBar prograss_bar;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private List<History> historyList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private RecyclerView history_page;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_by_keyword);

        result_page = (WebView) findViewById(R.id.result_page);
//        prograss_bar = (ProgressBar) findViewById(R.id.progressBar);
//        result_page.setWebViewClient(new myWebClient());
        result_page.getSettings().setJavaScriptEnabled(true);
        handleIntent(getIntent());

        result_page.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data someho
            result_page.loadUrl("https://m.momoshop.com.tw/search.momo?searchKeyword=" + query);
            setHistoryObj(query);
        }else{
            Bundle bundle = getIntent().getExtras();
            result_page.loadUrl("https://m.momoshop.com.tw/search.momo?searchKeyword="+bundle.getString("keyword"));
        }
    }

    private void setHistoryObj(String query){
        try {
            JSONObject json_obj = new JSONObject();
            json_obj.put("keyword",query);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            json_obj.put("date",sdf.format(timestamp));
            setHistory(json_obj);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHistory(JSONObject obj){
        try {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("HISTORY", 0);
            String history = preferences.getString("historyList", "");
            JSONArray json_array;
            if(history == ""){
                json_array = new JSONArray();
            }else{
                json_array = new JSONArray(history);
            }
            json_array.put(obj);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("historyList", json_array.toString()); // value to store
            System.out.println(json_array.toString());
            editor.commit();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    public void onBackPressed() {
        if (result_page!= null) {
            if (result_page.canGoBack()) {
                result_page.goBack();
            } else {
                // do when mWebView cant go back anymore
                this.finish();
            }
        }
    }
}

