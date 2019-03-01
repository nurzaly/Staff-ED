package my.gov.ilpsdk.apps.stafed;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import my.gov.ilpsdk.apps.stafed.Services.AppDatabase;
import my.gov.ilpsdk.apps.stafed.Utils.Helper;
import my.gov.ilpsdk.apps.stafed.data.Constant;
import my.gov.ilpsdk.apps.stafed.data.GlobalVariable;
import my.gov.ilpsdk.apps.stafed.data.Tools;
import my.gov.ilpsdk.apps.stafed.model.Bahagian;
import my.gov.ilpsdk.apps.stafed.model.Staff;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";
    private AppDatabase db;
    private SharedPreferences config;
    private GlobalVariable global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        config = getSharedPreferences(Constant.KEY_CONFIG, MODE_PRIVATE);
        global = new GlobalVariable();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constant.KEY_DB_DIREKTORI)
                .allowMainThreadQueries()
                .build();

        download_bahagian();
        download_staf();

        check_login();
//
        bindLogo();
        //check_database();

        try {
            Tools.systemBarLolipop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check_login(){
        if(config.getBoolean(Constant.KEY_LOGIN,false) == true){
            //goto_home_skip_update_check();
            goto_home();
        }
        else{
            login();
            finish();
        }
    }

    private void login(){
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }

    private void check_database(){
        try {
            if(!config.getString(Constant.KEY_DOWNLOAD_STAF,null).equals("success")){
                download_staf();
            }
        } catch (Exception e) {
            e.printStackTrace();
            download_staf();
        }

        try {
            if(!config.getString(Constant.KEY_DOWNLOAD_BAHAGIAN,null).equals("success")){
                download_bahagian();
            }
        } catch (Exception e) {
            e.printStackTrace();
            download_bahagian();
        }
    }

    private void goto_home_skip_update_check(){
        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        finish();

    }

    private void goto_home(){
        //Helper.showToast(getApplicationContext(),"gotohome");
        Log.d(TAG, "goto_home: staf " + global.isDonwload_staff());
        Log.d(TAG, "goto_home: bahagian " + global.isDonwload_bahagian());
        if(global.isDonwload_bahagian() == true && global.isDonwload_staff() == true){
            Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
            startActivity(i);
            finish();
        }
        else{
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    goto_home();
                }
            };
//        // Show splash screen for 3 seconds
            new Timer().schedule(task, 1000);
        }

    }
    private void download_bahagian(){
        AndroidNetworking.get(Constant.URL_GET_ALL_BAHAGIAN)
                .build()
                .getAsObjectList(Bahagian.class, new ParsedRequestListener<List<Bahagian>>() {

                    @Override
                    public void onResponse(List<Bahagian> response) {
                        if(response.size() > 0){
                            db.BahagianDao().deleteBahagian();
                            for (int i = 0; i < response.size(); i++) {
                                db.BahagianDao().insertAll(response.get(i));
                            }
                            SharedPreferences.Editor editor = getSharedPreferences(Constant.KEY_CONFIG,MODE_PRIVATE).edit();
                            editor.putString(Constant.KEY_DOWNLOAD_BAHAGIAN,"success");
                            editor.apply();

                            global.setDonwload_bahagian(true);
                            //Toast.makeText(getApplicationContext(),"Bahagian updated",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                        global.setDonwload_bahagian(true);
                    }
                });
    }

    private void download_staf(){
        AndroidNetworking.get(Constant.URL_GET_ALL_STAFF)
                .build()
                .getAsObjectList(Staff.class, new ParsedRequestListener<List<Staff>>() {
                    @Override
                    public void onResponse(List<Staff> response) {

                        if(response.size() > 0){
                            db.staffDao().deleteStaff();
                            for (int i = 0; i < response.size(); i++) {
                                db.staffDao().insertAll(response.get(i));
                            }
                            SharedPreferences.Editor editor = getSharedPreferences(Constant.KEY_CONFIG,MODE_PRIVATE).edit();
                            editor.putString(Constant.KEY_DOWNLOAD_STAF,"success");
                            editor.apply();

                            global.setDonwload_staff(true);
                        }
                        //Helper.showToast(getApplicationContext(),"Staff success updated");
                    }
                    @Override
                    public void onError(ANError anError) {
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                        global.setDonwload_staff(true);
                    }
                });
    }

    private void bindLogo(){
        // Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(700);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(700);
        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                splash.startAnimation(animation2);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        splash.startAnimation(animation1);
    }
}
