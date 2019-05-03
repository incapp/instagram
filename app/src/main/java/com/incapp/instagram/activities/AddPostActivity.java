package com.incapp.instagram.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.incapp.instagram.R;
import com.incapp.instagram.models.PostModel;
import com.incapp.instagram.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    private Uri uri;
    private EditText editTextCaption;
    private ImageView imageViewImage;
    private Button buttonPost;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar();

        editTextCaption = findViewById(R.id.editText_caption);
        imageViewImage = findViewById(R.id.imageView);
        buttonPost = findViewById(R.id.button_post);

        progressDialog = new ProgressDialog(AddPostActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please Wait!!!");
        progressDialog.setMessage("Working on it...");

        imageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("image/*");

                startActivityForResult(intent, 1);
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    final String caption = editTextCaption.getText().toString();
                    final String dateTime = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                            .format(new Date());
                    final String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(new Date());
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    Utils.hideKeyboard(AddPostActivity.this);

                    /* Steps for adding new post:
                     * 1. Image is uploaded to Firebase Storage with datetime as filename.
                     * 2. The post data is also stored in Firestore under the user data.
                     * */

                    progressDialog.show();

                    //Step 1
                    FirebaseStorage.getInstance()
                            .getReference(currentUser.getUid() + "/posts")
                            .child(fileName)
                            .putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    FirebaseStorage.getInstance()
                                            .getReference(currentUser.getUid() + "/posts")
                                            .child(fileName)
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final PostModel postModel = new PostModel(
                                                            dateTime,
                                                            fileName,
                                                            caption,
                                                            uri.toString(),
                                                            currentUser.getDisplayName()
                                                    );

                                                    //Step 2
                                                    FirebaseFirestore.getInstance()
                                                            .collection("users")
                                                            .document(currentUser.getUid())
                                                            .update("posts", FieldValue.arrayUnion(postModel))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AddPostActivity.this,
                                                                            "Post Success.", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AddPostActivity.this,
                                                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddPostActivity.this,
                                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this,
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AddPostActivity.this, "No image chosen.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            imageViewImage.setImageURI(uri);

            this.uri = uri;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
