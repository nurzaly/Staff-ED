package my.gov.ilpsdk.apps.stafed;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import my.gov.ilpsdk.apps.stafed.Services.AppDatabase;
import my.gov.ilpsdk.apps.stafed.Utils.Helper;
import my.gov.ilpsdk.apps.stafed.data.Constant;
import my.gov.ilpsdk.apps.stafed.data.GlobalVariable;
import my.gov.ilpsdk.apps.stafed.data.Tools;
import my.gov.ilpsdk.apps.stafed.fragment.GroupsFragment;
import my.gov.ilpsdk.apps.stafed.fragment.StaffFragment;
import my.gov.ilpsdk.apps.stafed.widget.CircleTransform;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private View parent_view,nview;
    private Toolbar searchToolbar;
    private AppBarLayout appBarLayout;


    private GlobalVariable global;
    private SharedPreferences config;
    private boolean isSearch = false;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content);
        global = (GlobalVariable) getApplication();
        config = getSharedPreferences(Constant.KEY_CONFIG, 0);

        initComponent();
        prepareActionBar(toolbar);
        //initDrawerMenu();


        SharedPreferences myconfig = getSharedPreferences(Constant.KEY_CONFIG, 0);
        displayFragment(R.id.nav_home, getString(R.string.title_nav_home));

        String lang = Locale.getDefault().getLanguage();



        // for system bar in lollipop
        try {
            Tools.systemBarLolipop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (!isSearch) {
            initDrawerMenu();
        }
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                //updateSavedCounter(navigationView, R.id.nav_saved, global.getSaved().size());
                super.onDrawerOpened(drawerView);
            }
        };
        //Picasso.with(getApplicationContext()).load(R.drawable.ic_ch_business).into(avatar);
        //avatar.setImageResource(R.drawable.ic_ch_business);
        //Log.i("TEST",avatar.toString());
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        nview = navigationView.getHeaderView(0);
        //hide_menu();
        init_user_info(nview);

        db = Room.databaseBuilder(this, AppDatabase.class,Constant.KEY_DB_DIREKTORI)
                .allowMainThreadQueries()
                .build();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                Constant.VAR_CURRENT_GROUP = "no";
                displayFragment(menuItem.getItemId(), menuItem.getTitle().toString());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    public void language_reset(){
        //initToolbar();
        initDrawerMenu();
    }

//    private void hide_menu(){
//        Menu nav_menu = navigationView.getMenu();
//        if(myconfig.getString(Constant.ROLE,"").equals("teacher")){
//            nav_menu.findItem(R.id.nav_class_list).setVisible(true);
//            nav_menu.findItem(R.id.nav_teacher_list).setVisible(false);
//        }
//        else{
//            nav_menu.findItem(R.id.nav_class_list).setVisible(false);
//            nav_menu.findItem(R.id.nav_teacher_list).setVisible(true);
//        }
//    }
private String get_avatar(String email){
    return Constant.URL_AVATAR  + "thumbs/" + email.split("@")[0] +".jpg";
}

    private void init_user_info(View nview) {
        final ImageView avatar = (ImageView) nview.findViewById(R.id.avatar);
        final TextView fullname = (TextView) nview.findViewById(R.id.tv_username);
        final TextView email = (TextView) nview.findViewById(R.id.tv_email);

        Picasso.with(getApplicationContext()).load(get_avatar(config.getString("email",null))).resize(100, 100).transform(new CircleTransform()).into(avatar);
        fullname.setText(config.getString("nama",null));
        email.setText(config.getString("email",null));

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ActivityStaffDetails.navigate(ActivityMain.this, avatar, db.staffDao().getStaf(config.getString(Constant.KEY_EMAIL,null)));
            }
        });
    }

    public void displayHomeFragment(){
        displayFragment(R.id.nav_home, getString(R.string.title_nav_home));
    }

    public void displayFragment(int id, String title) {
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                fragment = new StaffFragment();
                break;
            case R.id.nav_bahagian:
                fragment = new GroupsFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_change_password:
                Intent intent = new Intent(this, ActivityChangePassword.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                new MaterialDialog.Builder(this)
                        .title("About")
                        .content(R.string.about, true)
                        .positiveText(R.string.positive)
                        .show();
                break;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Staff-ED");
                    String shareMessage= "\nILP Sandakan Staff E-Directory\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose One"));
                } catch(Exception e) {
                    Helper.showToast(getApplicationContext(),"Error Share");
                }
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }

    private void logout(){
        SharedPreferences.Editor editor = config.edit();
        editor.remove(Constant.KEY_LOGIN);
        editor.remove(Constant.KEY_EMAIL);
        editor.remove(Constant.KEY_NAMA);
        //editor.remove(Const.KEY_INSTALL);
        boolean success = editor.commit();
        show_login_activity();
    }

    public void show_fragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.commit();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    private void show_login_activity() {
        Intent intent = new Intent(this,ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            prepareActionBar(toolbar);
            searchToolbar.setVisibility(View.GONE);
            supportInvalidateOptionsMenu();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(isSearch ? R.menu.menu_search_toolbar : R.menu.menu_main, menu);
//        if (isSearch) {
//            //Toast.makeText(getApplicationContext(), "Search " + isSearch, Toast.LENGTH_SHORT).show();
//            final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
//            search.setIconified(false);
//            search.setQueryHint("Search groups...");
//
//            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String s) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String s) {
//
//                    return true;
//                }
//            });
//            search.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//                    closeSearch();
//                    return true;
//                }
//            });
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_search: {
//                isSearch = true;
//                searchToolbar.setVisibility(View.VISIBLE);
//                prepareActionBar(searchToolbar);
//                supportInvalidateOptionsMenu();
//                return true;
//            }
//            case android.R.id.home:
//                closeSearch();
//                return true;
//            case R.id.action_notif: {
//                Snackbar.make(parent_view, "Notifications Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }



    @Override
    protected void onResume() {
        //updateSavedCounter(navigationView, R.id.nav_saved, global.getSaved().size());
        super.onResume();
//        Log.d(TAG, "onResume: ");
//        if(Constant.language_change == true){
//            refresh();
//        }
//        Constant.language_change = false;
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(!Constant.VAR_CURRENT_GROUP.equals("no")){
            Constant.VAR_CURRENT_GROUP = "no";
            displayHomeFragment();
        }
        else{
            doExitApp();
        }

        //super.onBackPressed();
    }
}
