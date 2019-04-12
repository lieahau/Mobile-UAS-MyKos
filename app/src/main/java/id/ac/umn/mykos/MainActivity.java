package id.ac.umn.mykos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private NavHostFragment navHost;
    private RoomViewModel roomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate dummy data
        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        roomViewModel.SetData(new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5")));

        navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    public void setActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
    }

    public void setHomeButton(int icon){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(icon);
    }

    public void setDrawerToolbar(Toolbar toolbar, ActionBarDrawerToggle toggle, DrawerLayout drawerLayout){
        setActionBar(toolbar);
        setHomeButton(R.drawable.ic_menu);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void removeDrawerToolbar(ActionBarDrawerToggle toggle, DrawerLayout drawerLayout){
        drawerLayout.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        // navigate up if back button is pressed
        // close drawer if open
        // will not navigate up if drawer open
        // only in fragment other than login
        String curFragmentTag = navHost.getNavController().getCurrentDestination().getLabel().toString();
        if(curFragmentTag.compareTo(getString(R.string.loginFragment)) != 0){
            Log.d("Debug", "Current Fragment Tag: "+curFragmentTag);
            View view = navHost.getView();
            if(view != null){
                DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
                if(drawerLayout == null){
                    // Call navigate up for default navigation back button action
                    // this is for Room detail
                    Log.d("Debug", "Room pressback");
                    navHost.getNavController().navigateUp();
                    return;
                }

                // this for UI other than Room Detail
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Log.d("Debug", "Close Drawer");
                }
                // Call default onBackPressed for Dashboard and Overview
                else if(curFragmentTag.compareTo(getString(R.string.dashboardFragment)) == 0
                        || curFragmentTag.compareTo(getString(R.string.overviewFragment)) == 0){
                    // save last UI state on SharedPre

                    // exit
                    Log.d("Debug", "Exit");
                    super.onBackPressed();
                }else{
                    Log.d("Debug", "Press back");
                    navHost.getNavController().navigateUp();
                }

            }
        }
    }
}
