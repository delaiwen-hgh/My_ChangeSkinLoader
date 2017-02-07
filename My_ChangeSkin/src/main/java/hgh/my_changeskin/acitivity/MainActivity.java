package hgh.my_changeskin.acitivity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import hgh.my_changeskin.R;
import hgh.my_changeskin.adapter.FragmentAdapter;
import hgh.my_changeskin.fragment.SkinFragment;
import solid.ren.skinlibrary.base.SkinBaseActivity;

public class MainActivity extends SkinBaseActivity
{
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTabTitle = {"基本换肤","动态添加","设置"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("换肤Demo");
        setSupportActionBar(mToolbar);
       // dynamicAddView(mToolbar, "titleTextColor", R.color.colorAccent); //一个View 只能设置一个皮肤属性
        dynamicAddView(mToolbar, "background", R.color.colorPrimaryDark);


        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

        for (int i = 0; i < mTabTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitle[i])); //addTab 才能切换字体
        }

        dynamicAddView(mTabLayout, "tabSelectedTextColor", R.color.colorPrimaryDark);//一个View 只能设置一个皮肤属性
        dynamicAddView(mTabLayout, "background", R.color.item_day);//一个View 只能设置一个皮肤属性
      //  dynamicAddView(mTabLayout, "tabLayoutIndicator", R.color.colorPrimaryDark);

       mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mTabTitle));
        mTabLayout.setupWithViewPager(mViewPager);

    }


}
