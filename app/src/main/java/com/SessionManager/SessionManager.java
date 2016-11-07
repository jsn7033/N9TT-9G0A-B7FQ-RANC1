package com.SessionManager;


import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;



public class SessionManager {

    SharedPreferences pref;
    Editor editor;

    ///-------------personal Details----------------
    String MobileNo;
    String username;



    //-----------------------Profession---------------

    String proruemp;
    String proprofission;
    String mobilno1;
    String proofcaddrs;
    String mobilno2;
    String mobilno3;
    String login;

    String imgname;
    String Userid = "";

    String isChecked;
    String shakefeature;
    String seekBartimerValue="";
    String sencerSencitityValue="";
    String startSencer="";
    Context context;

    public SessionManager(Context cntxt) {
        this.context = cntxt;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void Savepreferences(String key, String Value) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        editor.putString(key, Value);
        editor.commit();
    }
    public String GetstartSencer() {
        startSencer = (pref.getString("startSencer", ""));
        return startSencer;
    }

    public String GetsencerSencitityValue() {
        sencerSencitityValue = (pref.getString("sencerSencitityValue", ""));
        return sencerSencitityValue;
    }
    public String GetseekBartimerValue() {
        seekBartimerValue = (pref.getString("seekBartimerValue", ""));
        return seekBartimerValue;
    }
    public String Getshechfeature() {
        shakefeature = (pref.getString("shakefeature", ""));
        return shakefeature;
    }

    public String GetMobileNo() {
        MobileNo = (pref.getString("MobileNo", ""));
        return MobileNo;
    }


    public String GetisChecked() {
        isChecked = (pref.getString("isChecked", ""));
        return isChecked;
    }
    public String GetLogin() {
        login = (pref.getString("login", ""));
        return login;
    }
    //-----------------------------------------profession

    public String Getimgname() {
        imgname = (pref.getString("imgname", ""));
        return imgname;
    }

    public String GetUserid() {
        Userid = (pref.getString("Userid", ""));
        return Userid;
    }

    public String Getproprofission() {
        proprofission = (pref.getString("proprofission", ""));
        return proprofission;
    }

    public String Getusername() {
        username = (pref.getString("username", ""));
        return username;
    }

    public String Getproofcaddrs() {
        proofcaddrs = (pref.getString("proofcaddrs", ""));
        return proofcaddrs;
    }

    public String Getmobilno1() {
        mobilno1 = (pref.getString("mobilno1", ""));
        return mobilno1;
    }


    public String Getmobilno2() {
        mobilno2 = (pref.getString("mobilno2", ""));
        return mobilno2;
    }

    public String Getmobilno3() {
        mobilno3 = (pref.getString("mobilno3", ""));
        return mobilno3;
    }

    /**
     * author JSN
     * set flag true when opens shake setting screen.
     * @param flag
     */
    public void setFromSetting(boolean flag){
        editor = pref.edit();
        editor.putBoolean("isFromSetting", flag);
        editor.commit();
    }

    public boolean isFromSettingShake(){
        return  (pref.getBoolean("isFromSetting", false));
    }


    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}