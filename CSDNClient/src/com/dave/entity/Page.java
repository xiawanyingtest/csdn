package com.dave.entity;

public class Page {
	public int page = 1;
	public int contentMutiPages;
	public boolean contentFirstPage = true;
	
	public void setPageStart(){
		page = 2;
	}
	public void setPage(int num){
		page = num;
	}
	public String getCurrentPage(){
		return page + "";
	}
	public  void addPage(){
		page ++;
	}
}
