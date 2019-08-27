package com.example.mydata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText email2;
    private EditText password2;
    private Button signin2;
    private TextView signup2;
    private ProgressDialog progress;
    private FirebaseAuth authentification;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    SignInButton gsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Configure Google Sign In
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        gsb = (SignInButton)findViewById(R.id.gsignin);
        authentification = FirebaseAuth.getInstance();
        if(authentification.getCurrentUser() != null)
        {
            startActivity(new Intent(this,ProfileActivity.class));
        }
        email2 = (EditText) findViewById(R.id.email2);
        password2 = (EditText) findViewById(R.id.pass2);
        signin2 = (Button) findViewById(R.id.add2);
        signup2 = (TextView) findViewById(R.id.reg2);
        progress = new ProgressDialog(SigninActivity.this);
        signin2.setOnClickListener(this);
        signup2.setOnClickListener(this);
        gsb.setOnClickListener(this);
    }
    private void usersignin() {
        String em = email2.getText().toString().trim();
        String pd = password2.getText().toString().trim();
        if (TextUtils.isEmpty(em)) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pd)) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.setMessage("Authenticating...");
        progress.show();
        authentification.signInWithEmailAndPassword(em,pd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progress.dismiss();
                    Toast.makeText(SigninActivity.this,"You are Signed in",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SigninActivity.this,ProfileActivity.class));
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view == signin2){
            usersignin();
        }
        if(view == signup2)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        if(view == gsb)
        {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authentification.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = authentification.getCurrentUser();
                            Toast.makeText(SigninActivity.this,"You are Signed in",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(SigninActivity.this,ProfileActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SigninActivity.this,"Couldn't Sign in user ",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
