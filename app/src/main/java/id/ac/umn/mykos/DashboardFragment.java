package id.ac.umn.mykos;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment implements DashboardDialog.OnClickPositiveButton {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView dashboardList;
    private ListDashboardAdapter dashboardAdapter;
    private NavController navController;
    private RoomViewModel roomViewModel;
    private Menu menu;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void sendSearch(String input) {
        Log.e("DASHBOARD FRAGMENT", "sendSearch: found incoming input: " + input);
        /* TODO: WRITE ACTION AFTER INPUT SEARCH HERE */

    }

    @Override
    public void sendSort(String input) {
        Log.e("DASHBOARD FRAGMENT", "sendSort: found incoming input: " + input);
        if(!input.equalsIgnoreCase("")){
            if(input.equalsIgnoreCase(getResources().getString(R.string.sortbyname))){
                Log.e("DASHBOARD FRAGMENT", "sendSort: sort by name");
                /* TODO: WRITE SORT BY NAME HERE*/
//                MenuItem sortItem = menu.findItem(R.id.btn_sort);
//                Drawable newIcon = sortItem.getIcon();
//                newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.DST_ATOP);
            }
            else if(input.equalsIgnoreCase(getResources().getString(R.string.sortbyid))){
                Log.e("DASHBOARD FRAGMENT", "sendSort: sort by id");
                /* TODO: WRITE SORT BY ID HERE*/
            }
            else if(input.equalsIgnoreCase(getResources().getString(R.string.sortbydeadline))){
                Log.e("DASHBOARD FRAGMENT", "sendSort: sort by deadline");
                /* TODO: WRITE SORT BY DEADLINE HERE*/
            }
        }
        else{
            Log.e("DASHBOARD FRAGMENT", "sendSort: no sort selected");
            /* TODO: WRITE UNSORT DATA HERE (OPTIONAL) */
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /* START INIT ROOM VIEW MODEL */
        // Use activity context to make it shareable between fragment
        roomViewModel = ViewModelProviders.of(getActivity()).get(RoomViewModel.class);
        /* END INIT ROOM VIEW MODEL */
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.e("TESTING FUNCTION", "DASHBOARD ON CREATE VIEW");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.e("TESTING FUNCTION", "DASHBOARD ON VIEW CREATED");
        /* START INIT NAVCONTROLLER */
        navController = Navigation.findNavController(view);
        /* END INIT NAVCONTROLLER */

        /* START INIT SHARED ELEMENT TRANSITION */
        setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.shared_content_transition));
        /* END INIT SHARED ELEMENT TRANSITION */

        /* START CREATE NAVIGATION DRAWER */
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationDrawer(view, drawerLayout);
        /* END CREATE NAVIGATION DRAWER */

        /* START CREATE TOOLBAR */
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.opennavdrawer, R.string.closenavdrawer);
        ((MainActivity)getActivity()).setDrawerToolbar(toolbar, toggle, drawerLayout);
        /* END CREATE TOOLBAR */

        /* START HANDLING DASHBOARD LIST*/
        dashboardAdapter = new ListDashboardAdapter(new ArrayList<String>(), navController);
        roomViewModel.GetData().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> newData) {
                dashboardAdapter.SetData(newData);
            }
        });

        dashboardList = view.findViewById(R.id.dashboardList);
        dashboardList.setAdapter(dashboardAdapter);
        /* END HANDLING DASHBOARD LIST*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle bundle;
        DashboardDialog dashboardDialog;
        switch(item.getItemId()){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(navigationView);

                return true;

            case R.id.btn_search:
                bundle = new Bundle();
                bundle.putInt("layoutID", R.layout.dialog_dashboard_search);
                bundle.putString("target", "Search");
                dashboardDialog = new DashboardDialog();
                dashboardDialog.setArguments(bundle);
                dashboardDialog.setTargetFragment(DashboardFragment.this, 200);
                dashboardDialog.show(getFragmentManager(), "DashboardDialog");

                return true;

            case R.id.btn_sort:
                bundle = new Bundle();
                bundle.putInt("layoutID", R.layout.dialog_dashboard_sort);
                bundle.putString("target", "Sort");
                dashboardDialog = new DashboardDialog();
                dashboardDialog.setArguments(bundle);
                dashboardDialog.setTargetFragment(DashboardFragment.this, 200);
                dashboardDialog.show(getFragmentManager(), "DashboardDialog");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);
        this.menu = menu;
    }

    private void navigationDrawer(final View view, final DrawerLayout drawerLayout){
        navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // close drawer when item is tapped
                        drawerLayout.closeDrawer(GravityCompat.START);

                        switch(item.getItemId()){
                            case R.id.nav_dashboard:
                                return true;

                            case R.id.nav_overview:
                                navController.navigate(DashboardFragmentDirections.actionDashboardFragmentToOverviewFragment());
                                return true;

                            case R.id.nav_settings:
                                navController.navigate(DashboardFragmentDirections.actionDashboardFragmentToSettingsFragment());
                                return true;

                            default:
                                return true;
                        }
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        /* START REMOVE TOOLBAR */
        ((MainActivity)getActivity()).removeDrawerToolbar(toggle, drawerLayout);
        /* END REMOVE TOOLBAR */

        super.onDestroyView();
    }
}
