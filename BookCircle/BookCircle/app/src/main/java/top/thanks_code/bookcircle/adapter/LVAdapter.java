package top.thanks_code.bookcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import top.thanks_code.bookcircle.R;
import top.thanks_code.bookcircle.bean.BookInfo;

/**
 * Created by Administrator on 2017/10/7.
 */

public class LVAdapter extends BaseAdapter {

    private List<BookInfo> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public LVAdapter(List<BookInfo> list, Context context) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.id_tv_name);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_iv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.id_tv_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BookInfo markerItem = (BookInfo) getItem(position);
        viewHolder.name.setText(markerItem.getName());
        Glide.with(mContext).load(markerItem.getIcon().getFileUrl()).into(viewHolder.icon);
        viewHolder.time.setText(markerItem.getCreatedAt());

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView icon;
        TextView time;
    }

}
