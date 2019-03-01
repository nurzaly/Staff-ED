package my.gov.ilpsdk.apps.stafed.fragment;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import my.gov.ilpsdk.apps.stafed.ActivityMain;
import my.gov.ilpsdk.apps.stafed.R;
import my.gov.ilpsdk.apps.stafed.Services.AppDatabase;
import my.gov.ilpsdk.apps.stafed.adapter.GroupsGridAdapter;
import my.gov.ilpsdk.apps.stafed.data.Constant;
import my.gov.ilpsdk.apps.stafed.data.GlobalVariable;
import my.gov.ilpsdk.apps.stafed.data.Tools;
import my.gov.ilpsdk.apps.stafed.model.Bahagian;

public class GroupsFragment extends Fragment {

    RecyclerView recyclerView;
    public GroupsGridAdapter mAdapter;
    private ProgressBar progressBar;
    private View view;
    private LinearLayout lyt_not_found;
    private List<Bahagian> bahagian;
    private GlobalVariable global;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);
        lyt_not_found   = (LinearLayout) view.findViewById(R.id.lyt_not_found);
        global = new GlobalVariable();
        AppDatabase db = Room.databaseBuilder(view.getContext(), AppDatabase.class,Constant.KEY_DB_DIREKTORI)
                .allowMainThreadQueries()
                .build();
        bahagian = db.BahagianDao().getAllBahagian();

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (bahagian.size() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        }else{
            lyt_not_found.setVisibility(View.GONE);
        }

        // specify an adapter (see also next example)
        mAdapter = new GroupsGridAdapter( getActivity(), bahagian, db);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GroupsGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Bahagian obj, int position) {
                //ActivityGroupDetails.navigate((ActivityMain2) getActivity(), v.findViewById(R.id.lyt_parent), obj);
                Constant.VAR_CURRENT_GROUP = obj.getTag();
                ((ActivityMain)getActivity()).displayFragment(R.id.nav_home,obj.getTag().toUpperCase());
            }
        });

        return view;
    }



    public void onRefreshLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

}
