package com.example.mydata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth fba;
    private Button logout;
    private TextView message;
    private DatabaseReference dr;
    private EditText phone;
    private EditText address;
    private Button savedata;
    private TextView up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fba = FirebaseAuth.getInstance();
        String Uid = fba.getCurrentUser().getUid();
        dr = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child(Uid);
        if(fba.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,SigninActivity.class));
        }
        address = (EditText)findViewById(R.id.address);
        phone = (EditText)findViewById(R.id.pnum);
        savedata = (Button)findViewById(R.id.save);
        message = (TextView)findViewById(R.id.profile);
        logout = (Button)findViewById(R.id.logout);
        FirebaseUser user = fba.getCurrentUser();
        message.setText("Welcome "+user.getEmail());
        up = (TextView)findViewById(R.id.upimg);
        up.setOnClickListener(this);
        logout.setOnClickListener(this);
        savedata.setOnClickListener(this);

    }

    void saveInfo()
    {
        String pho = phone.getText().toString();
        String adr = address.getText().toString();
        if(TextUtils.isEmpty(pho))
        {
            Toast.makeText(this,"Please enter Phone no.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(adr))
        {
            Toast.makeText(this,"Please enter address",Toast.LENGTH_SHORT).show();
            return;
        }
        String id = dr.push().getKey();

        Map newsave = new HashMap();
        newsave.put("Phone",pho);
        newsave.put("Address",adr);
        dr.setValue(newsave);
//        UserInfo uinfo = new UserInfo(pho,adr);
//        FirebaseUser use = fba.getCurrentUser();
//        assert use != null;
//        dr.child("Users").child(use.getUid()).setValue(uinfo);
       Toast.makeText(this,"Info. Saved",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view == logout) {
            fba.signOut();
            finish();
            startActivity(new Intent(this, SigninActivity.class));
        }
        if(view == savedata)
        {
            saveInfo();
        }
        if(view == up)
        {
            finish();
            startActivity(new Intent(this, Storage.class));
        }
    }
}
