package top.thanks_code.bookcircle.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import top.thanks_code.bookcircle.base.BaseActivity;
import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.bean.BookInfo;

/**
 * Created by Administrator on 2017/10/7.
 */

public class ActivityBookInfo extends BaseActivity {

    private TextView tv_name,tv_desc;
    private ImageView iv_ic;
    private BookInfo bookInfo;
    private ImageView mImageView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        initData();
        initViews();
    }

    private void initData() {
        bookInfo = (BookInfo)getIntent().getBundleExtra("bundle").get("book");
    }

    private void initViews() {
        tv_desc = (TextView) findViewById(R.id.id_desc);
        mImageView = (ImageView) findViewById(R.id.id_iv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bitmap bm =((BitmapDrawable) ((ImageView) mImageView).getDrawable()).getBitmap();
                Bundle bundle  = new Bundle();
                bundle.putParcelable("bitmap", bm);
                Intent intent = new Intent(ActivityBookInfo.this,ActivityImageViewer.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);*/
                Bitmap bmp=((BitmapDrawable)mImageView.getDrawable()).getBitmap();
                Intent intent=new Intent(ActivityBookInfo.this,ActivityImageViewer.class);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();
                intent.putExtra("bitmap", bitmapByte);
                startActivity(intent);
            }
        });
        tv_name = (TextView) findViewById(R.id.id_book);
        iv_ic = (ImageView) findViewById(R.id.id_iv);
        if (bookInfo == null){
            toast(" null");
            return;
        }
        //Glide.with(this).load(bookInfo.getIcon().getFileUrl()).into(iv_ic);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getNetWorkBitmap(bookInfo.getIcon().getFileUrl());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_ic.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        tv_desc.setText(bookInfo.getDesc());
        tv_name.setText(bookInfo.getName());
    }

    public void finishThis(View view) {
        finish();
    }

    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return bitmap;
    }
}
