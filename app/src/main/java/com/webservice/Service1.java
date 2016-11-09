/* JSON API for android appliation */
package com.webservice;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class Service1 {
    private final String urlString = "http://104.238.126.224:8002/handler1.ashx";

    private static String convertStreamToUTF8String(InputStream stream) throws IOException {
        String result = "";
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[4096];
            int readedChars = 0;
            while (readedChars != -1) {
                readedChars = reader.read(buffer);
                if (readedChars > 0)
                    sb.append(buffer, 0, readedChars);
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private String load(String contents) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(60000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());
        w.write(contents);
        w.flush();
        InputStream istream = conn.getInputStream();
        String result = convertStreamToUTF8String(istream);
        Log.i("Webservice", "load: Respose: "+result);
        return result;
    }

    private Object mapObject(Object o) {
        Object finalValue = null;
        if (o.getClass() == String.class) {
            finalValue = o;
        }
        else if (Number.class.isInstance(o)) {
            finalValue = String.valueOf(o);
        } else if (Date.class.isInstance(o)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", new Locale("en", "USA"));
            finalValue = sdf.format((Date)o);
        }
        else if (Collection.class.isInstance(o)) {
            Collection<?> col = (Collection<?>) o;
            JSONArray jarray = new JSONArray();
            for (Object item : col) {
                jarray.put(mapObject(item));
            }
            finalValue = jarray;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            Method[] methods = o.getClass().getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() == o.getClass()
                        && method.getModifiers() == Modifier.PUBLIC
                        && method.getName().startsWith("get")) {
                    String key = method.getName().substring(3);
                    try {
                        Object obj = method.invoke(o,  null);
                        Object value = mapObject(obj);
                        map.put(key, value);
                        finalValue = new JSONObject(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return finalValue;
    }

    public JSONObject InsertUser(String mobileNo,String mac,String gsmID) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertUser");
        p.put("mobileNo",mapObject(mobileNo));
        p.put("mac",mapObject(mac));
        p.put("gsmID",mapObject(gsmID));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject GetOTP(String mobileNo) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "GetOTP");
        p.put("mobileNo",mapObject(mobileNo));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertUpdateUserDetails(String mobile,String UserName,String email,String gender,String dob) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertUpdateUserDetails");
        p.put("mobile",mapObject(mobile));
        p.put("UserName",mapObject(UserName));
        p.put("email",mapObject(email));
        p.put("gender",mapObject(gender));
        p.put("dob",mapObject(dob));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertUpdateUserEmergencyContacts(String mobile,String num1,String num2,String num3) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertUpdateUserEmergencyContacts");
        p.put("mobile",mapObject(mobile));
        p.put("num1",mapObject(num1));
        p.put("num2",mapObject(num2));
        p.put("num3",mapObject(num3));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchUserDetails(String mobile) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchUserDetails");
        p.put("mobile",mapObject(mobile));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateUserPersonalDetails(String mobile,String UserName,String firstName,String lastName,String emailID,String gender,String dob,String maritalstatus,String fathername,String fathermobile,String spousename,String spousemobile,String address,String residence_city,String residence_state) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateUserPersonalDetails");
        p.put("mobile",mapObject(mobile));
        p.put("UserName",mapObject(UserName));
        p.put("firstName",mapObject(firstName));
        p.put("lastName",mapObject(lastName));
        p.put("emailID",mapObject(emailID));
        p.put("gender",mapObject(gender));
        p.put("dob",mapObject(dob));
        p.put("maritalstatus",mapObject(maritalstatus));
        p.put("fathername",mapObject(fathername));
        p.put("fathermobile",mapObject(fathermobile));
        p.put("spousename",mapObject(spousename));
        p.put("spousemobile",mapObject(spousemobile));
        p.put("address",mapObject(address));
        p.put("residence_city",mapObject(residence_city));
        p.put("residence_state",mapObject(residence_state));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateUserProfessionalDetails(String mobile,String employed,String profession,String company,String office_address,String office_city,String office_state,String office_pin) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateUserProfessionalDetails");
        p.put("mobile",mapObject(mobile));
        p.put("employed",mapObject(employed));
        p.put("profession",mapObject(profession));
        p.put("company",mapObject(company));
        p.put("office_address",mapObject(office_address));
        p.put("office_city",mapObject(office_city));
        p.put("office_state",mapObject(office_state));
        p.put("office_pin",mapObject(office_pin));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateUserHealthDetails(String mobile,String bloodgroup,String policyno,String health_remark,String vehicle_no1,String vehicle_no2,String vehicle_no3,String insurance) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateUserHealthDetails");
        p.put("mobile",mapObject(mobile));
        p.put("bloodgroup",mapObject(bloodgroup));
        p.put("policyno",mapObject(policyno));
        p.put("health_remark",mapObject(health_remark));
        p.put("vehicle_no1",mapObject(vehicle_no1));
        p.put("vehicle_no2",mapObject(vehicle_no2));
        p.put("vehicle_no3",mapObject(vehicle_no3));
        p.put("insurance",mapObject(insurance));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchBages(String mobile) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchBages");
        p.put("mobile",mapObject(mobile));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertAdDetails(String ad_type,String ad_category,String ad_categoryname,String ad_title,String ad_productused,String ad_titlename,String ad_description,String ad_keywords,String ad_address,String ad_contactno,String ad_email,String ad_website,String ad_state,String ad_city,String ad_scope,String ad_image,String ad_postedby,String ad_date,String ad_status) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertAdDetails");
        p.put("ad_type",mapObject(ad_type));
        p.put("ad_category",mapObject(ad_category));
        p.put("ad_categoryname",mapObject(ad_categoryname));
        p.put("ad_title",mapObject(ad_title));
        p.put("ad_productused",mapObject(ad_productused));
        p.put("ad_titlename",mapObject(ad_titlename));
        p.put("ad_description",mapObject(ad_description));
        p.put("ad_keywords",mapObject(ad_keywords));
        p.put("ad_address",mapObject(ad_address));
        p.put("ad_contactno",mapObject(ad_contactno));
        p.put("ad_email",mapObject(ad_email));
        p.put("ad_website",mapObject(ad_website));
        p.put("ad_state",mapObject(ad_state));
        p.put("ad_city",mapObject(ad_city));
        p.put("ad_scope",mapObject(ad_scope));
        p.put("ad_image",mapObject(ad_image));
        p.put("ad_postedby",mapObject(ad_postedby));
        p.put("ad_date",mapObject(ad_date));
        p.put("ad_status",mapObject(ad_status));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject SearchAdDetails(String Searchkeyword,String mobileNo) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "SearchAdDetails");
        p.put("Searchkeyword",mapObject(Searchkeyword));
        p.put("mobileNo",mapObject(mobileNo));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchCities(String state) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchCities");
        p.put("state",mapObject(state));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertBroadCastDetails(String mobile,String message,String image,String type) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertBroadCastDetails");
        p.put("mobile",mapObject(mobile));
        p.put("message",mapObject(message));
        p.put("image",mapObject(image));
        p.put("type",mapObject(type));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchBroadcasts(String mobileNo,String postCategory) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchBroadcasts");
        p.put("mobileNo",mapObject(mobileNo));
        p.put("postCategory",mapObject(postCategory));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateBroadCastHit(String mobile,String broadcastID,String hitFlag,String comment) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateBroadCastHit");
        p.put("mobile",mapObject(mobile));
        p.put("broadcastID",mapObject(broadcastID));
        p.put("hitFlag",mapObject(hitFlag));
        p.put("comment",mapObject(comment));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateProfileImage(String mobile,String imgUrl) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateProfileImage");
        p.put("mobile",mapObject(mobile));
        p.put("imgUrl",mapObject(imgUrl));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchComments(String broadcastID) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchComments");
        p.put("broadcastID",mapObject(broadcastID));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateUserNotificationSetting(String mobileNo,String flag) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateUserNotificationSetting");
        p.put("mobileNo",mapObject(mobileNo));
        p.put("flag",mapObject(flag));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchOffers() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchOffers");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertUserOffers(String mobileNo,String offerID) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertUserOffers");
        p.put("mobileNo",mapObject(mobileNo));
        p.put("offerID",mapObject(offerID));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateCoinBalance(String mobile,String coinBalance) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "UpdateCoinBalance");
        p.put("mobile",mapObject(mobile));
        p.put("coinBalance",mapObject(coinBalance));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject FetchUserPurchasesHistory(String mobile) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "FetchUserPurchasesHistory");
        p.put("mobile",mapObject(mobile));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject InsertAlertLocation(String mobileNo,String message,String latitude,String longitude) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface","Service1");
        o.put("method", "InsertAlertLocation");
        p.put("mobileNo",mapObject(mobileNo));
        p.put("message",mapObject(message));
        p.put("latitude",mapObject(latitude));
        p.put("longitude",mapObject(longitude));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

}


