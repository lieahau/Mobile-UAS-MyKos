package id.ac.umn.mykos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment{
    private GoogleAuth auth;
    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = new GoogleAuth(this);
        this.navController = Navigation.findNavController(view);

        /* START INIT GOOGLE AUTH CLASS */
        FirebaseUser user = MainActivity.GetFirebaseAuth().getCurrentUser();
        // check if user already login or not
        // if already login then proceed without login

        if(user != null){
            // delay navigation to show splash screen
            // execute runable in main thread
            // not blocking
            Handler delay = new Handler(Looper.getMainLooper());
            delay.postDelayed(() -> {
                auth.HandleDataAfterSignIn(user.getUid(), false);
                Navigate();
            }, 5);
        }else{
            auth.signIn();
        }

        // tap screen to login again
        view.setOnClickListener(v -> {
            auth.signIn();
        });
        /* END INIT GOOGLE AUTH CLASS */
    }

    private void Navigate(){
        NavDirections dir;
        if(SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_LANDINGPAGE) == SharedPrefHandler.LANDING_DASHBOARD)
            dir = LoginFragmentDirections.actionLoginFragmentToDashboardFragment();
        else
            dir = LoginFragmentDirections.actionLoginFragmentToOverviewFragment();

        navController.navigate(dir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug", "On Activity Result, request code: "+requestCode);

        if(requestCode == 9001){
            Context context = getActivity();
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    if(task.isSuccessful()){
                        try{
                            Log.d("Debug", "try to get sign in account");
                            GoogleSignInAccount googleAccount = task.getResult(ApiException.class);
                            if(googleAccount == null) throw new NullPointerException();
                            Log.d("Debug", "Get Sign In account");

                            AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
                            MainActivity.GetFirebaseAuth().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // if successfully login with google credential
                                    if(task.isSuccessful()){
                                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                                        Log.d("Debug", "Success sign in with credential, "+ (isNew ? "new user" : "old user"));
                                        FirebaseUser user = MainActivity.GetFirebaseAuth().getCurrentUser();
                                        Toast.makeText(getContext(), user.getEmail() + " sign in successful", Toast.LENGTH_LONG).show();
                                        auth.HandleDataAfterSignIn(user.getUid(),isNew);
                                        Navigate();
                                    }else{
                                        Toast.makeText(getContext(), "Tap screen to sign in", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        catch (ApiException e){
                            // sign failed (google login box closed)
                            Log.d("Debug", "Fail to sign in");
                            Toast.makeText(context, "Tap screen to sign in", Toast.LENGTH_LONG).show();
                        }
                        catch (NullPointerException e){
                            Log.d("Debug", "Fail to sign in");
                            Toast.makeText(context, "Tap screen to sign in", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Log.d("Debug", "Fail to sign in, Exception: " + task.getException());
                        Toast.makeText(context, "Tap screen to sign in", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
