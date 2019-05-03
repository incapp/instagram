package com.incapp.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.incapp.instagram.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private ListView listView;
    private EditText editText;

    List<String> names = new ArrayList<>();
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editText = view.findViewById(R.id.editText);
        listView = view.findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                names
        );

        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().toUpperCase();

                if (query.isEmpty()) {
                    names.clear();

                    adapter.notifyDataSetChanged();
                } else {
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .whereGreaterThanOrEqualTo("upperCaseName", query)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    names.clear();
                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        names.add(snapshot.get("name").toString());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });

        return view;
    }
}
