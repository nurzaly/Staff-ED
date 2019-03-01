package my.gov.ilpsdk.apps.stafed.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import my.gov.ilpsdk.apps.stafed.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ResourceType")
public final class Constant {

    public static String DOMAIN = "http://apps.ilpsdk.gov.my/api-stafed/";
    public static String URL_GET_ALL_STAFF = DOMAIN + "get-all-staff";
    public static String URL_GET_ALL_BAHAGIAN = DOMAIN + "get-all-bahagian";
    public static String URL_AVATAR = "http://apps.ilpsdk.gov.my/stafed/images/";
    public static String URL_LOGIN = DOMAIN + "login";
    public static String URL_UPDATE_STAF = DOMAIN + "update-staf";
    public static String URL_CHANGE_PASSWORD = DOMAIN + "change-password";

    public static String KEY_DB_DIREKTORI = "direktori";
    public static String KEY_DOWNLOAD_STAF = "download_staf";
    public static String KEY_DOWNLOAD_BAHAGIAN = "download_bahagian";

    public static String KEY_CONFIG = "config";

    //login
    public static final String KEY_LOGIN = "login";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAMA = "nama";

    public static String VAR_CURRENT_GROUP = "no";


    public static Resources getStrRes(Context context){
        return context.getResources();
    }

    public static String formatTime(long time){
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if(date.get(Calendar.YEAR)==curDate.get(Calendar.YEAR)){
            if(date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR) ){
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            }
            else{
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        }
        else{
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("ERROR API",  e.getMessage());
        }

        return f.floatValue();
    }

}
