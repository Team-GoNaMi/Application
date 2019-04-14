package com.example.gonami.bookboxbook.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

public class BookInformation implements Serializable {
    // 책 자체의 정보 - 이걸 디비가 갖고 있어야 하나..??
    private String isbn;
    private String bookName;
    private String author;
    private String publisher;
    private String original_price;
//    private int original_price;
    private String publish_date;

    // Seller가 등록한 정보
    private String register_id;     // 얘로 모든 것을 찾을 수 있게
    private String seller_id;
    private ArrayList<String> school;
    private String selling_price;
//    private int selling_price;
    private ArrayList<String> book_image;  //사진은 무슨 형으로 저장해?
    private int underline;
    private int writing;
    private int cover;
    private int damage_page;
    private String memo;

    //  책 상태
    private int buy_avail;

    public BookInformation(String isbn, String bookName, String author, String publisher, String origin_price, String publish_date) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.original_price = origin_price;
//        this.original_price = Integer.parseInt(origin_price);
        this.publish_date = publish_date;
    }

    public void setBookInformation(String register_id, String seller_id, ArrayList<String> school, String selling_price, ArrayList<String> book_image,
                           int underline, int writing, int cover, int damage_page, String memo) {

        this.register_id = register_id;
        this.seller_id = seller_id;
        this.school = school;
        this.selling_price = selling_price;
//        this.selling_price = Integer.parseInt(selling_price);
        this.book_image = book_image;
        this.underline = underline;
        this.writing = writing;
        this.cover = cover;
        this.damage_page = damage_page;
        this.memo = memo;
        this.buy_avail = 1;
    }

    public String toString() {
        String concat = "";
        concat += "isbn=" + isbn;
        concat += "&book_name=" + bookName;
        concat += "&author=" + author;
        concat += "&publisher=" + publisher;
        concat += "&original_price=" + original_price;
        concat += "&publish_date=" + publish_date;
        concat += "&register_id=" + register_id;
        concat += "&seller_id=" + seller_id;
        concat += "&school=" + school;  // TODO change form
        concat += "&selling_price=" + selling_price;
        concat += "&book_image=" + book_image; // TODO change form
        concat += "&underline=" + underline;
        concat += "&writing=" + writing;
        concat += "&cover=" + cover;
        concat += "&damage_page=" + damage_page;
        concat += "&memo=" + memo;
        concat += "&buy_avail=" + buy_avail;

        return concat;
    }

    public String getISBN() {
        return isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public String getRegister_id() {
        return register_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public ArrayList<String> getSchool() {
        return school;
    }

    public String getSellingPrice() {
        return selling_price;
    }

    public ArrayList<String> getBook_image() {
        return book_image;
    }

    public int getUnderline() {
        return underline;
    }

    public int getWriting() {
        return writing;
    }

    public int getCover() {
        return cover;
    }

    public int getDamage_page() {
        return damage_page;
    }

    public String getMemo() {
        return memo;
    }

    public int isSell_avail() {
        return buy_avail;
    }

    public void setBuy_avail(int avail) {
        this.buy_avail = avail;
    }
}
