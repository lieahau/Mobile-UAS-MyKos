package id.ac.umn.mykos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class SettingsFragment extends Fragment implements SettingsDialog.OnClickPositiveButton {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private NavController navController;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /* START CALL IMPLEMENTS FUNCTION */
    @Override
    public void sendNumberOfRoom(int input) {
        Log.e("SETTINGS FRAGMENT", "sendNumberOfRoom: found incoming input: " + input);
        /* TODO: WRITE ACTION AFTER INPUT NUMBER OF ROOM SETTING HERE */
        SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_NUMBEROFROOM, input);

    }

    @Override
    public void sendRoomIDValue(String input) {
        Log.e("SETTINGS FRAGMENT", "sendRoomIDValue: found incoming input: " + input);
        String[] roomIDType = getResources().getStringArray(R.array.roomidvalue_array);
        if(input.equalsIgnoreCase(roomIDType[0])){
            Log.e("SETTINGS FRAGMENT", "sendRoomIDValue: room id numeric");
            /* TODO: WRITE ACTION FOR ROOM ID VALUE = NUMERIC HERE */
            SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_ID, SharedPrefHandler.ID_NUMERIC);
        }
        else if(input.equalsIgnoreCase(roomIDType[1])){
            Log.e("SETTINGS FRAGMENT", "sendRoomIDValue: room id alphabetic");
            /* TODO: WRITE ACTION FOR ROOM ID VALUE = ALPHABETIC HERE*/
            SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_ID, SharedPrefHandler.ID_ALPHABET);
        }
    }

    @Override
    public void sendMaximalDueDate(int input) {
        Log.e("SETTINGS FRAGMENT", "sendMaximalDueDate: found incoming input: " + input);
        /* TODO: WRITE ACTION AFTER INPUT MAXIMAL DUE DATE SETTING HERE */
        SharedPrefHandler.SetPref(getActivity(), SharedPrefHandler.KEY_DUEDATE, input);

    }
    /* END CALL IMPLEMENTS FUNCTION */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* START INIT NAVCONTROLLER */
        navController = Navigation.findNavController(view);
        /* END INIT NAVCONTROLLER */

        /* START CREATE NAVIGATION DRAWER */
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationDrawer(view, drawerLayout);
        /* END CREATE NAVIGATION DRAWER */

        /* START CREATE TOOLBAR */
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.opennavdrawer, R.string.closenavdrawer);
        ((MainActivity)getActivity()).setDrawerToolbar(toolbar, toggle, drawerLayout);
        /* END CREATE TOOLBAR */

        /* START ONCLICK EACH BUTTON */
        MaterialButton btn_numberOfRoom = view.findViewById(R.id.NumberOfRoomBtn);
        btn_numberOfRoom.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("layoutID", R.layout.dialog_edittext_number);
            bundle.putString("target", "NumberOfRoom");
            bundle.putString("initial", Integer.toString(SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_NUMBEROFROOM)));
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.setArguments(bundle);
            settingsDialog.setTargetFragment(SettingsFragment.this, 200);
            settingsDialog.show(getFragmentManager(), "SettingsDialog");
        });

        MaterialButton btn_roomIDValue = view.findViewById(R.id.RoomIDValueBtn);
        btn_roomIDValue.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("layoutID", R.layout.dialog_settings_dropdown);
            bundle.putString("target", "RoomIDValue");
            bundle.putInt("initial", SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_ID));
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.setArguments(bundle);
            settingsDialog.setTargetFragment(SettingsFragment.this, 200);
            settingsDialog.show(getFragmentManager(), "SettingsDialog");
        });

        MaterialButton btn_maximalDueDate = view.findViewById(R.id.MaximalDueDateBtn);
        btn_maximalDueDate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("layoutID", R.layout.dialog_edittext_number);
            bundle.putString("target", "MaximalDueDate");
            bundle.putString("initial", Integer.toString(SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_DUEDATE)));
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.setArguments(bundle);
            settingsDialog.setTargetFragment(SettingsFragment.this, 200);
            settingsDialog.show(getFragmentManager(), "SettingsDialog");
        });
        /* END ONCLICK EACH BUTTON */
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
                            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToDashboardFragment());
                            return true;

                        case R.id.nav_overview:
                            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToOverviewFragment());
                            return true;

                        case R.id.nav_settings:
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
