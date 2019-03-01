package my.gov.ilpsdk.apps.stafed.fragment;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import my.gov.ilpsdk.apps.stafed.ActivityMain;
import my.gov.ilpsdk.apps.stafed.ActivityStaffDetails;
import my.gov.ilpsdk.apps.stafed.R;
import my.gov.ilpsdk.apps.stafed.Services.AppDatabase;
import my.gov.ilpsdk.apps.stafed.adapter.AdapterStaff;
import my.gov.ilpsdk.apps.stafed.data.Constant;
import my.gov.ilpsdk.apps.stafed.data.GlobalVariable;
import my.gov.ilpsdk.apps.stafed.model.Bahagian;
import my.gov.ilpsdk.apps.stafed.model.Staff;
import my.gov.ilpsdk.apps.stafed.widget.DividerItemDecoration;


public class StaffFragment extends Fragment {

    private static final String TAG = "StaffFragment";

    private RecyclerView recyclerView;
    public AdapterStaff mAdapter;
    private ProgressBar progressBar;
    private GlobalVariable global;
    private AppDatabase db;
    List<Staff> staff;
    View view;
    private SearchView searchView;
    List<Bahagian> bahagian_popmenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);
        global = (GlobalVariable) getActivity().getApplication();

        setHasOptionsMenu(true);

        db = Room.databaseBuilder(getContext(),AppDatabase.class,Constant.KEY_DB_DIREKTORI)
                .allowMainThreadQueries()
                .build();

		
        // use a linear layout manager


        //get_staf();
        displayStaff();

        return view;
    }

    private void displayStaff(){

        if(!Constant.VAR_CURRENT_GROUP.equals("no")){
            staff = db.staffDao().getAllStaffByGroup(Constant.VAR_CURRENT_GROUP);
        }
        else{
            staff = db.staffDao().getAllStaff();
        }

        //global.setStaff(staff);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        mAdapter = new AdapterStaff(getActivity(), staff);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterStaff.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Staff obj, int position) {
                ActivityStaffDetails.navigate((ActivityMain) getActivity(), view.findViewById(R.id.image), obj);
            }
        });
    }

    public void onRefreshLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_toolbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search Staff...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemTouchHelper.attachToRecyclerView(null);
                //setItemsVisibility(menu, searchItem, false);
            }
        });

        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //itemTouchHelper.attachToRecyclerView(recyclerView);
                //setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void filter_staf_list(int which){
        Constant.VAR_CURRENT_GROUP = bahagian_popmenu.get(which).getTag();
        ((ActivityMain)getActivity()).displayFragment(R.id.nav_home,bahagian_popmenu.get(which).getTag().toUpperCase());
    }

    public void show_division_list(String[] bahagian) {

        new MaterialDialog.Builder(getContext())
                .title("Division List")
                .items(bahagian)
                .itemsCallback((dialog, view, which, text) -> filter_staf_list(which))
                .positiveText(android.R.string.cancel)
                .show();
    }

    private void show_bahagian(){
        bahagian_popmenu = (List<Bahagian>) db.BahagianDao().getAllBahagian();
        String[] bahagian_array = new String[bahagian_popmenu.size()];
        for(int i = 0; i < bahagian_popmenu.size(); i++){
            bahagian_array[i] =  bahagian_popmenu.get(i).getNama_bahagian();
        }
        show_division_list(bahagian_array);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //closeSearch();
                return true;
            case R.id.action_notif: {
                //Snackbar.make(getView(), "Notifications Clicked", Snackbar.LENGTH_SHORT).show();
                show_bahagian();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

}
