package com.example.assignment2.imageTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.assignment2.Database;
import com.example.assignment2.R;

public class GridImageActivity extends AppCompatActivity {
    private GridLayout gridlayoutPost;
    private String image_name = "Screen Shot 2020-10-18 at 3.34.01 pm.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image);
        gridlayoutPost = findViewById(R.id.gridlayout_post);
        gridlayoutPost.removeAllViews();//清空子视图 防止原有的子视图影响
        int columnCount=gridlayoutPost.getColumnCount();//得到列数
//        int marginSize = PixelUtils.dp2px(mContext, 4);//得到经过dp转化的margin值
        //遍历集合 动态添加
        for (int i = 0; i < 9; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);//行数
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount, 1.0f);//列数 列宽的比例 weight=1
            ImageView imageView = new SquareImageView(GridImageActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //由于宽（即列）已经定义权重比例 宽设置为0 保证均分
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rowSpec = rowSpec;
            layoutParams.columnSpec = columnSpec;
            Database.download_image(image_name, GridImageActivity.this, imageView);

//            layoutParams.setMargins(marginSize, marginSize, marginSize, marginSize);

            gridlayoutPost.addView(imageView, layoutParams);
        }
    }
}