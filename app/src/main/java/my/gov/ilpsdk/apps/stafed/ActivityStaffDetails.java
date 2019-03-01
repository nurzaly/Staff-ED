package my.gov.ilpsdk.apps.stafed;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import my.gov.ilpsdk.apps.stafed.Services.AppDatabase;
import my.gov.ilpsdk.apps.stafed.Utils.Helper;
import my.gov.ilpsdk.apps.stafed.data.Constant;
import my.gov.ilpsdk.apps.stafed.model.Bahagian;
import my.gov.ilpsdk.apps.stafed.model.Staff;

public class ActivityStaffDetails extends AppCompatActivity {
    private static final String TAG = "ActivityStaffDetails";

    public static final String EXTRA_OBJCT = "com.app.sample.chatting";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Staff obj) {
        Intent intent = new Intent(activity, ActivityStaffDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Staff staf;
    private List<Bahagian> bahagians;
    private View parent_view;
    private AppDatabase db;
    private Bahagian unit;
    private ImageView call1,call2,whatapps1,whatapps2;
    private String dial_number = null;
    private TextView tvUnit,ext, mobile1, mobile2;
    private LinearLayout ly_mobile2;
    private SharedPreferences config;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        staf = (Staff) getIntent().getSerializableExtra(EXTRA_OBJCT);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(staf.getNama());

        ext = findViewById(R.id.tv_ext);
        tvUnit = findViewById(R.id.tv_unit);
        mobile1 = findViewById(R.id.tv_no_mobile);
        mobile2 = findViewById(R.id.tv_no_mobile2);

        ly_mobile2 = findViewById(R.id.ly_mobile2);

        ImageView imageView = findViewById(R.id.image);
        Picasso.with(getApplicationContext()).load(staf.getAvatar("face")).into(imageView);

        config = getSharedPreferences(Constant.KEY_CONFIG, MODE_PRIVATE);


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constant.KEY_DB_DIREKTORI)
                .allowMainThreadQueries()
                .build();


        unit = db.BahagianDao().getBahagian(staf.getBahagian());

        ((TextView) findViewById(R.id.tv_jawatan)).setText(staf.getJawatan());
        ((TextView) findViewById(R.id.tv_unit)).setText(unit.getNama_bahagian());
        ext.setText(staf.getExt());
        ((TextView) findViewById(R.id.tv_email)).setText("Email : " + staf.getEmail());
        mobile1.setText(staf.getNo_mobile());
        mobile2.setText(staf.getNo_mobile2());

        call1 = findViewById(R.id.img_call);
        call2 = findViewById(R.id.img_call2);
        whatapps1 = findViewById(R.id.img_whatapps);
        whatapps2 = findViewById(R.id.img_whatapps2);





        whatapps1.setOnClickListener(clickListener);
        whatapps2.setOnClickListener(clickListener);

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_number = staf.getNo_mobile();
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    make_call(dial_number);
                } else {
                    ActivityCompat.requestPermissions(ActivityStaffDetails.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
                }
            }
        });

        try{
            if (staf.getNo_mobile2().length() >= 9) {
                ly_mobile2.setVisibility(View.VISIBLE);
                call2 = findViewById(R.id.img_call2);
                call2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dial_number = staf.getNo_mobile2();
                        if (checkPermission(Manifest.permission.CALL_PHONE)) {
                            make_call(dial_number);
                        } else {
                            ActivityCompat.requestPermissions(ActivityStaffDetails.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
                        }
                    }
                });
            }
            else{
                //ly_mobile2.setVisibility(View.GONE);
                call2.setVisibility(View.GONE);
                mobile2.setText("-");
                whatapps2.setVisibility(View.GONE);
                //((ImageView)findViewById(R.id.img_edit_4)).setVisibility(View.GONE);
                ((ImageView)findViewById(R.id.img_edit_4)).setImageResource(R.drawable.ic_add);
            }
        }
        catch (Exception e){
            ly_mobile2.setVisibility(View.GONE);
            call2.setVisibility(View.GONE);
            whatapps2.setVisibility(View.GONE);
            //((ImageView)findViewById(R.id.img_edit_4)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.img_edit_4)).setImageResource(R.drawable.ic_add);
        }

        check_profile();

    }

    private void own_profile(){
            ext.setOnClickListener(clickListener);
            tvUnit.setOnClickListener(clickListener);
            mobile1.setOnClickListener(clickListener);
            mobile2.setOnClickListener(clickListener);

            call1.setVisibility(View.GONE);
            call2.setVisibility(View.GONE);
            whatapps1.setVisibility(View.GONE);
            whatapps2.setVisibility(View.GONE);
    }

    private void other_profile(){
        ((ImageView)findViewById(R.id.img_edit_1)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_edit_2)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_edit_3)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_edit_4)).setVisibility(View.GONE);
    }

    private void check_profile(){
        if(staf.getEmail().equals(config.getString(Constant.KEY_EMAIL,null))){
            own_profile();
        }
        else{
            other_profile();
        }
    }

    private void show_input_dialog(String col, TextView tv){
        new MaterialDialog.Builder(this)
                .title("Edit " + col.toUpperCase())
                .content("Sila taip "+ col.toUpperCase() +" anda")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("Hantar")
                .input(
                        "",
                        tv.getText().toString(),
                        false,
                        (dialog, input) -> save_staf_to_server(input.toString(),col))
                .show();
    }

    private void save_local(String input, String col){
        switch (col){
            case "bahagian":
                db.staffDao().updateBahagian(input,staf.getEmail());
                tvUnit.setText(((Bahagian)db.BahagianDao().getBahagian(input)).getNama_bahagian());
                break;
            case "ext":
                db.staffDao().updateExt(input,staf.getEmail());
                ext.setText(input);
                break;
            case "no_mobile":
                db.staffDao().updateMobile(input,staf.getEmail());
                mobile1.setText(input);
                break;
            case "no_mobile2":
                db.staffDao().updateMobile2(input,staf.getEmail());
                mobile2.setText(input);
                break;
        }

    }

    private void save_staf_to_server(String input,String col){
        final ProgressDialog progressDialog = new ProgressDialog(ActivityStaffDetails.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_UPDATE_STAF)
                .addBodyParameter("email", staf.getEmail())
                .addBodyParameter("column", col)
                .addBodyParameter("value", input)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        //Log.d(TAG, "onResponse: " + response.toString());
                        try {
                            if(response.get("status").equals("success")){
                                save_local(input,col);
                            }
                            else{
                                Helper.showToast(getApplicationContext(),"Failed update");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Helper.error_connection(getApplicationContext(),anError, TAG);
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_whatapps:
                    open_whatsapps("+6" + staf.getNo_mobile());
                    break;
                case R.id.img_whatapps2:
                    open_whatsapps("+6" + staf.getNo_mobile2());
                    break;
                case R.id.tv_ext:
                    show_input_dialog("ext", ext);
                    break;
                case R.id.tv_no_mobile:
                    show_input_dialog("no_mobile", mobile1);
                    break;
                case R.id.tv_no_mobile2:
                    if(mobile2.getText().equals("-")){
                        // TODO: 27/2/2019
                        Helper.showToast(getApplicationContext(),"Coming Soon");
                    }
                    else{
                        show_input_dialog("no_mobile2", mobile2);
                    }

                    break;
                case R.id.tv_unit:
                    bahagians = (List<Bahagian>) db.BahagianDao().getAllBahagian();
                    String[] bahagian_array = new String[bahagians.size()];
                    for(int i = 0; i < bahagians.size(); i++){
                        bahagian_array[i] =  bahagians.get(i).getNama_bahagian();
                    }
                    show_division_list(bahagian_array);
                    break;


            }
        }
    };

    public void show_division_list(String[] bahagian) {
        new MaterialDialog.Builder(this)
                .title("Division List")
                .items(bahagian)
                .itemsCallback((dialog, view, which, text) -> save_staf_to_server(bahagians.get(which).getTag(),"bahagian"))
                .positiveText(android.R.string.cancel)
                .show();
    }

    private void make_call(String phone) {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(getApplicationContext(), "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    make_call(dial_number);
                }
                return;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void open_whatsapps(String phone){
        try {

            PackageManager packageManager = getApplicationContext().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                getApplicationContext().startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_friend_details, menu);
        return true;
    }

}
