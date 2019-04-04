package id.ac.umn.mykos;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class LoginFragmentDirections {
  private LoginFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionLoginFragmentToDashboardFragment() {
    return new ActionOnlyNavDirections(R.id.action_loginFragment_to_dashboardFragment);
  }
}
