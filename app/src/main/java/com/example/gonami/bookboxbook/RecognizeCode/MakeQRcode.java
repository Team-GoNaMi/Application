package com.example.gonami.bookboxbook.RecognizeCode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MakeQRcode extends AppCompatActivity {

    private ImageView img_qr;
    private TextView tv_bb_info;
    private Bitmap bit_qr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookboxbook);
        img_qr = findViewById(R.id.img_qr);
        tv_bb_info = findViewById(R.id.tv_bb_info);

        bit_qr = generateQRCode("tradenum");
        img_qr.setImageBitmap(bit_qr);
    }

    public static Bitmap generateQRCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap bitmap = null;
        try {
            bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 200, 200));
        }catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bmp.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

}
