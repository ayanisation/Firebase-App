package com.example.mydata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class Storage extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private Button cimage;
    private Button lout;
    private Button uimage;
    private ImageView image;
    private Uri filepath;
    private StorageReference storageRef;
    private DatabaseReference dr;
    private TextView seeup;
    private EditText iname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        storageRef = FirebaseStorage.getInstance().getReference("Images");
        dr = FirebaseDatabase.getInstance().getReference().child("Images");
        cimage = (Button) findViewById(R.id.cimg);
        uimage = (Button) findViewById(R.id.uimg);
        image = (ImageView) findViewById(R.id.img);
        lout = (Button) findViewById(R.id.out);
        seeup = (TextView) findViewById(R.id.showuploads);
        iname = (EditText)findViewById(R.id.imgname);
        cimage.setOnClickListener(this);
        uimage.setOnClickListener(this);
        lout.setOnClickListener(this);
        seeup.setOnClickListener(this);
    }

    private void fileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            Picasso.with(this).load(filepath).into(image);

//            try {
//                Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
//                image.setImageBitmap(bit);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


    }
    private String getExt(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mmap = MimeTypeMap.getSingleton();
        return mmap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadfile() {
        if (filepath == null)
            Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
        else {
            final ProgressDialog prog = new ProgressDialog(this);
            prog.setTitle("Uploading...");
            prog.show();
            final StorageReference riversRef = storageRef.child(iname.getText().toString().trim()+"."+getExt(filepath));
            riversRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            prog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                            String urla = riversRef.getDownloadUrl().toString();
                            //String urla = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString().trim();
                            Upload upload = new Upload(iname.getText().toString().trim(),
                                    urla);
                            String uid = dr.push().getKey();
                            dr.child(uid).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            prog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error!! Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    prog.setMessage((int) progress + "% Complete");
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        if (view == cimage) {
            fileChoser();
        }
        if (view == uimage) {
            uploadfile();
        }
        if (view == lout) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
        }
        if (view == seeup) {
            finish();
            startActivity(new Intent(this, download.class));
        }
    }
}
