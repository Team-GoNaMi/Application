package com.example.gonami.bookboxbook.RecognizeCode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MakeQRcode extends AppCompatActivity {

    //Intent data??
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
