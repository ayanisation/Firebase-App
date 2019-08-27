package com.example.mydata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class download extends AppCompatActivity {
    private RecyclerView rview;
    private imgAdapter ima;
    private DatabaseReference datref;
    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        rview =  findViewById(R.id.my_recycler_view);
        rview.setHasFixedSize(true);
        rview.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();
        datref = FirebaseDatabase.getInstance().getReference("Images");
        datref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for(DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Upload upl = postsnap.getValue(Upload.class);
                    uploads.add(upl);
                }
                    ima = new imgAdapter(download.this, uploads);
                    rview.setAdapter(ima);

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                Toast.makeText(download.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
