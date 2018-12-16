package top.thanks_code.bookcircle.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.nereo.multi_image_selector.MultiImageSelector;
import top.thanks_code.bookcircle.base.BaseActivity;
import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.adapter.ImagePublishAdapter;
import top.thanks_code.bookcircle.bean.BookInfo;
import top.thanks_code.bookcircle.bean.ImageItem;
import util.MyUtil;

/**
 * Created by Administrator on 2017/10/7.
 */

public class UploadActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_desc;
    private Spinner mSpinner;

    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private ArrayList<String> mSelectPath;
    private ImagePublishAdapter mAdapter;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private GridView mGridView;
    private boolean isFirst = true;
    private boolean isOnece = true;
    private int size = 0;
    private String str;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };
    private String TAG = "ActivityAddPost2";
    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initViews();
    }

    private void initViews() {
        et_name = (EditText) findViewById(R.id.id_et_name);
        et_desc = (EditText) findViewById(R.id.id_et_desc);
        mSpinner = (Spinner) findViewById(R.id.id_sp);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                str = (String) mSpinner.getSelectedItem();
                //把该值传给 TextView
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        mDataList.clear();
        ImageItem item = new ImageItem();
        item.setId(R.drawable.icon_addpic_unfocused);
        mDataList.add(item);
        mGridView = (GridView) findViewById(R.id.id_gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mGridView.getAdapter().getCount()-1){
                    //Toast.makeText(UploadActivity.this, "最后一个", Toast.LENGTH_SHORT).show();
                    pickImage(mGridView);
                }else {
                    /*Toast.makeText(UploadActivity.this, "这是第"+(position+1)+"个,总共"+mGridView.getAdapter().getCount()+"个",
                            Toast.LENGTH_SHORT).show();*/
                }
            }
        });
    }

    //选择图片
    public void pickImage(View v) {
        //Toast.makeText(this, ""+size, Toast.LENGTH_SHORT).show();
        if (size >= 1){
            Toast.makeText(this, "图片数量不能超过一张", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            //是否打开照相机
            boolean showCamera = true;
            //最大照片数量
            int maxNum = 9;
            MultiImageSelector selector = MultiImageSelector.create(UploadActivity.this);
            //是否打开照相机
            selector.showCamera(showCamera);
            //设置最大可选择照片数量
            selector.count(maxNum);
            //if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
            //设置单选模式
            selector.single();
            //} else {
            //设置多选模式
            //selector.multi();
            //}
            selector.origin(mSelectPath);
            selector.start(UploadActivity.this, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage(mGridView);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                //获取选中的图片的路劲
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                if (isOnece) {
                    mDataList.remove(0);
                    isOnece = false;
                }else {
                    mDataList.remove(mDataList.size()-1);
                }
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                    ImageItem item = new ImageItem();
                    item.setSourcePath(p);
                    size++;
                    mDataList.add(item);
                }

                if (isFirst){
                    isFirst = false;
                    ImageItem item = new ImageItem();
                    item.setId(R.drawable.icon_addpic_unfocused);
                    mDataList.add(item);
                    mHandler.sendMessage(new Message());
                    return;
                }else {
                    ImageItem item = new ImageItem();
                    item.setId(R.drawable.icon_addpic_unfocused);
                    mDataList.add(item);
                    mHandler.sendMessage(new Message());
                    //Toast.makeText(this, "" + sb.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void finishThis(View view) {
        finish();
    }

    public void uploadeInfo(View view) {
        if (isClick){
            return;
        }
        if (!isClick){
            isClick = true;
        }

        String name = et_name.getText().toString();
        String desc = et_desc.getText().toString();
        if (name.equals("") || name == null){
            Toast.makeText(this, "必须填写标题！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mSelectPath.equals("") || mSelectPath == null){
            Toast.makeText(this, "必须选一张图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (desc.equals("") || desc == null){
            Toast.makeText(this, "必须填写内容！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (str.equals("") || str == null){
            Toast.makeText(this, "必须选择类别！", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap b = MyUtil.ratio(mSelectPath.get(0),500,500);
        MyUtil.saveCroppedImage(b);
        String picPath = "/sdcard/myFolder/temp_cropped.jpg";
        final BmobFile pic = new BmobFile(new File(picPath));
        final BookInfo bookInfo = new BookInfo();
        bookInfo.setName(name);
        bookInfo.setDesc(desc);
        bookInfo.setClassfy(str);
        bookInfo.setTime("2017.10.7");
        pic.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    bookInfo.setIcon(pic);
                    bookInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                toast("发布成功！");
                                finish();
                            }else {
                                toast("发布失败！"+e.getMessage());
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
