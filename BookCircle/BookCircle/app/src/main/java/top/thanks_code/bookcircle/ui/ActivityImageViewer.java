package top.thanks_code.bookcircle.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.base.BaseActivity;

/**
 * Created by Administrator on 2017/10/8.
 */

public class ActivityImageViewer extends BaseActivity {

    private ImageView mImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        initViews();
        initDatas();
    }

    private void initDatas() {
        Intent intent = getIntent();
        if (intent != null) {
            byte[] bis = intent.getByteArrayExtra("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.id_iv);
        if (bitmap == null) {
            return;
        } else {
            mImageView.setImageBitmap(bitmap);
        }
    }


    public void finishThis(View view) {
        finish();
    }
}
