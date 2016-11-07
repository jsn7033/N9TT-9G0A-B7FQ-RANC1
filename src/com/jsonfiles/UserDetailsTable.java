package com.webservice;

public class UserDetailsTable {

	String mobileno1,mobileno2,mobileno3,name,emailid,dob;


	public UserDetailsTable() {
		super();
		this.mobileno1 = null;
		this.mobileno2 = null;
		this.mobileno3 = null;
		this.name = null;
		this.emailid = null;
		this.dob = null;
	
	}

	public String getMobileno1() {
		return mobileno1;
	}

	public void setMobileno1(String mobileno1) {
		this.mobileno1 = mobileno1;
	}

	public String getMobileno2() {
		return mobileno2;
	}

	public void setMobileno2(String mobileno2) {
		this.mobileno2 = mobileno2;
	}

	public String getMobileno3() {
		return mobileno3;
	}

	public void setMobileno3(String mobileno3) {
		this.mobileno3 = mobileno3;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public UserDetailsTable(String mobileno1, String mobileno2,
			String mobileno3, String name, String emailid, String dob) {
		super();
		this.mobileno1 = mobileno1;
		this.mobileno2 = mobileno2;
		this.mobileno3 = mobileno3;
		this.name = name;
		this.emailid = emailid;
		this.dob = dob;
	}

	
	

}
