package com.example.assignment2.activity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.assignment2.R;
import com.example.assignment2.adapter.NavigationFragmentPagerAdapter;
import com.example.assignment2.fragment.MeFragment;
import com.example.assignment2.fragment.SquareFragment;
import com.example.assignment2.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                                ViewPager.OnPageChangeListener,
                                                                 RadioGroup.OnCheckedChangeListener{

    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private TextView textTopBar;
    private List<Fragment> mFragments;
    private NavigationFragmentPagerAdapter mAdapter;
    private RadioButton rb_square, rb_me,rb_search,rb_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
    private void initView() {
        // find view
        mViewPager = findViewById(R.id.viewpager);
        mTabRadioGroup = findViewById(R.id.rg_navigation_tab);
        rb_square = findViewById(R.id.rb_square);
        rb_me = findViewById(R.id.rb_me);
        rb_video = findViewById(R.id.rb_video);
        // init fragment
        mFragments = new ArrayList<>(3);
        mFragments.add(new VideoFragment());
        mFragments.add(new SquareFragment());
        mFragments.add(new MeFragment());

        // init view pager
        mAdapter = new NavigationFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(this);
        mTabRadioGroup.setOnCheckedChangeListener(this);
        // adjust navigation tab size
        setNavigationSize(rb_me);
        setNavigationSize(rb_square);
        setNavigationSize(rb_video);
        //set the first page
        rb_square.setChecked(true);
    }

    private void setNavigationSize(RadioButton rb){
        Drawable[] drawables = rb.getCompoundDrawables();
        Rect rect = new Rect();
        // 2/5 means the ratio of scale
        rect.set(0,0,drawables[1].getMinimumWidth() * 2 / 5, drawables[1].getMinimumHeight() * 2 / 5);
        drawables[1].setBounds(rect);
        rb.setCompoundDrawables(null, drawables[1], null, null);
    }

    // View.OnClickListener
    @Override
    public void onClick(View v) {

    }
    // ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
        radioButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    // RadioGroup.OnCheckedChangeListener
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i).getId() == checkedId) {
                mViewPager.setCurrentItem(i);
                return;
            }
        }
    }
}