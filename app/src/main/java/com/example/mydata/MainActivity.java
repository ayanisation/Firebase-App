package com.example.mydata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email;
    private EditText password;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(this,ProfileActivity.class));
        }
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pass);
        signup = (Button) findViewById(R.id.add);
        signin = (TextView) findViewById(R.id.reg);
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    private void registerUser() {
        String emailid = email.getText().toString().trim();
        String pword = password.getText().toString().trim();
        if (TextUtils.isEmpty(emailid)) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pword)) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(emailid, pword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "You are successfully registered", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Could not register. please try again ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == signup)
            registerUser();
        if (view == signin) {
            finish();
            startActivity(new Intent(this,SigninActivity.class));
        }
    }
}
