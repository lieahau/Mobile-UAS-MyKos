package id.ac.umn.mykos;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;

public class GoogleAuth{
    private GoogleSignInClient mGoogleSignInClient;
    private Fragment fragment;

    public GoogleAuth(Fragment fragment){
        this.fragment = fragment;
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragment.getString(R.string.OAuth_ID_Token))
                .requestEmail()
                .build();

        Context context = fragment.getContext();
        if(context != null){
            Log.d("Debug", "Get Google Sign In Client");
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }else{
            Log.d("Debug", "Context is null");
        }

    }

    // call this func to open google sign in box
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        fragment.startActivityForResult(signInIntent, 9001);
    }

    // call this func to sign out google
    public void signOut(){
        mGoogleSignInClient.signOut();
    }

    public void HandleDataAfterSignIn(String iduser, boolean isNew){
        // Retrieve data from Firebase
        FragmentActivity activity = this.fragment.getActivity();
        if(activity != null){
            Log.d("Debug", "Successfully set Room View Model");
            RoomViewModel roomViewModel = ViewModelProviders.of(activity).get(RoomViewModel.class);
            roomViewModel.getFirebase(iduser);
            if(isNew) roomViewModel.setPlaceholder(10);
        }else{
            Log.d("Debug", "Activity fragment is null");
        }
    }
}
