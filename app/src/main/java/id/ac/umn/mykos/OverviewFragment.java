package id.ac.umn.mykos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class OverviewFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView overviewList;
    private ListOverviewAdapter overviewAdapter;
    private NavController navController;
    private RoomViewModel roomViewModel;

    public OverviewFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
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

        /* START HANDLING OVERVIEW LIST*/
        overviewAdapter = new ListOverviewAdapter(new ArrayList<Room>(), navController, getActivity());
        roomViewModel.GetData().observe(this, new Observer<ArrayList<Room>>() {
            @Override
            public void onChanged(ArrayList<Room> newData) {
                overviewAdapter.SetData(newData);
            }
        });

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
                item -> {
                    // close drawer when item is tapped
                    drawerLayout.closeDrawer(GravityCompat.START);

                    switch(item.getItemId()){
                        case R.id.nav_dashboard:
                            navController.navigate(OverviewFragmentDirections.actionOverviewFragmentToDashboardFragment());
                            return true;

                        case R.id.nav_overview:
                            return true;

                        case R.id.nav_settings:
                            navController.navigate(OverviewFragmentDirections.actionOverviewFragmentToSettingsFragment());
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
