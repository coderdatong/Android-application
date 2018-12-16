package top.thanks_code.bookcircle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.thanks_code.bookcircle.base.BaseActivity;
import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.adapter.LVAdapter;
import top.thanks_code.bookcircle.bean.BookInfo;

/**
 * Created by Administrator on 2017/10/7.
 */

public class ActivitySearch extends BaseActivity {

    private ListView mListView;
    private EditText et;
    private String name;
    private LVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initData() {
        name = et.getText().toString().trim();
        BmobQuery<BookInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("name", name);
        bmobQuery.findObjects(new FindListener<BookInfo>() {
            @Override
            public void done(List<BookInfo> list, BmobException e) {
                if (e == null) {
                    mAdapter = new LVAdapter(list, ActivitySearch.this);
                    mListView.setAdapter(mAdapter);
                    //toast("ok"+list.size());
                } else {
                    toast("服务器异常" + e.getMessage());
                }
            }
        });
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.id_lv);
        et = (EditText) findViewById(R.id.id_et);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",(BookInfo)mAdapter.getItem(position));
                Intent intent = new Intent(ActivitySearch.this,ActivityBookInfo.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

    public void finishThis(View view) {
        finish();
    }

    public void search(View view) {
        initData();
        closeInputMethod(this);
    }

    /**
     *
     * @param context
     *            关闭输入法，需要一个activity
     */
    public static void closeInputMethod(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
            // TODO: handle exception
            Log.d("", "关闭输入法异常");
        }
    }
}
