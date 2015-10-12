package com.dave.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dave.csdn.R;
import com.dave.entity.Comment;
import com.dave.util.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class CommentAdapter  extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<Comment> list;
	
	private SpannableStringBuilder htmlSpannable;
	
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public CommentAdapter(Context c) {
		super();
		layoutInflater = (LayoutInflater) LayoutInflater.from(c);
		list = new ArrayList<Comment>();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.biz_topic_vote_submit_default)
				.showImageForEmptyUri(R.drawable.biz_topic_vote_submit_default)
				.showImageOnFail(R.drawable.biz_topic_vote_submit_default).cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
	public void setList(List<Comment> list){
		this.list = list;
	}
	public void addList(List<Comment> list){
		this.list.addAll(list);
 	}
	public void clearList(){
		this.list.clear();
 	}
	public List<Comment> getList(){
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
		Comment item = list.get(position); // 获取当前项数据
		if (null == convertView) {
			holder = new ViewHolder();
			switch(item.getType()){
			case Constants.DEF_COMMENT_TYPE.PARENT:
				convertView = layoutInflater.inflate(R.layout.comment_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.reply = (TextView) convertView.findViewById(R.id.replyCount);
				break;
			case Constants.DEF_COMMENT_TYPE.CHILD:
				convertView = layoutInflater.inflate(R.layout.comment_child_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (null != item) {
			switch(item.getType()){
			case Constants.DEF_COMMENT_TYPE.PARENT:
				holder.name.setText(item.getName());
				holder.content.setText(Html.fromHtml(item.getContent()));
				holder.date.setText(item.getDate());
				holder.reply.setText(item.getReplyCount());
				break;
			case Constants.DEF_COMMENT_TYPE.CHILD:
				holder.name.setText(item.getName());
				holder.content.setText(Html.fromHtml(item.getContent()));
				holder.date.setText(item.getDate());
				break;
			default:
				break;
			}
		}
		return convertView;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	

	@Override
	public int getItemViewType(int position) {
		switch(list.get(position).getType()){
		case Constants.DEF_COMMENT_TYPE.PARENT:
			return 0;
		case Constants.DEF_COMMENT_TYPE.CHILD:
			return 1;
		}
		return 1;
	}
    @Override    
    public boolean isEnabled(int position) {   
    	return true;
    }  
    private class ViewHolder {
    	TextView id;
        TextView date;
        TextView name;
        TextView content;
        ImageView image;
        TextView reply;
    }
}
