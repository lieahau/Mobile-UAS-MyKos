package id.ac.umn.mykos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class OverviewFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView overviewList;
    private ListOverviewAdapter overviewAdapter = new ListOverviewAdapter(new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5")));

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.e("TESTING FUNCTION", "OVERVIEW ON CREATE VIEW");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.e("TESTING FUNCTION", "OVERVIEW ON VIEW CREATED");

        /* START CREATE NAVIGATION DRAWER */
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationDrawer(view, drawerLayout);
        /* END CREATE NAVIGATION DRAWER */

        /* START CREATE TOOLBAR */
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.opennavdrawer, R.string.closenavdrawer);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).setDrawerToolbar(toolbar, toggle, drawerLayout);
        /* END CREATE TOOLBAR */

        /* START HANDLING OVERVIEW LIST*/
        overviewList = view.findViewById(R.id.overviewList);
        overviewList.setAdapter(overviewAdapter);
        /* END HANDLING OVERVIEW LIST*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(navigationView);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                                Navigation.findNavController(view).navigate(R.id.dashboardFragment);
                                return true;

                            case R.id.nav_overview:
                                return true;

                            case R.id.nav_settings:
                                Navigation.findNavController(view).navigate(R.id.settingsFragment);
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
