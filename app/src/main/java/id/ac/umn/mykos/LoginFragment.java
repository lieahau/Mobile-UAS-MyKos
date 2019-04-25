package id.ac.umn.mykos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;

    //
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignInButton dashboard = view.findViewById(R.id.btn_to_dashboard);
        Button signOut = view.findViewById(R.id.signOut);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .enableAutoManage(this.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //

        final View viewRef = view;
        dashboard.setOnClickListener(v -> {
            //
            signIn();
            //
            NavDirections dir;
            if(SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_LANDINGPAGE) == SharedPrefHandler.LANDING_DASHBOARD)
                dir = LoginFragmentDirections.actionLoginFragmentToDashboardFragment();
            else
                dir = LoginFragmentDirections.actionLoginFragmentToOverviewFragment();

            Navigation.findNavController(viewRef).navigate(dir);
        });
        signOut.setOnClickListener(v -> {
            signOut();
        });
    }

    // call this func to open google sign in box
    private void signIn() {
        Intent signInIntent = GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001);
    }
    // call this func to sign out google
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }
    public void OnActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9001){
            GoogleSignInResult result = GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // after sign in comes here
    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
            // sign success
            GoogleSignInAccount acct = result.getSignInAccount();

        }
        else{
            // sign failed

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // if not connect
    }
}
