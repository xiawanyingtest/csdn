package com.dave.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dave.csdn.R;
import com.dave.entity.News;
import com.dave.entity.NewsItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsListYiDongAdapter extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<NewsItem> list;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public NewsListYiDongAdapter(Context c) {
		super();
		layoutInflater = (LayoutInflater) LayoutInflater.from(c);
		list = new ArrayList<NewsItem>();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20))
				.displayer(new FadeInBitmapDisplayer(300)).build();
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
	public List<NewsItem> getList(){
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
			convertView = layoutInflater.inflate(R.layout.news_item_yidong, null);
			holder = new ViewHolder();
		//	holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.img = (ImageView) convertView.findViewById(R.id.newsImg);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewsItem item = list.get(position); // 获取当前项数据
		if (null != item) {
//			holder.id.setText(position + 1 + "");
			holder.title.setText(item.getTitle());
			holder.content.setText(item.getContent());
			holder.date.setText(item.getDate());
			if(item.getImgLink() != null){
				holder.img.setVisibility(View.VISIBLE);
				imageLoader.displayImage(item.getImgLink(), holder.img, options);
			}else{
				holder.img.setVisibility(View.GONE);
			}
			
		}
		return convertView;
	}
    private class ViewHolder {
    	TextView id;
        TextView date;
        TextView title;
        ImageView img;
        TextView content;
    }
}
