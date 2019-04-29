//package com.example.gonami.bookboxbook.RecognizeCode;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.content.FileProvider;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.example.gonami.bookboxbook.AddBook.BookSettingActivity;
//
//import java.io.File;
//import java.io.IOException;
//
//public class Photo {
//
//    private static final int FROM_CAMERA = 0;
//    private static final int FROM_ALBUM = 1;
//    private Uri albumURI, photoURI;
//    private String mCurrentPhotoPath;
//
//    public void makeDialog(){
//
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BookSettingActivity.this);
//
//        alt_bld.setTitle("사진 업로드").setCancelable(
//                false).setNeutralButton("사진촬영",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        takePhoto();
//                    }
//                }).setNeutralButton("앨범선택",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int id) {
//                        //앨범에서 선택
//                        selectAlbum();
//                    }
//                }).setNegativeButton("취소   ",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alt_bld.create();
//        alert.show();
//
//    }
//
//    public void selectAlbum(){
//        //앨범 열기
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        intent.setType("image/*");
//        startActivityForResult(intent, FROM_ALBUM);
//
//    }
//
//    public void takePhoto(){
//
//        // 촬영 후 이미지 가져옴
//        String state = Environment.getExternalStorageState();
//
//        if(Environment.MEDIA_MOUNTED.equals(state)){
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if(intent.resolveActivity(getPackageManager())!=null){
//                File photoFile = null;
//                try{
//                    photoFile = createImageFile();
//
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                if(photoFile!=null){
//
//                    photoURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intent, FROM_CAMERA);
//                }
//            }
//
//        }else{
//            Log.v("알림", "저장공간에 접근 불가능");
//            return;
//        }
//    }
//
//    public File createImageFile() throws IOException{
//        String imageFileName = System.currentTimeMillis() + ".jpg";
//        File imageFile= null;
//
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if(!storageDir.exists()){
//            storageDir.mkdirs();
//
//        }
//        imageFile = File.createTempFile(
//                imageFileName,      /* prefix */
//                ".jpg",         /* suffix */
//                storageDir          /* directory */
//        );
//        mCurrentPhotoPath = imageFile.getAbsolutePath();
//        return imageFile;
//
//    }
//
//    public void galleryAddPic(){
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        sendBroadcast(mediaScanIntent);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode != RESULT_OK){
//            return;
//        }
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(IMAGE_HIGHT, IMAGE_WIDTH);
//        imageView = new ImageView(this);
//        imageView.setLayoutParams(lp);
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        imageView.setRotation(90);
//        imageView.setBackgroundColor(Color.GREEN);
//        switch (requestCode){
//            case FROM_ALBUM : {
//                //앨범에서 가져오기
//                if(data.getData()!=null){
//                    try{
//                        File albumFile = null;
//                        albumFile = photo.createImageFile();
//                        albumURI = Uri.fromFile(albumFile);
//                        bookImage.add(data.getData().toString());
//                        imageView.setImageURI(data.getData());
//                        layout.addView(imageView);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                        Log.v("알림","앨범에서 가져오기 에러");
//                    }
//                }
//                break;
//            }
//
//
//            case FROM_CAMERA : {
//                //촬영
//                try{
//                    galleryAddPic();
//                    bookImage.add(photoURI.toString());
//                    imageView.setImageURI(photoURI);
//                    layout.addView(imageView);
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                break;
//            }
//        }
//    }
//}
