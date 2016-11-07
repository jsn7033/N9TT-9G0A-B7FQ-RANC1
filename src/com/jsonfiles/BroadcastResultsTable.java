package com.webservice;

public class BroadcastResultsTable {

	String broadcast_id,name,residence_city,bronze_badges_count,silver_badges_count,gold_badges_count,broadcast_content,broadcast_image,useful_count,useless_count,broadcast_date;


	public BroadcastResultsTable() {
		super();
		this.broadcast_id = null;
		this.name = null;
		this.residence_city = null;
		this.bronze_badges_count = null;
		this.silver_badges_count = null;
		this.gold_badges_count = null;
		this.broadcast_content = null;
		this.broadcast_image = null;
		this.useful_count = null;
		this.useless_count = null;
		this.broadcast_date = null;
	
	}
	public BroadcastResultsTable(String broadcast_id, String name,
			String residence_city, String bronze_badges_count,
			String silver_badges_count, String gold_badges_count,
			String broadcast_content, String broadcast_image,
			String useful_count, String useless_count, String broadcast_date) {
		super();
		this.broadcast_id = broadcast_id;
		this.name = name;
		this.residence_city = residence_city;
		this.bronze_badges_count = bronze_badges_count;
		this.silver_badges_count = silver_badges_count;
		this.gold_badges_count = gold_badges_count;
		this.broadcast_content = broadcast_content;
		this.broadcast_image = broadcast_image;
		this.useful_count = useful_count;
		this.useless_count = useless_count;
		this.broadcast_date = broadcast_date;
	}
	public String getBroadcast_id() {
		return broadcast_id;
	}
	public void setBroadcast_id(String broadcast_id) {
		this.broadcast_id = broadcast_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResidence_city() {
		return residence_city;
	}
	public void setResidence_city(String residence_city) {
		this.residence_city = residence_city;
	}
	public String getBronze_badges_count() {
		return bronze_badges_count;
	}
	public void setBronze_badges_count(String bronze_badges_count) {
		this.bronze_badges_count = bronze_badges_count;
	}
	public String getSilver_badges_count() {
		return silver_badges_count;
	}
	public void setSilver_badges_count(String silver_badges_count) {
		this.silver_badges_count = silver_badges_count;
	}
	public String getGold_badges_count() {
		return gold_badges_count;
	}
	public void setGold_badges_count(String gold_badges_count) {
		this.gold_badges_count = gold_badges_count;
	}
	public String getBroadcast_content() {
		return broadcast_content;
	}
	public void setBroadcast_content(String broadcast_content) {
		this.broadcast_content = broadcast_content;
	}
	public String getBroadcast_image() {
		return broadcast_image;
	}
	public void setBroadcast_image(String broadcast_image) {
		this.broadcast_image = broadcast_image;
	}
	public String getUseful_count() {
		return useful_count;
	}
	public void setUseful_count(String useful_count) {
		this.useful_count = useful_count;
	}
	public String getUseless_count() {
		return useless_count;
	}
	public void setUseless_count(String useless_count) {
		this.useless_count = useless_count;
	}
	public String getBroadcast_date() {
		return broadcast_date;
	}
	public void setBroadcast_date(String broadcast_date) {
		this.broadcast_date = broadcast_date;
	}


	
	

}
