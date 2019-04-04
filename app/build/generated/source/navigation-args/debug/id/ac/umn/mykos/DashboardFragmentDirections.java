package id.ac.umn.mykos;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class DashboardFragmentDirections {
  private DashboardFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionDashboardFragmentToOverviewFragment() {
    return new ActionOnlyNavDirections(R.id.action_dashboardFragment_to_overviewFragment);
  }

  @NonNull
  public static NavDirections actionDashboardFragmentToRoomDetailFragment() {
    return new ActionOnlyNavDirections(R.id.action_dashboardFragment_to_roomDetailFragment);
  }
}
