package com.example.gonami.bookboxbook.BookMarket;

import android.net.Uri;

public class ViewHolder {
    Uri ivBookImage;
    String tvBookName;
    String tvBookInfo;
    String tvSchoolNames;
    String tvBookOriginPrice;
    String tvBookPrice;

    public void addItem( String tvBookInfo, String tvSchoolNames, String tvBookOriginPrice, String tvBookPrice){
       // this.ivBookImage=ivBookImage;
        this.tvBookInfo=tvBookInfo;
        this.tvSchoolNames=tvSchoolNames;
        this.tvBookOriginPrice=tvBookOriginPrice;
        this.tvBookPrice=tvBookPrice;
    }
    public Uri getIvBookImage(){
        return ivBookImage;
    }
    public String getTvBookName(){
        return tvBookName;
    }
    public String getTvBookInfo(){
        return tvBookInfo;
    }
    public String getTvSchoolNames(){
        return tvSchoolNames;
    }
    public String getTvBookOriginPrice(){
        return tvBookOriginPrice;
    }
    public String getTvBookPrice(){
        return tvBookPrice;
    }
}
