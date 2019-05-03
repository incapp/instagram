package com.incapp.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.incapp.instagram.R;
import com.incapp.instagram.adapters.PostsListAdapter;
import com.incapp.instagram.models.PostModel;
import com.incapp.instagram.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    ListView listViewPosts;
    List<PostModel> allPosts = new ArrayList<>();
    PostsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listViewPosts = view.findViewById(R.id.listView_posts);

        adapter = new PostsListAdapter(getActivity(), allPosts, false);

        listViewPosts.setAdapter(adapter);

        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            UserModel userModel = snapshot.toObject(UserModel.class);

                            if (userModel.getPosts() != null)
                                allPosts.addAll(userModel.getPosts());
                        }

                        Collections.sort(allPosts);

                        adapter.notifyDataSetChanged();
                    }
                });

        return view;
    }
}
