package id.ac.umn.mykos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;

public class LoginFragment extends Fragment{
    private GoogleAuth auth;
    private View view;

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
        this.view = view;

        SignInButton dashboard = view.findViewById(R.id.btn_to_dashboard);
        Button signOut = view.findViewById(R.id.signOut);

        /* START INIT GOOGLE AUTH CLASS */
        FirebaseUser user = MainActivity.GetFirebaseAuth().getCurrentUser();
        // check if user already login or not
        // if already login then proceed without login

        if(user != null){
            auth.HandleDataAfterSignIn(user.getUid());
            Navigate();
        }else{
            auth.signIn();
        }
        /* END INIT GOOGLE AUTH CLASS */
        dashboard.setOnClickListener(v -> {
            auth.signIn();
        });
    }

    private void Navigate(){
        NavDirections dir;
        if(SharedPrefHandler.GetPrefInt(getActivity(), SharedPrefHandler.KEY_LANDINGPAGE) == SharedPrefHandler.LANDING_DASHBOARD)
            dir = LoginFragmentDirections.actionLoginFragmentToDashboardFragment();
        else
            dir = LoginFragmentDirections.actionLoginFragmentToOverviewFragment();

        Navigation.findNavController(view).navigate(dir);
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
                                        Log.d("Debug", "Success sign in with credential");
                                        FirebaseUser user = MainActivity.GetFirebaseAuth().getCurrentUser();
                                        Toast.makeText(getContext(), user.getEmail() + " sign in successful", Toast.LENGTH_LONG).show();
                                        auth.HandleDataAfterSignIn(user.getUid());
                                        Navigate();
                                    }else{
                                        Toast.makeText(getContext(), "Fail to sign in", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        catch (ApiException e){
                            // sign failed (google login box closed)
                            Log.d("Debug", "Fail to sign in");
                            Toast.makeText(context, "Fail to sign in", Toast.LENGTH_LONG).show();
                        }
                        catch (NullPointerException e){
                            Log.d("Debug", "Fail to sign in");
                            Toast.makeText(context, "Fail to sign in", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Log.d("Debug", "Fail to sign in, Exception: " + task.getException());
                        Toast.makeText(context, "Fail to sign in", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
