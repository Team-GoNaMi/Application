package com.example.gonami.bookboxbook.DataCenter;

import java.util.ArrayList;

public class BookInformation {
    // 책 자체의 정보 - 이걸 디비가 갖고 있어야 하나..??
    private String isbn;
    private String bookName;
    private String author;
    private String publisher;
    private int original_price;
    private int edition;

    // Seller가 등록한 정보
    private String register_id;     // 얘로 모든 것을 찾을 수 있게
    private String seller_id;
    private ArrayList<String> school;
    private int selling_price;
    private ArrayList<String> book_image;  //사진은 무슨 형으로 저장해?
    private int underline;
    private int writing;
    private int cover;
    private int damage_page;
    private String memo;

    //  책 상태
    private int sell_avail;

    public BookInformation(String isbn, String bookName, String author, String publisher, int origin_price, int edition) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.original_price = origin_price;
        this.edition = edition;
    }

    public void setBookInformation(String register_id, String seller_id, ArrayList<String> school, int selling_price, ArrayList<String> book_image,
                           int underline, int writing, int cover, int damage_page, String memo) {

        this.register_id = register_id;
        this.seller_id = seller_id;
        this.school = school;
        this.selling_price= selling_price;
        this.book_image = book_image;
        this.underline = underline;
        this.writing = writing;
        this.cover = cover;
        this.damage_page = damage_page;
        this.memo = memo;
        this.sell_avail = 1;
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

    public int getOriginal_price() {
        return original_price;
    }

    public int getEdition() {
        return edition;
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

    public int getSelling_price() {
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

    public boolean isSell_avail() {
        return sell_avail;
    }

    public void setSell_avail(boolean avail) {
        this.sell_avail = avail;
    }
}
