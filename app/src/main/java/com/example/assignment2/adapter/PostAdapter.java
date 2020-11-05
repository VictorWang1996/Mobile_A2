package com.example.assignment2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.entity.PostEntity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PostEntity> posts;

    public PostAdapter(Context context, List<PostEntity> posts){
        this.mContext = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ViewHolder vh = (ViewHolder) holder;
    PostEntity postEntity = posts.get(position);
    vh.tvAuthor.setText(postEntity.getUserID());
    vh.tvTime.setText(postEntity.getPostTime());
    vh.tvComment.setText(String.valueOf(postEntity.getCommentCount()));
    vh.tvCollect.setText(String.valueOf(postEntity.getCollectCount()));
    vh.tvLike.setText(String.valueOf(postEntity.getLikeCount()));

    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAuthor;
        private TextView tvTime;
        private TextView tvLike;
        private TextView tvComment;
        private TextView tvCollect;
        public ViewHolder(@NonNull View View) {
            super(View);
            tvAuthor = View.findViewById(R.id.author);
            tvTime = View.findViewById(R.id.time);
            tvLike = View.findViewById(R.id.like);
            tvCollect = View.findViewById(R.id.collect);
            tvComment= View.findViewById(R.id.comment);
        }
    }
}
