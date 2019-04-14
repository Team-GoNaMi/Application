package com.example.gonami.bookboxbook.DataModel;

public class BookTradeInformation {
    private String trade_id;
    private String bookRegister_id;
    private String seller_id;
    private String buyer_id;

    // 거래 상태
    private int status;

    public BookTradeInformation(String trade_id, String bookRegister_id, String seller_id, String buyer_id, int status) {
        this.trade_id = trade_id;
        this.bookRegister_id = bookRegister_id;
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
        this.status = 0;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public String getBookRegister_id() {
        return bookRegister_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public int getStatus() {
        return status;
    }
}
