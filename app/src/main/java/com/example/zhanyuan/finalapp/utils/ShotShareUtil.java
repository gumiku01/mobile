package com.example.zhanyuan.finalapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShotShareUtil {

    /**截屏分享，供外部调用**/
    public static void shotShare(Context context, View view){
        //截屏
        String path=screenShot(context, view);
        //分享
        if(path != null && path != ""){
            ShareImage(context,path);
        }
    }

    public static void ShareCanvas(Canvas canvas){

    }

    /**获取截屏**/
    private static String screenShot(Context context, View view){
        String imagePath=null;
        Bitmap bitmap;

        if(view == null) {
            View v1 = ((Activity) context).getWindow().getDecorView();
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
        }
        else{
            bitmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(bitmap));
        }

        if(bitmap!=null){
            try {
                // 图片文件路径
                imagePath = getDiskCachePath(context)+"share.png";
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                return imagePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**分享**/
    private static void ShareImage(Context context, String imagePath){
        if (imagePath != null){
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            File file = new File(imagePath);

            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file));// 分享的内容
            intent.setType("image/*");// 分享发送的数据类型
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Share screen shot");
            if(intent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(chooser);
            }
        } else {
            //ToastUtil.shortShow(context,"先截屏，再分享");
        }
    }

    /**获取缓存路径(内存不足时数据会消失，应用删除的时候，数据会被清理掉)**/
    private static String getDiskCachePath(Context context) {
        String cachePath = null;
        //手机内部缓存路径
        //------/data/user/0/package_name/cache/
        String innerPath = context.getCacheDir().getAbsolutePath() + File.separator;
        if (isSdcardExist()) {
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null) {
                //sdcard上缓存路径
                //--------/storage/emulated/0/Android/data/package_name/cache/
                String ousidePath = cacheFile.getAbsolutePath() + File.separator;
                if (usefulFilePath(ousidePath)) {
                    cachePath = ousidePath;
                } else {
                    //若此路径下无法执行读写,则是android4.4系统为防止外置sdk存储混乱，不好管理，禁用了外置sdk的读写
                    cachePath = innerPath;
                }
            } else {
                //AppContext.getInstance().getExternalCacheDir()可能为空，原因是sdcard死锁，需要重启手机
                cachePath = innerPath;
            }
        }else{
            cachePath = innerPath;
        }
        return cachePath;
    }

    private static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**判断文件路径的有效性**/
    private static boolean usefulFilePath(String path) {
        if (path != null && path != "") {
            String tempPath = path + "demo.txt";
            File file = new File(tempPath);
            if (file.exists()) {
                return true;
            } else {
                try {
                    if (file.createNewFile()) {
                        file.delete();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
