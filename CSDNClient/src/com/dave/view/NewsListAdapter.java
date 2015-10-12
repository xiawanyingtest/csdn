package com.dave.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dave.csdn.R;
import com.dave.entity.NewsItem;


public class NewsListAdapter extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<NewsItem> list;
	
	public NewsListAdapter(Context c) {
		super();
		layoutInflater = (LayoutInflater) LayoutInflater.from(c);
		list = new ArrayList<NewsItem>();
	}
	public void setList(List<NewsItem> list){
		this.list = list;
	}
	public void addList(List<NewsItem> list){
		this.list.addAll(list);
 	}
	public void clearList(){
		this.list.clear();
 	}
	public List<?> getList(){
		return list;
	}
	public void removeItem(int position){
		if(list.size() > 0){
			list.remove(position);
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			// 装载布局文件 app_item.xml
			convertView = layoutInflater.inflate(R.layout.news_item, null);
			holder = new ViewHolder();
			holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewsItem item = list.get(position); // 获取当前项数据
		if (null != item) {
			holder.id.setText(position + 1 + "");
			holder.title.setText(item.getTitle());
			holder.date.setText(item.getDate());
		}
		return convertView;
	}
    private class ViewHolder {
    	TextView id;
        TextView date;
        TextView title;
    }
}
