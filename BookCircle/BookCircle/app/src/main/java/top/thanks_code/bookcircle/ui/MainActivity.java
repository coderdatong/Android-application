package top.thanks_code.bookcircle.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.thanks_code.bookcircle.base.BaseActivity;
import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.adapter.LVAdapter;
import top.thanks_code.bookcircle.bean.BookInfo;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ListView mListView;
    private LVAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    private void initData() {
        BmobQuery<BookInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<BookInfo>() {
            @Override
            public void done(List<BookInfo> list, BmobException e) {
                if (e == null){
                    if (list.size() == 0 || list == null){
                        return;
                    }
                    mAdapter = new LVAdapter(list,MainActivity.this);
                    mListView.setAdapter(mAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }else {
                    Log.d(TAG,"服务器异常");
                    toast("服务器异常"+e.getMessage());
                }
            }
        });
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.id_lv);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",(BookInfo)mAdapter.getItem(position));
                Intent intent = new Intent(MainActivity.this,ActivityBookInfo.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_sr);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    public void toUploadActivity(View view) {
        startActivity(new Intent(this,UploadActivity.class));
    }

    public void toSearchActivity(View view) {
        startActivity(new Intent(this,ActivitySearch.class));
    }
}
