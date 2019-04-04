package id.ac.umn.mykos;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class roomDetailFragmentDirections {
  private roomDetailFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionRoomDetailFragmentToOverviewFragment() {
    return new ActionOnlyNavDirections(R.id.action_roomDetailFragment_to_overviewFragment);
  }

  @NonNull
  public static NavDirections actionRoomDetailFragmentToDashboardFragment() {
    return new ActionOnlyNavDirections(R.id.action_roomDetailFragment_to_dashboardFragment);
  }
}
