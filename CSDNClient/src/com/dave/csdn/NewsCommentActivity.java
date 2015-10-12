package com.dave.csdn;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.entity.Comment;
import com.dave.entity.Page;
import com.dave.util.Constants;
import com.dave.util.DataUtil;
import com.dave.util.Http;
import com.dave.util.URLUtil;
import com.dave.view.CommentAdapter;

public class NewsCommentActivity extends Activity implements IXListViewRefreshListener, IXListViewLoadMore{
	private XListView listView;
	private CommentAdapter adapter;
	
	private ProgressBar progressBar;
	private ImageView reLoadImageView;
	
	private ImageView backBtn;
	private TextView commentTV;
	
	public static String commentCount = "";
	private String url;
	private Page page;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		init();
		initComponent();
       
		listView.setRefreshTime(getDate());
		listView.startRefresh();
	//	new MainTask().execute(url, "refresh");
	}
	private void init(){
		url = getIntent().getExtras().getString("url");
		page = new Page();
	//	page.setPageStart();
		adapter = new CommentAdapter(this);
	//	url = getIntent().getExtras().getString("newsLink");
	//	url = "http://www.csdn.net/article/2013-08-01/2816432-15-steps-to-build-tech-company";
	//	url = "http://www.csdn.net/article/2013-08-02/2816441-mobile-weekly-CryENGINE";
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
				new MainTask().execute(url, "refresh");
			}
		});
		
		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commentTV = (TextView) findViewById(R.id.comment);
		
		listView = (XListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(this);
		listView.setPullLoadEnable(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_no, R.anim.push_right_out);
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String temp = Http.httpGet(URLUtil.getCommnetListURL(params[0], page.getCurrentPage()));
			if(temp == null){
				return Constants.DEF_RESULT_CODE.ERROR;
			}
			List<Comment> list = DataUtil.getComment(temp);
			if(list.size() == 0){
				return Constants.DEF_RESULT_CODE.NO_DATA;
			}
			
			if(params[1].equals(Constants.DEF_TASK_TYPE.LOAD)){
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			}else{
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == Constants.DEF_RESULT_CODE.ERROR){
				Toast.makeText(getApplicationContext(), "网络信号不佳", Toast.LENGTH_SHORT).show();
			//	reLoadImageView.setVisibility(View.VISIBLE);
				listView.stopRefresh(getDate());
				listView.stopLoadMore();
			} else if(result == Constants.DEF_RESULT_CODE.NO_DATA){
				Toast.makeText(getApplicationContext(), "无更多评论", Toast.LENGTH_SHORT).show();
				listView.stopLoadMore();
				listView.stopRefresh(getDate());
			} else if(result == Constants.DEF_RESULT_CODE.LOAD){
				page.addPage();
				adapter.notifyDataSetChanged();
				listView.stopLoadMore();
			} else if(result == Constants.DEF_RESULT_CODE.REFRESH){
				adapter.notifyDataSetChanged();
				listView.stopRefresh(getDate());
				page.setPage(2);
				commentTV.setText("共有评论：" + commentCount);
			}
			progressBar.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onLoadMore() {
		new MainTask().execute(url, Constants.DEF_TASK_TYPE.LOAD);
	}
	@Override
	public void onRefresh() {
		page.setPage(1);
		new MainTask().execute(url, Constants.DEF_TASK_TYPE.REFRESH);
	}
	public String getDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);  
	    return sdf.format(new java.util.Date()); 
	}
}

