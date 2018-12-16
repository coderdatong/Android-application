package top.thanks_code.bookcircle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.bean.ImageItem;

public class ImagePublishAdapter extends BaseAdapter {
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private Context mContext;

    public ImagePublishAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
            return mDataList.size();
    }

    public Object getItem(int position) {
        return mDataList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        //所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = View.inflate(mContext, R.layout.item_publish, null);
        ImageItem item = mDataList.get(position);
        ImageView imageIv = (ImageView) convertView.findViewById(R.id.item_grid_image);
        if (item.getSourcePath() == null){
            Glide.with(mContext).load(item.getId()).into(imageIv);
        }else {
            Glide.with(mContext).load(item.getSourcePath()).into(imageIv);
        }
        return convertView;
    }

}
