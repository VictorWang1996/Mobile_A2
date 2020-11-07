package com.example.assignment2.adapter;

import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.entity.PostEntity;
import com.example.assignment2.utils.Database;
import com.example.assignment2.utils.SpannableMaker;
import com.example.assignment2.view.SquareImageView;

import java.util.List;

public class SearchPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PostEntity> posts;

    public SearchPostAdapter(Context context, List<PostEntity> posts){
        this.mContext = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.gridLayout.removeAllViews();
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
        vh.tvLocation.setText(String.valueOf(postEntity.getLocation()));
        vh.tvLocation.setVisibility(View.VISIBLE);
        if(postEntity.getHeader()!=null && !postEntity.getHeader().equals("")){
            Database.download_image(postEntity.getHeader(),mContext,vh.ivHeader);
            Log.e("Database",postEntity.getHeader());
        }
        if(postEntity.getPostText()!=null && !postEntity.getPostText().equals("")) {
            Spannable spannable = SpannableMaker.buildEmotionSpannable(mContext, postEntity.getPostText(), (int)vh.tvPostContent.getTextSize());
            vh.tvPostContent.setText(spannable);
            vh.tvPostContent.setVisibility(View.VISIBLE);
        }
        int columnCount= vh.gridLayout.getColumnCount();//get column
//        int marginSize = PixelUtils.dp2px(mContext, 4);//得到经过dp转化的margin值
        //遍历集合 动态添加
    if (postEntity.getPostImgPath()!=null && postEntity.getPostImgPath().size()!=0){

        for (int i = 0; i < postEntity.getPostImgPath().size(); i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);//行数
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount, 1.0f);//列数 列宽的比例 weight=1
            ImageView imageView = new SquareImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //由于宽（即列）已经定义权重比例 宽设置为0 保证均分
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rowSpec = rowSpec;
            layoutParams.columnSpec = columnSpec;
            Database.download_image(postEntity.getPostImgPath().get(i), mContext, imageView);
//            layoutParams.setMargins(marginSize, marginSize, marginSize, marginSize);
            vh.gridLayout.addView(imageView, layoutParams);
        }
        vh.gridLayout.setVisibility(View.VISIBLE);
    }
    // set image header here
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
        private TextView tvPostContent;
        private ImageView ivHeader;
        private GridLayout gridLayout;
        private TextView tvLocation;
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
            gridLayout = View.findViewById(R.id.gridlayout_post);
        }
    }
}
