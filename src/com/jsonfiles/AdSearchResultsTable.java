package com.webservice;

public class AdSearchResultsTable {

	String ad_id,ad_categoryname,ad_description;


	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	public String getAd_categoryname() {
		return ad_categoryname;
	}

	public void setAd_categoryname(String ad_categoryname) {
		this.ad_categoryname = ad_categoryname;
	}

	public String getAd_description() {
		return ad_description;
	}

	public void setAd_description(String ad_description) {
		this.ad_description = ad_description;
	}

	public AdSearchResultsTable() {
		super();
		this.ad_id = null;
		this.ad_categoryname = null;
		this.ad_description = null;
	
	}

	public AdSearchResultsTable(String ad_id, String ad_categoryname,
			String ad_description) {
		super();
		this.ad_id = ad_id;
		this.ad_categoryname = ad_categoryname;
		this.ad_description = ad_description;
	}


	


	
	

}
