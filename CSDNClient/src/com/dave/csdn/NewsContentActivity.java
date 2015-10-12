package com.dave.csdn;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.XListView;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dave.util.Constants;
import com.dave.util.DataUtil;
import com.dave.util.Http;
import com.dave.view.NewsContentAdapter;

public class NewsContentActivity extends Activity implements IXListViewLoadMore{
	private XListView listView;
	private NewsContentAdapter adapter;
	
	private ProgressBar progressBar;
	private ImageView reLoadImageView;
	
	private ImageView backBtn;
	private ImageView commentBtn;
	
	public static String url;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		
		init();
		initComponent();
		
		
	//	listView.NotRefreshAtBegin();
		new MainTask().execute(url, Constants.DEF_TASK_TYPE.FIRST);
	}
	private void init(){
		adapter = new NewsContentAdapter(this);
		url = getIntent().getExtras().getString("newsLink");
	//	url = "http://www.csdn.net/article/2013-06-18/2815806-GitHub-iOS-open-source-projects-two/1";
	}
	private void initComponent(){
		progressBar = (ProgressBar) findViewById(R.id.newsContentPro);
		reLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
		reLoadImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("click");
				reLoadImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				new MainTask().execute(url, Constants.DEF_TASK_TYPE.FIRST);
			}
		});
		
		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		commentBtn = (ImageView) findViewById(R.id.comment);
		commentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(NewsContentActivity.this, NewsCommentActivity.class);
				i.putExtra("url", url);
				startActivity(i);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
			}
		});
		
		listView = (XListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		listView.disablePullRefreash();
		listView.setPullLoadEnable(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int state = adapter.getList().get(arg2-1).getState();
				switch(state){
				case Constants.DEF_NEWS_ITEM_TYPE.IMG:
					String url = adapter.getList().get(arg2-1).getImgLink();
					Intent i = new Intent();
					i.setClass(NewsContentActivity.this, ImageActivity.class);
					i.putExtra("url", url);
					startActivity(i);
					break;
				default:
					break;
				}
				
			}
		});
		
	}
	
	@Override
	public void finish() {
		DataUtil.resetPages();
		super.finish();
		overridePendingTransition(R.anim.push_no, R.anim.push_right_out);
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String temp = Http.httpGet(params[0]);
			if(temp == null){
				if(params[1].equals(Constants.DEF_TASK_TYPE.FIRST)){
					return Constants.DEF_RESULT_CODE.FIRST;
				}else{
					return Constants.DEF_RESULT_CODE.ERROR;
				}
				
			}
			adapter.addList(DataUtil.getContent(url, temp));
			if(params[1].equals(Constants.DEF_TASK_TYPE.FIRST)){
				return Constants.DEF_RESULT_CODE.REFRESH;
			}
			return Constants.DEF_RESULT_CODE.LOAD;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == Constants.DEF_RESULT_CODE.FIRST){
				Toast.makeText(getApplicationContext(), "ÍøÂçÐÅºÅ²»¼Ñ", Toast.LENGTH_SHORT).show();
				reLoadImageView.setVisibility(View.VISIBLE);
				//listView.stopLoadMore();
			}else if(result == Constants.DEF_RESULT_CODE.ERROR){
				Toast.makeText(getApplicationContext(), "ÍøÂçÐÅºÅ²»¼Ñ", Toast.LENGTH_SHORT).show();
				listView.stopLoadMore();
			}else if(result == Constants.DEF_RESULT_CODE.REFRESH){
				adapter.notifyDataSetChanged();
			}else{
				adapter.notifyDataSetChanged();
				listView.stopLoadMore();
			}
			progressBar.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onLoadMore() {
		if(!DataUtil.contentLastPage){
			new MainTask().execute(url, Constants.DEF_TASK_TYPE.NOR_FIRST);
		}else{
			listView.stopLoadMore(" -- THE END -- ");
	//		listView.disablePullLoad();
		}
	}
}
