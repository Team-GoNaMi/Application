package com.example.gonami.bookboxbook.DataModel;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class BookInformation implements Serializable {
    // 책 자체의 정보
    private String isbn;
    private String bookName;
    private String author;
    private String publisher;
    private String original_price;
    private String publish_date;
    private String book_image;

    // Seller가 등록한 정보
    private String register_id;     // 얘로 모든 것을 찾을 수 있게
    private String seller_id;
    private ArrayList<String> school;
    private String selling_price;
    private ArrayList<String> book_photo;  //사진은 무슨 형으로 저장해?
    private int underline;
    private int writing;
    private int cover;
    private int damage_page;
    private String memo;
    private String selected_school;

    //  책 상태
    private int buy_avail;

    // 북 마크
    private boolean bookmark;

    // search와  book mark, 거래 List에서 객체 생성할 때 사용하는 생성자
    public BookInformation(String register_id, String isbn, String bookName, String author, String publisher, String original_price, String selling_price,
                           Boolean bookmark, String schools, String selected, String image) {
        this.register_id = register_id;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.original_price = original_price;
        this.selling_price = selling_price;
        this.book_image = image;

        this.bookmark = bookmark;

        String[] school_array = schools.split(",");
        this.school = new ArrayList<String>();
        for (String school : school_array)
            this.school.add(school);

        this.isbn = isbn;
        this.selected_school = selected;

        //temp
        this.book_photo = new ArrayList<String>();
    }

    public BookInformation(String isbn, String bookName, String author, String publisher, String origin_price, String publish_date, String bookImage) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.original_price = origin_price;
        this.publish_date = publish_date;

        this.book_image = bookImage;

        //temp
        this.book_photo = new ArrayList<String>();
    }

    public void setBookInformation(String register_id, String seller_id, ArrayList<String> school,
                                   String selling_price, ArrayList<String> book_image,
                                   int underline, int writing, int cover, int damage_page, String memo) {

        this.register_id = register_id;
        this.seller_id = seller_id;
        this.school = school;
        this.selling_price = selling_price;

        for(int i = 0; i<book_image.size();i++){
            this.book_photo.add(book_image.get(i));
        }

        this.underline = underline;
        this.writing = writing;
        this.cover = cover;
        this.damage_page = damage_page;
        this.memo = memo;
        this.buy_avail = 1;
    }

    public String toString() {

        String school_list = school.toString();
        Log.i("School", school_list);
        String schools = school_list.substring(1, school_list.length()-1);
        Log.i("School", schools + " - " + schools.length());

        String book_images = "";
        String book_img_list = book_photo.toString();
        Log.i("Image", book_img_list);
        if (book_photo.size() == 0) {
            book_images = "!!";
        }
        else {
            book_images = book_img_list.substring(1, book_img_list.length() - 1);
        }
        Log.i("Image", book_images + " - " + book_images.length());

        String concat = "";
        concat += "isbn=" + isbn;
        concat += "&book_name=" + bookName;
        concat += "&author=" + author;
        concat += "&publisher=" + publisher;
        concat += "&original_price=" + original_price;
        concat += "&publish_date=" + publish_date;
        concat += "&book_image=" + book_image;

        concat += "&register_id=" + register_id;
        concat += "&seller_id=" + seller_id;

        concat += "&selling_price=" + selling_price;
        concat += "&book_photo=" + book_images; // TODO change form
        concat += "&underline=" + underline;
        concat += "&writing=" + writing;
        concat += "&cover=" + cover;
        concat += "&damage_page=" + damage_page;
        concat += "&memo=" + memo;
        concat += "&buy_avail=" + buy_avail;

        concat += "&school=" + schools;

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

    public String getSchoolString() {
        String schools = "";
        for (String school : school) {
            schools += school + ",";
        }
        int end = schools.length() - 1;
        schools = schools.substring(0, end);

        return schools;
    }

    public String getSelected_school() {
        return selected_school;
    }

    public String getSellingPrice() {
        return selling_price;
    }

    public ArrayList<String> getBook_photo() {
        return book_photo;
    }

    public String getFirstBookImage() {
        return book_image;
    }

    public Boolean isImageExist() {
        return (book_photo.size() != 0);
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

    public boolean isBookmark() {
        return bookmark;
    }

    public void convertBookmark() {
        this.bookmark = !this.bookmark;
    }

    public void setBuy_avail(int avail) {
        this.buy_avail = avail;
    }
}
