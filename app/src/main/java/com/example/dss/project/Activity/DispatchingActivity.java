package com.example.dss.project.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dss.R;
import com.example.dss.project.Fragment.Account;
import com.example.dss.project.Fragment.ViewHistory;
import com.example.dss.project.Models.Dispatch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DispatchingActivity extends AppCompatActivity {

    LoadingDialog loadingDialog = new LoadingDialog(DispatchingActivity.this);

    boolean is1stNavigationOpen = true, is2ndNavigationOpen = false, is3rdNavigationOpen = false, is4thNavigationOpen = false;

    ImageButton reload, notif;
    ListView listViewDispatch;
    TextView id, time, place, type, salary;
    List<Dispatch> listItemDispatch = new ArrayList<>();
    CustomAdapter customAdapter;
    SearchView filTer;

    LinearLayout searchAndView;

    Fragment fragment = new Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //Set selected Item
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        //Action Bar and Set to center
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.align_center_action_bar, null);
        actionBar.setCustomView(cView);

        showNotif();

        searchAndView = findViewById(R.id.search_and_view);
        listViewDispatch = findViewById(R.id.list_view_dispatch);
        reload = cView.findViewById(R.id.btn_reload);
        notif = cView.findViewById(R.id.btn_notification);
        filTer = findViewById(R.id.search_filter_dispatch);

        //Set ListView to the Background
        //searchAndView.setVisibility(View.VISIBLE);
        listViewDispatch = findViewById(R.id.list_view_dispatch);
        customAdapter = new CustomAdapter(listItemDispatch);
        listViewDispatch.setAdapter(customAdapter);

        filTer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                System.out.println("Filled");
                return true;
            }
        });

        listViewDispatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Chose then Back to Map
                filTer.setQuery("", false);
                filTer.clearFocus();
                Toast.makeText(DispatchingActivity.this, "Thông tin chi tiết chuyến đi", Toast.LENGTH_SHORT).show();
                searchAndView.setVisibility(LinearLayout.GONE);
            }
        });

        //String data for testing
        String listId[] = {"A14527", "B33641", "A97845", "A51846", "B51846", "C45678", "C35846"};
        String listTime[] = {"11:09", "11:09", "11:09", "11:09", "11:09", "11:09", "11:09"};
        String listPlace[] = {"Cầu Giấy", "Hoa Bằng", "Đống Đa", "Hà Đông", "Thanh Bình", "Kim Giang", "Tố Hữu"};
        String listType[] = {"Giấy A4", "Sắt", "Vật liệu xây dựng", "Đồ gia dụng", "Máy móc", "Hoa quả", "Nguyên vật liệu"};
        long listSalary[] = {846l, 355l, 566l, 68453l, 46l, 456l, 846l};
        for (int i = 0; i < listId.length; i++){
            Dispatch dispatch = new Dispatch(listId[i] + i, listTime[i], listPlace[i], listType[i], listSalary[i]);
            listItemDispatch.add(dispatch);
        }

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DispatchingActivity.this, "RELOAD", Toast.LENGTH_SHORT).show();
            }
        });

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DispatchingActivity.this, "NOTIFICATION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Notification about new dispatching
    private void showNotif() {
        StringBuilder sb = new StringBuilder();
        sb.append("Thông tin đơn điều vận: ");
        sb.append("\n");
        sb.append("Thời gian: ");
        sb.append("\n");
        sb.append("Địa điểm: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(DispatchingActivity.this);
        builder.setTitle("Đơn điều vận mới");
        builder.setMessage(sb.toString());
        builder.setCancelable(false);

        builder.setPositiveButton("Nhận đơn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dissmissDialog();
                    }
                }, 10000);
                startActivity(new Intent(DispatchingActivity.this, MapsActivity.class));
            }
        });

        builder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //For Draw
                case R.id.nav_home:
                    Toast.makeText(DispatchingActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DispatchingActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.nav_dispatch:
                    fragment = getSupportFragmentManager().findFragmentByTag("Acoount");
                    if (fragment != null){
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    break;
                case R.id.nav_salary:
                    Toast.makeText(DispatchingActivity.this, "THU NHẬP", Toast.LENGTH_SHORT).show();
                    fragment = getSupportFragmentManager().findFragmentByTag("Acoount");
                    if (fragment != null){
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    break;
                case R.id.nav_account:
                    Toast.makeText(DispatchingActivity.this, "CÁ NHÂN", Toast.LENGTH_SHORT).show();
                    Fragment accountFragment = new Account();
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_container, accountFragment, "Acoount").commit();
                    break;
            }
            return true;
        }
    };

    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<Dispatch> itemsModelList;
        private List<Dispatch> itemsModelListFiltered;

        public CustomAdapter(List<Dispatch> itemsModelList) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered = itemsModelList;
        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //Get View for Adapter
        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_dieuvan, null);
            id =  view.findViewById(R.id.id);
            time = view.findViewById(R.id.thoi_gian);
            place = view.findViewById(R.id.dia_diem);
            type = view.findViewById(R.id.loai_hang);
            salary = view.findViewById(R.id.thu_nhap);

            //set ListView
            //imageView.setRotation(itemsModelListFiltered.get(i).getDirection());
            id.setText("ID: " + itemsModelListFiltered.get(i).getId());
            time.setText("Time: " + itemsModelListFiltered.get(i).getTime());
            place.setText("Place: " + itemsModelListFiltered.get(i).getPlace());
            type.setText("Type: " + itemsModelListFiltered.get(i).getType());
            salary.setText("Salary: " + itemsModelListFiltered.get(i).getSalary());

            return view;
        }


        //Set on Text Change
        @Override
        public Filter getFilter() {
            final Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelList.size();
                        filterResults.values = itemsModelList;
                    } else {
                        String searchString = constraint.toString().toUpperCase();

                        List<Dispatch> resultData = new ArrayList<>();

                        //Car change
                        for (Dispatch dispatch : itemsModelList) {
                            if (dispatch.getId().contains(searchString)) {
                                resultData.add(dispatch);
                            }

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    itemsModelListFiltered = (List<Dispatch>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }

    /*@Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
            fm.popBackStack();
        } else {
            Toast.makeText(this, "Nowhere to going back", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }*/
}
