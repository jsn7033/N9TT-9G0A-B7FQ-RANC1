package com.webservice;

public class BotSearchResultsTable {

	String result_desc,link;


	public BotSearchResultsTable() {
		super();
		this.result_desc = null;
		this.link = null;
	
	}


	public String getResult_desc() {
		return result_desc;
	}


	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public BotSearchResultsTable(String result_desc, String link) {
		super();
		this.result_desc = result_desc;
		this.link = link;
	}


	
	

}
