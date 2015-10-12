package com.dave.util;

public class PageUtil {
	public static int page = 1;
//	public static int totalNum = 71;
	
	public static int contentMutiPages;
	public static boolean contentFirstPage = true;
	
	public static void setPageStart(){
		page = 2;
	}
	public static void setPage(int num){
		page = num;
	}
	public static String getCurrentPage(){
		return page + "";
	}
	public static void addPage(){
		page ++;
	}
	
//	public static void setTotalNum(String num){
//		totalNum = Integer.valueOf(num);
//	}
//	public static boolean isLastPage(){
//		if(page * 70 >= totalNum){
//			System.out.println("当前页为最后一页");
//			return true;
//		}
//		return false;
//	}
	
}
