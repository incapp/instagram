package com.incapp.instagram.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.incapp.instagram.R;
import com.incapp.instagram.models.PostModel;

import java.util.List;

public class PostsListAdapter extends ArrayAdapter<PostModel> {

    private List<PostModel> posts;
    private boolean isProfileView;

    public PostsListAdapter(@NonNull Context context, List<PostModel> posts, boolean isProfileView) {
        super(context, R.layout.row_for_posts_list);
        this.posts = posts;
        this.isProfileView = isProfileView;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textViewDate, textViewCaption, textViewUser;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        PostModel postModel = posts.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_for_posts_list, parent, false);

            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.textViewDate = convertView.findViewById(R.id.textView_date);
            viewHolder.textViewCaption = convertView.findViewById(R.id.textView_caption);
            viewHolder.textViewUser = convertView.findViewById(R.id.textView_user);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(getContext())
                .load(postModel.getDownloadUrl())
                .into(viewHolder.imageView);

//        FirebaseStorage.getInstance()
//                .getReference(uid + "/posts")
//                .child(postModel.getFileName())
//                .getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Glide.with(getContext())
//                                .load(uri)
//                                .into(viewHolder.imageView);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.getMessage();
//                    }
//                });

        viewHolder.textViewCaption.setText(postModel.getCaption());
        viewHolder.textViewDate.setText(postModel.getDateTime());
        if (isProfileView) {
            viewHolder.textViewUser.setVisibility(View.GONE);
        } else {
            viewHolder.textViewUser.setVisibility(View.VISIBLE);
            viewHolder.textViewUser.setText(postModel.getUserName() != null ? postModel.getUserName() : "NA");
        }

        return convertView;
    }
}
