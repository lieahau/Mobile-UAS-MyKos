package id.ac.umn.mykos;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class overviewFragmentDirections {
  private overviewFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionOverviewFragmentToDashboardFragment() {
    return new ActionOnlyNavDirections(R.id.action_overviewFragment_to_dashboardFragment);
  }

  @NonNull
  public static NavDirections actionOverviewFragmentToRoomDetailFragment() {
    return new ActionOnlyNavDirections(R.id.action_overviewFragment_to_roomDetailFragment);
  }
}
