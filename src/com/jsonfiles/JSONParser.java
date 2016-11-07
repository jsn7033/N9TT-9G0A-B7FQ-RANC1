package com.webservice;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.JsonReader;
import android.util.Log;


public class JSONParser {

	public JSONParser()
	{
	super();	
	}
	
	public UserDetailsTable parseUserDetails(JSONObject object)
	{
		UserDetailsTable userDetail=new UserDetailsTable();
		
		try {
			JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);
			userDetail.setMobileno1(jsonObj.getString("mobileno1"));
			userDetail.setMobileno1(jsonObj.getString("mobileno2"));
			userDetail.setMobileno1(jsonObj.getString("mobileno3"));
			userDetail.setName(jsonObj.getString("name"));
			userDetail.setEmailid(jsonObj.getString("emailid"));
			userDetail.setDob(jsonObj.getString("dob"));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseUserDetails", e.getMessage());
		}
		
		return userDetail;
			
	}
	
	public ArrayList<BotSearchResultsTable> parseBotSearchDetails(JSONObject object)
	{
		ArrayList<BotSearchResultsTable> searchDetail=new ArrayList<BotSearchResultsTable>();
		
		try {
			/*JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);
			
			searchDetail.setResult_desc(jsonObj.getString("result_desc"));
			searchDetail.setLink(jsonObj.getString("link"));*/
			
			JSONArray jsonArray=object.getJSONArray("Value");
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				jsonObj=jsonArray.getJSONObject(i);
				searchDetail.add(new BotSearchResultsTable(jsonObj.getString("result_desc"), jsonObj.getString("link")));
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseUserDetails", e.getMessage());
		}
		
		return searchDetail;
			
	}
	
	//function to parse ad search details
	public ArrayList<AdSearchResultsTable> parseAdSearchDetails(JSONObject object)
	{
		ArrayList<AdSearchResultsTable> searchDetail=new ArrayList<AdSearchResultsTable>();
		
		try {
			
			JSONArray jsonArray=object.getJSONArray("Value");
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				jsonObj=jsonArray.getJSONObject(i);
				searchDetail.add(new AdSearchResultsTable(jsonObj.getString("ad_id"), jsonObj.getString("ad_categoryname"),jsonObj.getString("ad_description")));
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseUserDetails", e.getMessage());
		}
		
		return searchDetail;
			
	}
	
	//function to parse broadcast details
		public ArrayList<BroadcastResultsTable> parseBroadcastDetails(JSONObject object)
		{
			ArrayList<BroadcastResultsTable> broadcastDetail=new ArrayList<BroadcastResultsTable>();
			
			try {
				
				JSONArray jsonArray=object.getJSONArray("Value");
				JSONObject jsonObj=null;
				for(int i=0;i<jsonArray.length();i++)
				{
					jsonObj=jsonArray.getJSONObject(i);
					broadcastDetail.add(new BroadcastResultsTable(jsonObj.getString("broadcast_id"), jsonObj.getString("name"), jsonObj.getString("residence_city"), jsonObj.getString("bronze_badges_count"), jsonObj.getString("silver_badges_count"), jsonObj.getString("gold_badges_count"), jsonObj.getString("broadcast_content"), jsonObj.getString("broadcast_image"), jsonObj.getString("useful_count"), jsonObj.getString("useless_count"), jsonObj.getString("broadcast_date")));
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("JSONParser => parsebroadcastDetails", e.getMessage());
			}
			
			return broadcastDetail;
				
		}
	
	
	
	/*public ArrayList<DeptTable> parseDepartment(JSONObject object)
	{
		ArrayList<DeptTable> arrayList=new ArrayList<DeptTable>();
		try {
			JSONArray jsonArray=object.getJSONArray("Value");
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				jsonObj=jsonArray.getJSONObject(i);
				arrayList.add(new DeptTable(jsonObj.getInt("no"), jsonObj.getString("name")));
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return arrayList;
	}*/
	
	/*public ArrayList<DeptTable> parseDepartment(JSONObject object)
	{
		ArrayList<DeptTable> arrayList=new ArrayList<DeptTable>();
		try {
			JSONArray jsonArray=object.getJSONArray("Value");
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				jsonObj=jsonArray.getJSONObject(i);
				arrayList.add(new DeptTable(jsonObj.getString("dep_id"), jsonObj.getString("dep_name")));
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return arrayList;
	}
	//code to parse departmentwise search results
	public ArrayList<GetDepartmentWiseResults> parseDepartmentwise_searchresults(JSONObject object)
	{
		ArrayList<GetDepartmentWiseResults> arrayList=new ArrayList<GetDepartmentWiseResults>();
		try {
			JSONArray jsonArray=object.getJSONArray("Value");
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				jsonObj=jsonArray.getJSONObject(i);
				arrayList.add(new GetDepartmentWiseResults(jsonObj.getString("dr_id"),jsonObj.getString("dr_name"), jsonObj.getString("symptom_id"),jsonObj.getString("h_id"), jsonObj.getString("h_name"), jsonObj.getString("h_address"), jsonObj.getString("h_latitude"), jsonObj.getString("h_longitude")));
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return arrayList;
	}
	
	@SuppressWarnings({ "rawtypes", "unused","unchecked"})
	public ArrayList parseCity(JSONObject object)
	{
		
		ArrayList<String> cityName=new ArrayList<String>();
		try {
			JSONArray jsonArray=object.getJSONArray("Value");			
			JSONObject jsonObj=null;
			for(int i=0;i<jsonArray.length();i++)
			{
				cityName.add(jsonArray.getString(i));
				
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return cityName;
	}
	
	public List<ArrayList> parsedepartment(JSONObject object)
	{
		List<ArrayList> deptidname=new ArrayList<ArrayList>();
		ArrayList<String> deptid=new ArrayList<String>();
		ArrayList<String> deptname=new ArrayList<String>();
		try {
			JSONArray jsonArray=object.getJSONArray("Value");			
			JSONObject jsonObj=null;
			deptid.add(jsonArray.getString(0).replace("[", "").replace("]", "").replace("\"", ""));
			deptname.add(jsonArray.getString(1).replace("[", "").replace("]", "").replace("\"", ""));
			deptidname.add(deptid);
			deptidname.add(deptname);
			for(int i=0;i<jsonArray.length();i++)
			{
				cityName.add(jsonArray.getString(i));
				
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return deptidname;
	}
	
	
	public boolean parseUserAuth(JSONObject object)
	{	boolean userAtuh=false;
			try {
				userAtuh= object.getBoolean("Value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("JSONParser => parseUserAuth", e.getMessage());
			}
			
			return userAtuh;
	}
	
	public Searched_doctor_details_table parseUserDetails(JSONObject object)
	{
		Searched_doctor_details_table d_detail=new Searched_doctor_details_table();
		
		try {
            
			JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);
			d_detail.setDr_name(jsonObj.getString("dr_name"));
			d_detail.setSymptom_id(jsonObj.getString("symptom_id"));
			d_detail.setDegree(jsonObj.getString("degree"));
			d_detail.setDr_experience(jsonObj.getString("dr_experience"));
			d_detail.setDoctor_fees(jsonObj.getString("doctor_fees"));
			d_detail.setDoctor_details(jsonObj.getString("doctor_details"));
			d_detail.setVisit_days(jsonObj.getString("visit_days"));
			d_detail.setVisit_start_time(jsonObj.getString("visit_start_time"));
			d_detail.setVisit_end_time(jsonObj.getString("visit_end_time"));
			d_detail.setVisit_end_time(jsonObj.getString("h_id"));
			d_detail.setVisit_end_time(jsonObj.getString("h_name"));
			d_detail.setVisit_end_time(jsonObj.getString("h_address"));
			d_detail.setVisit_end_time(jsonObj.getString("h_phone"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseUserDetails", e.getMessage());
		}
		
		return d_detail;
			
	}
	
	public UserDetailsDatatable parseGetUserData(JSONObject object)
	{
		UserDetailsDatatable userDetail=new UserDetailsDatatable();
		try {
			
			JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);
			String name=jsonObj.getString("name");
			String DOB=jsonObj.getString("dob");
			String DEPENDENT=jsonObj.getString("number_of_dependents");
			userDetail.setName(name);
			userDetail.setMobile_no(jsonObj.getString("mobile_no"));
			userDetail.setEmail(jsonObj.getString("email"));
			userDetail.setCity_id(jsonObj.getString("city_id"));
			userDetail.setEmergency_mobile_no(jsonObj.getString("emergency_mobile_no"));
			userDetail.setEmergency_person_name(jsonObj.getString("emergency_person_name"));
			userDetail.setHome_address(jsonObj.getString("home_address"));
			userDetail.setOffice_address(jsonObj.getString("office_address"));
			userDetail.setBlood_group(jsonObj.getString("blood_group"));
			userDetail.setExisting_condition(jsonObj.getString("existing_condition"));
			userDetail.setHealth_insurance(jsonObj.getString("health_insurance"));
			userDetail.setDob(DOB);
			userDetail.setMarital_status(jsonObj.getString("marital_status"));
			userDetail.setNumber_of_dependents(DEPENDENT);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("JSONParser => parseDepartment", e.getMessage());
		}
		return userDetail;
	}
	public String parseRegisterUser(JSONObject object){
		String USERID="";
		try{
			JSONArray jsonArray=object.getJSONArray("Value");			
			JSONObject jsonObj=null;
			USERID=jsonArray.getString(0);
			
			USERID=object.getString("Value");
			
		}catch(JSONException e){
			
		}catch(Exception e){
			
		}
		return USERID;
	}
	*/
}
