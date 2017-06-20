package com.example.lab.fintech_momo;

/**
 * Created by lab on 2017/6/17.
 */

public class History {
    private String keyword, date;

    public History(){

    }

    public History(String keyword, String date){
        this.keyword = keyword;
        this.date = date;
    }

    public String getKeyword(){
        return keyword;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
}
