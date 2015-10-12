package com.dave.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dave.csdn.R;
import com.dave.entity.News;
import com.dave.util.Constants;
import com.dave.util.DataUtil;
import com.dave.util.Http;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsContentAdapter extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<News> list;
	
	private SpannableStringBuilder htmlSpannable;
	
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public NewsContentAdapter(Context c) {
		super();
		layoutInflater = (LayoutInflater) LayoutInflater.from(c);
		list = new ArrayList<News>();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
	public void setList(List<News> list){
		this.list = list;
	}
	public void addList(List<News> list){
		this.list.addAll(list);
 	}
	public void clearList(){
		this.list.clear();
 	}
	public List<News> getList(){
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
		News item = list.get(position); // 获取当前项数据
		if (null == convertView) {
			holder = new ViewHolder();
			switch(item.getState()){
			case Constants.DEF_NEWS_ITEM_TYPE.TITLE:
				convertView = layoutInflater.inflate(R.layout.news_content_title_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_NEWS_ITEM_TYPE.SUMMARY:
				convertView = layoutInflater.inflate(R.layout.news_content_summary_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_NEWS_ITEM_TYPE.CONTENT:
				convertView = layoutInflater.inflate(R.layout.news_content_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			case Constants.DEF_NEWS_ITEM_TYPE.IMG:
				convertView = layoutInflater.inflate(R.layout.news_content_img_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView);
				break;
			case Constants.DEF_NEWS_ITEM_TYPE.BOLD_TITLE:
				convertView = layoutInflater.inflate(R.layout.news_content_bold_title_item, null);
				holder.content = (TextView) convertView.findViewById(R.id.text);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (null != item) {
			switch(item.getState()){
			case Constants.DEF_NEWS_ITEM_TYPE.IMG:
				imageLoader.displayImage(item.getContent(), holder.image, options);
				break;
			default:
//				holder.content.setText(item.getContent());
				holder.content.setText(Html.fromHtml(item.getContent(), null, new MyTagHandler()));
//				holder.content.setText(Html.fromHtml("<ul><bold>加粗</bold>sdfsdf<ul>", null, new MyTagHandler()));
				break;
			}
		}
		return convertView;
	}
	@Override
	public int getViewTypeCount() {
		return 5;
	}
	

	@Override
	public int getItemViewType(int position) {
		switch(list.get(position).getState()){
		case Constants.DEF_NEWS_ITEM_TYPE.TITLE:
			return 0;
		case Constants.DEF_NEWS_ITEM_TYPE.SUMMARY:
			return 1;
		case Constants.DEF_NEWS_ITEM_TYPE.CONTENT:
			return 2;
		case Constants.DEF_NEWS_ITEM_TYPE.IMG:
			return 3;
		case Constants.DEF_NEWS_ITEM_TYPE.BOLD_TITLE:
			return 4;
		}
		return 1;
	}
    @Override    
    public boolean isEnabled(int position) {   
    	switch(list.get(position).getState()){
		case Constants.DEF_NEWS_ITEM_TYPE.IMG:
			return true;
		default:
			return false;
		}
    }  
    private class ViewHolder {
    	TextView id;
        TextView date;
        TextView title;
        TextView content;
        ImageView image;
    }
}
