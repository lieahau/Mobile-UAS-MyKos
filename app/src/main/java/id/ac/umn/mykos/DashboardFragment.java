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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DashboardFragment extends Fragment implements DashboardDialog.OnClickPositiveButton {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView dashboardList;
    private ListDashboardAdapter dashboardAdapter;
    private NavController navController;
    private RoomViewModel roomViewModel;
    private MenuItem sortItem, searchItem;
    private String initialSearch;
    private int initialSort;
    private static final String BUNDLE_INITIAL_SEARCH = "BUNDLE_INITIAL_SEARCH";
    private static final String BUNDLE_INITIAL_SORT = "BUNDLE_INITIAL_SORT";

    public DashboardFragment() {
        // Required empty public constructor
    }

    /* START CALL IMPLEMENTS FUNCTION */
    @Override
    public void sendSearch(String input) {
        Log.e("DASHBOARD FRAGMENT", "sendSearch: found incoming input: " + input);
        initialSearch = input;
        SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_SEARCH, input);

        if(!input.equalsIgnoreCase("")) { // if input NOT EMPTY
            /* START CHANGE ICON COLOR TO GREEN */
            Drawable newIcon = searchItem.getIcon();
            newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.MULTIPLY);
            /* END CHANGE ICON COLOR TO GREEN */

            /* WRITE ACTION AFTER INPUT SEARCH */
            roomViewModel.sortRoom(getActivity(), input, null);
        }
        else{ // empty string search
            /* START CHANGE ICON COLOR TO BLACK */
            Drawable newIcon = searchItem.getIcon();
            newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.MULTIPLY);
            /* END CHANGE ICON COLOR TO BLACK */
            roomViewModel.sortRoom(getActivity(), "", null);
        }
    }

    @Override
    public void sendSort(String input) {
        if(!input.equalsIgnoreCase("")){
            Log.e("DASHBOARD FRAGMENT", "sendSort: found incoming input: " + input);
            if(input.equalsIgnoreCase(getResources().getString(R.string.sortbyname))){ // SORT BY NAME
                initialSort = R.id.radio_sort_name;

                /* START CHANGE ICON COLOR TO GREEN */
                Drawable newIcon = sortItem.getIcon();
                newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.MULTIPLY);
                /* END CHANGE ICON COLOR TO GREEN */

                /* WRITE SORT BY NAME */
                roomViewModel.sortRoom(getActivity(), null, input);
            }
            else if(input.equalsIgnoreCase(getResources().getString(R.string.sortbyid))){ // SORT BY ID
                initialSort = R.id.radio_sort_id;

                /* START CHANGE ICON COLOR TO BLACK */
                Drawable newIcon = sortItem.getIcon();
                newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.MULTIPLY);
                /* END CHANGE ICON COLOR TO BLACK */

                /* WRITE SORT BY ID */
                roomViewModel.sortRoom(getActivity(), null, input);
            }
            else if(input.equalsIgnoreCase(getResources().getString(R.string.sortbydeadline))){ // SORT BY DEADLINE
                initialSort = R.id.radio_sort_deadline;

                /* START CHANGE ICON COLOR TO GREEN */
                Drawable newIcon = sortItem.getIcon();
                newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.MULTIPLY);
                /* END CHANGE ICON COLOR TO GREEN */

                /* WRITE SORT BY DEADLINE */
                roomViewModel.sortRoom(getActivity(), null, input);
            }
            SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_SORT, initialSort);
        }
    }
    /* END CALL IMPLEMENTS FUNCTION */

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        dashboardAdapter = new ListDashboardAdapter(new ArrayList<Room>(), navController);
        roomViewModel.GetDashboardData().observe(this, newData -> {
            dashboardAdapter.SetData(newData);
            dashboardAdapter.notifyDataSetChanged();
            Log.d("Debug", "SetData");
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
                bundle.putString("initial", initialSearch);
                dashboardDialog = new DashboardDialog();
                dashboardDialog.setArguments(bundle);
                dashboardDialog.setTargetFragment(DashboardFragment.this, 200);
                dashboardDialog.show(getFragmentManager(), "DashboardDialog");

                return true;

            case R.id.btn_sort:
                bundle = new Bundle();
                bundle.putInt("layoutID", R.layout.dialog_dashboard_sort);
                bundle.putString("target", "Sort");
                bundle.putInt("initial", initialSort);
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

        sortItem = menu.findItem(R.id.btn_sort);
        searchItem = menu.findItem(R.id.btn_search);

        initialSort = SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_SORT);
        Drawable newIcon = sortItem.getIcon();
        if(initialSort == R.id.radio_sort_id) {
            newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.MULTIPLY);
        }
        else{
            newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.MULTIPLY);
        }

        initialSearch = SharedPrefHandler.GetPrefString(getActivity(), SharedPrefHandler.KEY_SEARCH);
        Drawable newIcon2 = searchItem.getIcon();
        if(initialSearch.equalsIgnoreCase("")) {
            newIcon2.mutate().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.MULTIPLY);
        }
        else{
            newIcon2.mutate().setColorFilter(getResources().getColor(R.color.colorLightGreen), PorterDuff.Mode.MULTIPLY);
        }
    }

    private void navigationDrawer(final View view, final DrawerLayout drawerLayout){
        navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                item -> {
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
