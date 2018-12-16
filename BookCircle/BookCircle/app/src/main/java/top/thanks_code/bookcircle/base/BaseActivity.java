package top.thanks_code.bookcircle.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/10/7.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Bmob.initialize(this,"e2c9a50d098c8a95fd36d64a3a9b22b9");
    }

    public void toast(String msg){

        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();

    }
}
