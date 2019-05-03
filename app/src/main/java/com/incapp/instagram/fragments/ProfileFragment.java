package com.incapp.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.incapp.instagram.R;
import com.incapp.instagram.activities.LoginActivity;
import com.incapp.instagram.adapters.PostsListAdapter;
import com.incapp.instagram.models.PostModel;
import com.incapp.instagram.models.UserModel;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView textViewEmail;
    private TextView textViewName;
    private ListView listViewPosts;
    private TextView textViewPosts;
    private ImageView imageViewImage;
    Button buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewEmail = view.findViewById(R.id.textView_email);
        textViewName = view.findViewById(R.id.textView_name);
        listViewPosts = view.findViewById(R.id.listView_posts);
        textViewPosts = view.findViewById(R.id.textView_posts);
        imageViewImage = view.findViewById(R.id.imageView_image);
        buttonLogout = view.findViewById(R.id.button_logout);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        textViewName.setText(currentUser.getDisplayName());
        textViewEmail.setText(currentUser.getEmail());

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);

                        if (userModel.getPosts() == null)
                            userModel.setPosts(new ArrayList<PostModel>());

                        textViewPosts.setText("Posts\n" + userModel.getPosts().size());

                        listViewPosts.setAdapter(new PostsListAdapter(
                                getActivity(),
                                userModel.getPosts(),
                                true
                        ));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));

                getActivity().finish();
            }
        });

        return view;
    }
}
