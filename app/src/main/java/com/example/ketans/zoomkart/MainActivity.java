package com.example.ketans.zoomkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private Button mUpload;
    private ImageView mImageView;

    private static final int CAMERA_REQUEST__CODE=1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUpload=(Button) findViewById(R.id.upload);
        mImageView=(ImageView) findViewById(R.id.imageView);
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress= new ProgressDialog(this);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST__CODE);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAMERA_REQUEST__CODE && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading image...");
            mProgress.show();

            Uri uri=data.getData();
            StorageReference filepath=mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    Picasso.with(MainActivity.this).load(downloadUri).fit().centerCrop().into(mImageView);
                    Toast toast = Toast.makeText(MainActivity.this, "Uploading finished", Toast.LENGTH_LONG);
                }
            });
        }
    }
}
