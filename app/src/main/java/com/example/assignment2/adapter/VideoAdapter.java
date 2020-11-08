package com.example.assignment2.adapter;

import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.example.assignment2.R;
import com.example.assignment2.entity.VideoEntity;
import com.example.assignment2.listener.OnItemChildClickListener;
import com.example.assignment2.listener.OnItemClickListener;
import com.example.assignment2.utils.Database;
import com.example.assignment2.utils.SpannableMaker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VideoEntity> videos;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;
    public VideoAdapter(Context context, List<VideoEntity> videos){
        this.mContext = context;
        this.videos = videos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = videos.get(position);
        vh.mPosition = position;
        vh.tvAuthor.setText(videoEntity.getUserID());
        vh.tvTime.setText(videoEntity.getPostTime());
        vh.tvComment.setText(String.valueOf(videoEntity.getCommentCount()));
        vh.tvCollect.setText(String.valueOf(videoEntity.getCollectCount()));
        vh.tvLike.setText(String.valueOf(videoEntity.getLikeCount()));
//        Log.e("Location", postEntity.getLocation());
        if(videoEntity.getLocation()!=null&&!videoEntity.getLocation().equals("")){
            vh.tvLocation.setVisibility(View.VISIBLE);
            vh.tvLocation.setText(videoEntity.getLocation());
//            Log.e("Location2",vh.tvLocation.getText().toString());
        }

        if(videoEntity.getHeader()!=null && !videoEntity.getHeader().equals("")){
            //Picasso.with(mContext).load(videoEntity.getHeader()).transform(new CircleTransform()).into(vh.ivHeader);
            Database.download_headerImage(videoEntity.getHeader(),mContext,vh.ivHeader);
           //Database.download_image(videoEntity.getHeader(),mContext,vh.ivHeader);
            Log.e("Database",videoEntity.getHeader());
        }
        if(videoEntity.getPostText()!=null && !videoEntity.getPostText().equals("")) {
            Spannable spannable = SpannableMaker.buildEmotionSpannable(mContext, videoEntity.getPostText(), (int)vh.tvPostContent.getTextSize());
            vh.tvPostContent.setText(spannable);
            vh.tvPostContent.setVisibility(View.VISIBLE);
        }
        if(videoEntity.getCoverUrl()!=null && !videoEntity.getCoverUrl().equals("")){
            Picasso.with(mContext).load(videoEntity.getCoverUrl()).into(vh.mThumb);
            //Database.download_image(videoEntity.getHeader(),mContext,vh.ivHeader);
            Log.e("Database",videoEntity.getCoverUrl());
        }


    // set image header here
    }
    @Override
    public int getItemCount() {
        return this.videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int mPosition;
        private TextView tvAuthor;
        private TextView tvTime;
        private TextView tvLike;
        private TextView tvComment;
        private TextView tvCollect;
        private TextView tvPostContent;
        private ImageView ivHeader;
        private TextView tvLocation;
        public FrameLayout mPlayerContainer;
        public ImageView mThumb;
        public PrepareView mPrepareView;
        public ViewHolder(@NonNull View View) {
            super(View);
            tvAuthor = View.findViewById(R.id.author);
            tvTime = View.findViewById(R.id.time);
            tvLike = View.findViewById(R.id.like);
            tvCollect = View.findViewById(R.id.collect);
            tvComment= View.findViewById(R.id.comment);
            tvPostContent = View.findViewById(R.id.postContent);
            ivHeader = View.findViewById(R.id.img_header);
            tvLocation= View.findViewById(R.id.location);
            mPlayerContainer = View.findViewById(R.id.player_container);
            mPrepareView = View.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                View.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            View.setTag(this);
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }

        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
